package mihnayan.divetojava.frontend;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.UserId;
import mihnayan.divetojava.gamemechanics.MainGameMechanics;
import mihnayan.divetojava.resourcesystem.GameSessionResource;
import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceNotExistException;

public class GameDataRequestHandler extends RequestHandler {
	
	private static Logger log = Logger.getLogger(GameDataRequestHandler.class.getName());
	
	public static volatile GameState gameState = GameState.WAITING_FOR_QUORUM;
	public static volatile GameData gameData = null;
	
	private static final String GAME_RESOURCE_FILE = "GameResource.xml";
	
	private String sessionId;
	private String userName;
	private UserId userId;
	private AuthState loginStatus = AuthState.NOT_LOGGED;

	public GameDataRequestHandler(HttpServletRequest request, Abonent abonent) {
		super(request, abonent);
		sessionId = request.getSession().getId();
		HttpSession session = LoginRequestHandler.getSession(sessionId);
		if (session == null || LoginRequestHandler.getAuthState(session) != AuthState.LOGGED) return;
		
		loginStatus = AuthState.LOGGED;
		userId = (UserId) session.getAttribute("userId");
		userName = LoginRequestHandler.authenticatedUsers.get(userId).getUserName();
		
		if (gameState != GameState.GAMEPLAY) startGame();
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
	 * Rules of building user data 
	 * User data are json format:
	 * {
	 *     "player": {
	 *         "userName": "user name",
	 *         "userId": 0
	 *     },
	 *     "opponent": {
	 *         "userName": "user name"
	 *     },
	 *     "gameState": GameState,
	 *     "loginStatus": AuthState,
	 *     "gameData": {
	 *         "elapsedTime": "123456789"
	 *     }
	 * }
	 */
	@Override
	public String toJSON() {
		return "{"
				+ "\"player\": {"
					+ "\"userName\": " + (userName == null ? null : "\"" + userName + "\"") + ", "
					+ "\"userId\": " + (userId == null ? 0 : userId)  
					+ "}, "
				+ "\"opponent\": {"
					+ "\"userName\": \"" + getOpponentUserName(userId) + "\""
					+ "},"
				+ "\"gameState\": \"" + gameState + "\","
				+ "\"loginStatus\": \"" + loginStatus + "\"," 
				+ "\"gameData\": " + gameDataToJSON()
				+ "}";
	}
	
	/**
	 * Returns the name of the first user 
	 * whose ID does not match the ID of the user that sent the request
	 * @return
	 */
	private String getOpponentUserName(UserId playerId) {
		if (playerId == null || gameState != GameState.GAMEPLAY) return "";
		
		Set<UserId> keys = LoginRequestHandler.authenticatedUsers.keySet();
		Iterator<UserId> iterator = keys.iterator();
		UserId opponentId = iterator.next();
		if (opponentId.equals(playerId)) opponentId = iterator.next();

		return LoginRequestHandler.authenticatedUsers.get(opponentId).getUserName();
	}
	
	private String gameDataToJSON() {
		if (gameData == null) return null;
		StringBuffer json = new StringBuffer("{");
		json.append("\"elapsedTime\": ").append(gameData.getElapsedTime());
		json.append("}");
		
		return json.toString();
	}
	
	private void startGame() {
		GameSessionResource gsr = null;
		try {
			gsr = (GameSessionResource) ResourceFactory.instance().get(GAME_RESOURCE_FILE);
		} catch (ResourceNotExistException e) {
			log.log(Level.WARNING, "Can't start game! Cause: " + GAME_RESOURCE_FILE + " not found.");
			return;
		}
		if (LoginRequestHandler.authenticatedUsers.size() < gsr.getRequiredPlayers()) return;
		Iterator<UserId> iterator = LoginRequestHandler.authenticatedUsers.keySet().iterator();
		UserId user1 = iterator.next();
		UserId user2 = iterator.next();
		
		MessageService ms = abonent.getMessageService();
		Address to = ms.getAddressService().getAddress(MainGameMechanics.class);
		ms.sendMessage(new MsgStartGameSession(abonent.getAddress(), to, user1, user2));
		GameDataRequestHandler.gameState = GameState.GAMEPLAY;
		log.info("Start game message was sent");
	}

}
