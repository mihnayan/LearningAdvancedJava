package mihnayan.divetojava.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mihnayan.divetojava.accountsrv.AccountServer;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.gamemechanics.MainGameMechanics;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class GameFrontend extends AbstractHandler implements Runnable, Frontend {
	
	private static Logger log = Logger.getLogger(GameFrontend.class.getName());
	
	private static ConcurrentHashMap<String, HttpSession> sessions = 
			new ConcurrentHashMap<String, HttpSession>();
	private Map<Integer, UserProfile> authenticatedUsers = new HashMap<Integer, UserProfile>();
	
	private MessageService ms;
	private Address address;
	private AtomicInteger handleCount;
	
	private static final byte REQUIRED_PLAYERS = 2;
	private GameState gameState;
	private GameData gameData;
	
	/**
	 * Rules of building user data 
	 * User data are json format:
	 * {
	 *     sessionId: 0,
	 *     userName: "",
	 *     userId: "",
	 *     loginStatus: AuthState,
	 *     text: "some text"
	 * }   
	 */
	private class LoginHandler {
		private String sessionId;
		private String userName;
		private Integer userId;
		private AuthState loginStatus;
		private String statusText = "";
		private HttpSession session;
		
		// field for user name in html form
		private final String FRM_USER_NAME = "user-name";
		
		public LoginHandler(HttpServletRequest request) {
			session = request.getSession();
			
			sessionId = session.getId();
			userName = (String) session.getAttribute("userName");
			if (userName == null)
				userName = request.getParameter(FRM_USER_NAME);
			userId = (Integer) session.getAttribute("userId");
			loginStatus = getAuthState(session);
		}
		
		public void handleRequest(HttpServletResponse response) {
			
			if (loginStatus == AuthState.NEW && userName != null) {
				registerSession(session, userName);
				loginStatus = getAuthState(session);
			}
				
			if (loginStatus == AuthState.NOT_LOGGED)
				unregisterSession(session);
			
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
					+ "\"sessionId\": \"" + sessionId + "\", "
					+ "\"userName\": \"" + (userName == null ? "" : userName) + "\", "
					+ "\"userId\": " + (userId == null ? 0 : userId) + ", "
					+ "\"loginStatus\": \"" + loginStatus + "\", "
					+ "\"text\": \"" + statusText + "\""
					+ "}";
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
	 *     "gameData": {
	 *         "elapsedTime": "123456789"
	 *     }
	 * }
	 */
	private class GameHandler {
		
		String sessionId;
		private String userName;
		private Integer userId;
		
		String opponentSessionId;
		private String opponentUserName;
		
		public GameHandler(HttpServletRequest request) {
			sessionId = request.getSession().getId();
			HttpSession session = sessions.get(sessionId);
			if (session == null || !isAuthenticated(session)) return;
			
			userName = (String) session.getAttribute("userName");
			userId = (Integer) session.getAttribute("userId");
			
			gameState = (GameState) session.getAttribute("gameState");
			if (gameState == GameState.GAMEPLAY) return;
			
			opponentSessionId = getOpponentSessionId();
			if (opponentSessionId == null) return;
			
			HttpSession opponentSession = sessions.get(opponentSessionId);
			opponentUserName = (String) opponentSession.getAttribute("userName");
			Integer opponentUserId = (Integer) opponentSession.getAttribute("userId");
			
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
						+ "\"userName\": \"" + opponentUserName + "\""
						+ "},"
					+ "\"gameState\": \"" + gameState + "\"" 
					+ "}";
		}
		
		/**
		 * Returns the ID of the first session in which the userId does not match the userId 
		 * of the user who sent the request.
		 * @return
		 */
		private String getOpponentSessionId() {
			Set<String> keys = sessions.keySet();
			keys.remove(sessionId);
			for (String key : keys) {
				if (userId != (Integer) sessions.get(key).getAttribute("userId"))
						return key;
			}
			
			return null;
		}
	}
	
	public GameFrontend(MessageService ms) {
		super();
		this.ms = ms;
		address = new Address();
		
		gameState = GameState.WAITING_FOR_QUORUM;
		
		handleCount = new AtomicInteger();
	}

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		baseRequest.setHandled(true);
		
		handleCount.incrementAndGet();
		
		if ("/login".equals(target)) {			
			LoginHandler loginHandler = new LoginHandler(request);
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
				Thread.sleep(5000);
			} catch(InterruptedException e) {
				return;
			}
			log.info("This page was handled " + handleCount + " times...");
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
		HttpSession session = sessions.get(sessionId);
		if (userId != 0) {
			session.setAttribute("userId", userId);
			setAuthState(session, AuthState.LOGGED);
			UserProfile userProfile = new UserProfile();
			userProfile.setUserName(userName);
			authenticatedUsers.put(userId, userProfile);
		}
		else
			setAuthState(session, AuthState.NOT_LOGGED);
	}
	
	@Override
	public void setGameData(GameData gameData) {
		this.gameData = gameData;
	}

	private void registerSession(HttpSession session, String userName) {
		
		String sessionId = session.getId();
		
		session.setAttribute("userName", userName);
		session.setAttribute("userId", null);
		setAuthState(session, AuthState.WAITING);
		sessions.put(sessionId, session);
		
		Address to = ms.getAddressService().getAddress(AccountServer.class);
		ms.sendMessage(new MsgGetUserId(address, to, userName, sessionId));
	}
	
	private void unregisterSession(HttpSession session) {
		setAuthState(session, AuthState.NEW);
		session.removeAttribute("userName");
		sessions.remove(session.getId());
	}
	
	private void setAuthState(HttpSession session, AuthState authState) {
		session.setAttribute("authState", authState);
	}
	
	private AuthState getAuthState(HttpSession session) {
		if (session.isNew()) setAuthState(session, AuthState.NEW);
		return (AuthState) session.getAttribute("authState");
	}
	
	private boolean isAuthenticated(HttpSession session) {
		AuthState state = (AuthState) session.getAttribute("authState");
		return state != null && state == AuthState.LOGGED;
	}
	
	private void startGame() {
		if (authenticatedUsers.size() < REQUIRED_PLAYERS) return;
		Iterator<Integer> iterator = authenticatedUsers.keySet().iterator();
		int user1 = iterator.next();
		int user2 = iterator.next();
		Address to = ms.getAddressService().getAddress(MainGameMechanics.class);
		ms.sendMessage(new MsgStartGameSession(address, to, user1, user2));
		log.info("Start game message was sent");
	}
}
