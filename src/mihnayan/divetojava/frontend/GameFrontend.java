package mihnayan.divetojava.frontend;

import java.io.IOException;
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
import mihnayan.divetojava.base.MessageService;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class GameFrontend extends AbstractHandler implements Runnable, Frontend {
	
	private static Logger log = Logger.getLogger(GameFrontend.class.getName());
	
	private static ConcurrentHashMap<String, HttpSession> authenticatedSessions = 
			new ConcurrentHashMap<String, HttpSession>();
	
	private MessageService ms;
	private Address address;
	private AtomicInteger handleCount;
	
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
					+ "\"sessionId\": \"" + sessionId + "\","
					+ "\"userName\": \"" + (userName == null ? "" : userName) + "\","
					+ "\"userId\": \"" + (userId == null ? "" : userId) + "\","
					+ "\"loginStatus\": \"" + loginStatus + "\","
					+ "\"text\": \"" + statusText + "\""
					+ "}";
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
			LoginHandler loginHandler = new LoginHandler(request);
			loginHandler.handleRequest(response);
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
	public void setUserId(String sessionId, int userId) {
		HttpSession session = authenticatedSessions.get(sessionId);
		if (userId != 0) {
			session.setAttribute("userId", userId);
			setAuthState(session, AuthState.LOGGED);
		}
		else
			setAuthState(session, AuthState.NOT_LOGGED);
	}
	
	private void registerSession(HttpSession session, String userName) {
		
		String sessionId = session.getId();
		
		session.setAttribute("userName", userName);
		session.setAttribute("userId", null);
		setAuthState(session, AuthState.WAITING);
		authenticatedSessions.put(sessionId, session);
		
		Address to = ms.getAddressService().getAddress(AccountServer.class);
		ms.sendMessage(new MsgGetUserId(address, to, userName, sessionId));
	}
	
	private void unregisterSession(HttpSession session) {
		setAuthState(session, AuthState.NEW);
		session.removeAttribute("userName");
		authenticatedSessions.remove(session.getId());
	}
	
	private void setAuthState(HttpSession session, AuthState authState) {
		session.setAttribute("authState", authState);
	}
	
	private AuthState getAuthState(HttpSession session) {
		if (session.isNew()) setAuthState(session, AuthState.NEW);
		return (AuthState) session.getAttribute("authState");
	}
}
