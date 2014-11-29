package mihnayan.divetojava.frontend;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.gamemechanics.MainGameMechanics;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class GameFrontend extends AbstractHandler implements Runnable, Frontend {
	
	private static Logger log = Logger.getLogger(GameFrontend.class.getName());
	
	private MessageService ms;
	private Address address;
	private AtomicInteger handleCount;
	
	private static final byte REQUIRED_PLAYERS = 2;
	private GameState gameState = GameState.WAITING_FOR_QUORUM;
	private GameData gameData;
	
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
	private class GameHandler {
		
		private String sessionId;
		private String userName;
		private Integer userId = 0;
		private AuthState loginStatus = AuthState.NOT_LOGGED;
		
		public GameHandler(HttpServletRequest request) {
			sessionId = request.getSession().getId();
//			HttpSession session = sessions.get(sessionId);
			HttpSession session = LoginRequestHandler.getSession(sessionId);
			if (session == null || LoginRequestHandler.getAuthState(session) != AuthState.LOGGED) return;
			
			loginStatus = AuthState.LOGGED;
			userId = (Integer) session.getAttribute("userId");
			userName = LoginRequestHandler.authenticatedUsers.get(userId).getUserName();
		}
		
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
		private String getOpponentUserName(int playerId) {
			if (playerId == 0 || gameState != GameState.GAMEPLAY) return "";
			
			Set<Integer> keys = LoginRequestHandler.authenticatedUsers.keySet();
			Iterator<Integer> iterator = keys.iterator();
			int opponentId = iterator.next();
			if (opponentId == playerId) opponentId = iterator.next();

			return LoginRequestHandler.authenticatedUsers.get(opponentId).getUserName();
		}
		
		private String gameDataToJSON() {
			if (gameData == null) return null;
			StringBuffer json = new StringBuffer("{");
			json.append("\"elapsedTime\": ").append(gameData.getElapsedTime());
			json.append("}");
			
			return json.toString();
		}
	}
	
	public GameFrontend(MessageService ms) {
		super();
		this.ms = ms;
		address = new Address();
		
		handleCount = new AtomicInteger();
	}

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		baseRequest.setHandled(true);
		
		handleCount.incrementAndGet();
		
		if ("/login".equals(target)) {			
			LoginRequestHandler loginHandler = new LoginRequestHandler(request, this);
			loginHandler.handleRequest(response);
		}
		
		if ("/gameData".equals(target)) {
			GameHandler gameHandler = new GameHandler(request);
			gameHandler.handleRequest(response);
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				ms.execForAbonent(this);
				if (gameState != GameState.GAMEPLAY) startGame();
				Thread.sleep(100);
			} catch(InterruptedException e) {
				return;
			}
//			log.info("This page was handled " + handleCount + " times...");
		}
	}
	
	@Override
	public Address getAddress() {
		return address;
	}
	
	@Override
	public MessageService getMessageService() {
		return ms;
	}
	
	@Override
	public void setUser(String sessionId, int userId, String userName) {
		LoginRequestHandler.setUser(sessionId, userId, userName);
	}
	
	@Override
	public void setGameData(GameData gameData) {
		this.gameData = gameData;
	}

	private void startGame() {
		if (LoginRequestHandler.authenticatedUsers.size() < REQUIRED_PLAYERS) return;
		Iterator<Integer> iterator = LoginRequestHandler.authenticatedUsers.keySet().iterator();
		int user1 = iterator.next();
		int user2 = iterator.next();
		Address to = ms.getAddressService().getAddress(MainGameMechanics.class);
		ms.sendMessage(new MsgStartGameSession(address, to, user1, user2));
		gameState = GameState.GAMEPLAY;
		log.info("Start game message was sent");
	}
}
