package mihnayan.divetojava.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mihnayan.divetojava.accountsrv.AccountServer;
import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.UserId;

public class LoginRequestHandler extends RequestHandler {
	
	private static Logger log = Logger.getLogger(LoginRequestHandler.class.getName());
	
	private static ConcurrentHashMap<String, HttpSession> sessions = 
			new ConcurrentHashMap<String, HttpSession>();
	public static Map<UserId, UserProfile> authenticatedUsers = 
			new HashMap<UserId, UserProfile>();
	
	private String sessionId;
	private String userName;
	private UserId userId;
	private AuthState loginStatus;
	private String statusText;
	
	private HttpSession session;
	
	// field for user name in html form
	private final String FRM_USER_NAME = "user-name";

	public LoginRequestHandler(HttpServletRequest request, Abonent abonent) {
		super(request, abonent);
		session = request.getSession();
		
		sessionId = session.getId();
		userName = (String) session.getAttribute("userName");
		if (userName == null)
			userName = request.getParameter(FRM_USER_NAME);
		userId = (UserId) session.getAttribute("userId");
		loginStatus = getAuthState(session);
	}
	
	public static void setAuthState(HttpSession session, AuthState authState) {
		session.setAttribute("authState", authState);
	}
	
	public static AuthState getAuthState(HttpSession session) {
		if (session.getAttribute("authState") == null || session.isNew()) {
			setAuthState(session, AuthState.NEW);
		}
		return (AuthState) session.getAttribute("authState");
	}
	
	public static HttpSession getSession(String sessionId) {
		return sessions.get(sessionId);
	}
	
	public static void setUser(String sessionId, UserId userId, String userName) {
		HttpSession session = sessions.get(sessionId);
		if (userId != null) {
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
	@Override
	public String toJSON() {
		return "{"
				+ "\"sessionId\": \"" + sessionId + "\", "
				+ "\"userName\": \"" + (userName == null ? "" : userName) + "\", "
				+ "\"userId\": " + (userId == null ? 0 : userId) + ", "
				+ "\"loginStatus\": \"" + loginStatus + "\", "
				+ "\"text\": \"" + statusText + "\""
				+ "}";
	}
	
	private void registerSession(HttpSession session, String userName) {
		
		String sessionId = session.getId();
		
		session.setAttribute("userName", userName);
		session.setAttribute("userId", null);
		setAuthState(session, AuthState.WAITING);
		sessions.put(sessionId, session);
	
		MessageService ms = abonent.getMessageService();
		Address to = ms.getAddressService().getAddress(AccountServer.class);
		
		ms.sendMessage(new MsgGetUserId(abonent.getAddress(), to, userName, sessionId));
	}
	
	private void unregisterSession(HttpSession session) {
		setAuthState(session, AuthState.NEW);
		session.removeAttribute("userName");
		sessions.remove(session.getId());
	}

}
