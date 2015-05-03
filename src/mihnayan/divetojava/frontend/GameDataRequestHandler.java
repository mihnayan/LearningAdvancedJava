package mihnayan.divetojava.frontend;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.base.GameState;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.base.UserSession;
import mihnayan.divetojava.gamemechanics.MainGameMechanics;
import mihnayan.divetojava.utils.DtjHelper;

/**
 * Helper class that handle requests for the current state of the game.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
class GameDataRequestHandler extends AbstractRequestHandler {

    private static Logger log = Logger.getLogger(GameDataRequestHandler.class
            .getName());

    private UserSession session;
    private AuthState loginStatus = AuthState.FAILED;
    private GameState gameState = GameState.WAITING_FOR_QUORUM;  
    private GameData gameData;
    
    private final MessageService ms;
    private final Address gameMechanicsAddress;

    /**
     * Creates GameDataRequestHandler object.
     * @param request The request to be processed.
     * @param abonent The subscriber, who may receive and process the game data (typically,
     *        the implementation of the GameMechanics interface).
     */
    public GameDataRequestHandler(HttpServletRequest request, Frontend frontend) {
        super(request, frontend);
        
        ms = frontend.getMessageService();
        gameMechanicsAddress = ms.getAddressService().getAddress(MainGameMechanics.class);
        
        String sessionId = request.getSession().getId();
        session = LoginRequestHandler.getAuthenticatedSession(sessionId);
        if (session == null) {
            return;
        }

        loginStatus = AuthState.LOGGED;

        if (gameState != GameState.GAMEPLAY) {
            startGame();
        }
        
        if (gameState == GameState.GAMEPLAY) {
            gameData = session.getCurrentGameData();
            ms.sendMessage(
                    new MsgRequestGameData(frontend.getAddress(), gameMechanicsAddress, 
                            session.getUser()));
        }
    }

    @Override
    public void buildResponse(HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().println(toJSON());
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Rules of building user data.
     * User data are json format:<br />
     * { "player": {<br />
     * &nbsp;&nbsp;&nbsp;&nbsp;"userName": "user name",<br />
     * &nbsp;&nbsp;&nbsp;&nbsp;"userId": 0,<br />
     * &nbsp;&nbsp;&nbsp;&nbsp;"fullName": "fullname"<br />&nbsp;&nbsp;&nbsp;&nbsp;},<br />
     * "gameState": GameState,<br />
     * "loginStatus": AuthState,<br />
     * "gameData": {<br />
     * &nbsp;&nbsp;&nbsp;&nbsp;"elapsedTime": "123456789" } }
     */
    @Override
    public String toJSON() {
        String userName = "";
        String userId = "";
        String userFullName = "";
        if (session != null) {
            userName = session.getUser().getUsername();
            userId = session.getUser().getId().toString();
            userFullName = session.getUser().getFullName();
        }
        return "{" 
                + "\"player\": {" 
                    + DtjHelper.buildJSONParameter("userName", userName) + ", "
                    + DtjHelper.buildJSONParameter("userId", userId) + ", "
                    + DtjHelper.buildJSONParameter("fullName", userFullName) + "}, "
                + DtjHelper.buildJSONParameter("gameState", gameState) + ", "
                + DtjHelper.buildJSONParameter("loginStatus", loginStatus) + ", "
                + "\"gameData\": " + gameDataToJSON() + "}";
    }

    private String gameDataToJSON() {
        StringBuilder json = new StringBuilder("{");
        
        if (gameData != null) {
            json.append("\"elapsedTime\": ").append(gameData.getElapsedTime()).append(", ");
            json.append("\"opponents\": [");
            for (User user : gameData.getOpponents()) {
                json.append("\"").append(user.getFullName()).append("\",");
            }
            json.delete(json.length() - 1, json.length()).append("]");
        } else {
            json.append("");
        }
        json.append("}");

        return json.toString();
    }

    private void startGame() {
        if (gameState == GameState.GAMEPLAY) {
            return;
        }
        
        Set<User> players = LoginRequestHandler.getAuthenticatedUsers();
        if (players.size() < frontend.getMinPlayersCount()) {
            return;
        }
        
        ms.sendMessage(
                new MsgStartGameSession(frontend.getAddress(), gameMechanicsAddress, players));
        gameState = GameState.GAMEPLAY;
        log.info("Start game message was sent");
    }

}
