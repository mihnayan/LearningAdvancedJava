package mihnayan.divetojava.frontend;

import java.io.IOException;
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

/**
 * Helper class that handle requests for the current state of the game.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
class GameDataRequestHandler extends AbstractRequestHandler {

    private static Logger log = Logger.getLogger(GameDataRequestHandler.class
            .getName());

    public static volatile GameState gameState = GameState.WAITING_FOR_QUORUM;

    private String sessionId;
    private User user;
    private AuthState loginStatus = AuthState.FAILED;
    
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
        
        sessionId = request.getSession().getId();
        UserSession session = LoginRequestHandler.getAuthenticatedSession(sessionId);
        if (session == null) {
            return;
        }

        loginStatus = AuthState.LOGGED;
        user = session.getUser();

        if (gameState != GameState.GAMEPLAY) {
            startGame();
        }
        
        if (gameState == GameState.GAMEPLAY) {
            ms.sendMessage(
                    new MsgRequestGameData(frontend.getAddress(), gameMechanicsAddress, user));
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
     * {"player": {<br />
     * &nbsp;&nbsp;&nbsp;&nbsp;"userName": "user name", "userId": 0 }, "opponent": { "userName":
     * "user name" }, "gameState": GameState, "loginStatus": AuthState,
     * "gameData": { "elapsedTime": "123456789" } }
     */
    @Override
    public String toJSON() {
        return "{" + "\"player\": {" + "\"userName\": "
                + (user == null ? null : "\"" + user.getUsername() + "\"") + ", "
                + "\"userId\": \"" + (user == null ? "" : user.getId()) + "\"}, "
                + "\"gameState\": \"" + gameState + "\","
                + "\"loginStatus\": \"" + loginStatus + "\","
                + "\"gameData\": " + gameDataToJSON() + "}";
    }

    private String gameDataToJSON() {
        GameData gameData = 
                LoginRequestHandler.authenticatedSessions.get(sessionId).getCurrentGameData();
        if (gameData == null) {
            return null;
        }
        StringBuilder json = new StringBuilder("{");
        json.append("\"elapsedTime\": ").append(gameData.getElapsedTime()).append(", ");
        json.append("\"opponents\": [");
        
        for (User user : gameData.getOpponents()) {
            json.append("\"").append(user.getUsername()).append("\"");
        }
        
        json.append("]");
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
        GameDataRequestHandler.gameState = GameState.GAMEPLAY;
        log.info("Start game message was sent");
    }

}
