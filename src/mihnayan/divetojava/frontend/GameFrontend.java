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
	
	// field for user name in html form
	private final String FRM_USER_NAME = "user-name";
	
	private final String[] targets = {"login", "welcome"};
	
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
	private class LoginDataBuilder {
		private String sessionId;
		private String userName;
		private Integer userId;
		private AuthState loginStatus;
		private String statusText = "";
		private HttpSession session;
		
		public LoginDataBuilder(HttpServletRequest request) {
			session = request.getSession();
			sessionId = session.getId();
			userName = (String) session.getAttribute("userName");
			if (userName == null)
				userName = request.getParameter(FRM_USER_NAME);
			userId = (Integer) session.getAttribute("userId");
			loginStatus = getAuthState();
		}
		
		public void handleRequest() {
			
			if (loginStatus == AuthState.NEW && userName != null) {
				registerSession(session, userName);
				loginStatus = AuthState.WAITING;
			}
				
			if (loginStatus == AuthState.NOT_LOGGED)
				unregisterSession(session);
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
		
		private AuthState getAuthState() {
			if (!authenticatedSessions.containsKey(sessionId)) return AuthState.NEW;
			if (userId == null) return AuthState.WAITING;
			if (userId == 0) return AuthState.NOT_LOGGED;
			return AuthState.LOGGED;
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
				
//		if (!"/servlet".equals(target))
//			response.sendRedirect("/");
		
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			
			LoginDataBuilder loginData = new LoginDataBuilder(request);
			loginData.handleRequest();
			
			response.getWriter().println(loginData.toJSON());
			
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
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
		authenticatedSessions.get(sessionId).setAttribute("userId", userId);
	}
	
	private void registerSession(HttpSession session, String userName) {
		
		String sessionId = session.getId();
		
		session.setAttribute("userName", userName);
		session.setAttribute("userId", null);
		authenticatedSessions.put(sessionId, session);
		
		Address to = ms.getAddressService().getAddress(AccountServer.class);
		ms.sendMessage(new MsgGetUserId(address, to, userName, sessionId));
	}
	
	private void unregisterSession(HttpSession session) {
		session.removeAttribute("userName");
		session.removeAttribute("userId");
		authenticatedSessions.remove(session.getId());
	}
}
