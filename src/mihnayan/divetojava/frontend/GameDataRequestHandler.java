package mihnayan.divetojava.frontend;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.base.GameState;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.base.UserId;
import mihnayan.divetojava.base.UserSession;
import mihnayan.divetojava.gamemechanics.MainGameMechanics;

/**
 * Helper class that handle requests for the current state of the game.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public class GameDataRequestHandler extends AbstractRequestHandler {

    private static Logger log = Logger.getLogger(GameDataRequestHandler.class
            .getName());

    public static volatile GameState gameState = GameState.WAITING_FOR_QUORUM;
    public static volatile GameData gameData = null;

    private String sessionId;
    private User user;
    private AuthState loginStatus = AuthState.FAILED;

    /**
     * Creates GameDataRequestHandler object.
     * @param request The request to be processed.
     * @param abonent The subscriber, who may receive and process the game data (typically,
     *        the implementation of the GameMechanics interface).
     */
    public GameDataRequestHandler(HttpServletRequest request, Frontend frontend) {
        super(request, frontend);
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
    }

    @Override
    public void handleRequest(HttpServletResponse response) {
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
                + "\"opponent\": {" + "\"userName\": \""
                //TODO: Здесь оппонента надо получать из gameData.
                + defineOpponent(user).getUsername() + "\"" + "},"
                + "\"gameState\": \"" + gameState + "\","
                + "\"loginStatus\": \"" + loginStatus + "\","
                + "\"gameData\": " + gameDataToJSON() + "}";
    }

    /**
     * Returns the first user whose does not match of the user that sent the request.
     * @return Opponent or null if opponent was not found.
     */
    private User defineOpponent(User player) {
        if (player == null) {
            return null;
        }
        
        Collection<UserSession> userSessions = LoginRequestHandler.authenticatedSessions.values();
        Iterator<UserSession> iterator = userSessions.iterator();
        User opponent = player;
        while (player.equals(opponent) && iterator.hasNext()) {
            opponent = iterator.next().getUser();
        }
        return opponent;
    }

    private String gameDataToJSON() {
        if (gameData == null) {
            return null;
        }
        StringBuffer json = new StringBuffer("{");
        json.append("\"elapsedTime\": ").append(gameData.getElapsedTime());
        json.append("}");

        return json.toString();
    }

    private void startGame() {
        if (gameState == GameState.GAMEPLAY ||
                LoginRequestHandler.authenticatedSessions.size() < frontend.getMinPlayersCount()) {
            return;
        }
        User opponent = defineOpponent(user);
        if (opponent == null) {
            return;
        }

        MessageService ms = frontend.getMessageService();
        Address to = ms.getAddressService().getAddress(MainGameMechanics.class);
        ms.sendMessage(new MsgStartGameSession(frontend.getAddress(), to,
                LoginRequestHandler.getAuthenticatedUsers()));
        GameDataRequestHandler.gameState = GameState.GAMEPLAY;
        log.info("Start game message was sent");
    }

}
