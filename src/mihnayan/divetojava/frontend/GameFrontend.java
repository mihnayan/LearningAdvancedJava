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
	 *     userLoginData: {
	 *         sessionId: 0,
	 *         userName: "",
	 *         userId: ""
	 *     },
	 *     
	 *     requestStatus: {
	 *         type: ["error", "success", "warning"],
	 *         text: "some text"
	 *     }
	 * }
	 * 
	 * 1. If the request is a new session, then fill 'sessionId' key only in response. 
	 * 	  Keys 'userName' and 'userId'are empty.
	 * 2. If the user has sent a request for authentication by sending the user name, the response 
	 *    filled only keys 'sessionId' and 'userName' while waiting for the response from the 
	 *    authentication server. The 'userId' key value becomes equal to '0' at this time.
	 * 3. If the user is logged, all the keys of 'userLoginData' object are filled with the proper values.
	 * 4. If user authentication was not successful, then the 'userId' key has the empty string value.
	 *     
	 */
	private class LoginDataBuilder {
		private String sessionId;
		private String userName = "";
		private String userId = "";
		private String requestStatus = "";
		private String requestStatusText = "";
		
		public void handleRequest(HttpServletRequest request) {
			HttpSession session = request.getSession();
			sessionId = session.getId();
			
			if (session.isNew()) return;
			
			userName = (String) session.getAttribute("userName");
			if (userName == null) {
				userName = request.getParameter(FRM_USER_NAME);
				
				if (userName == null) return;
				
				if (AccountServer.isValidUserName(userName)) {
					session.setAttribute("userName", userName);
					session.setAttribute("userId", 0);
					authenticatedSessions.put(sessionId, session);
					
					Address to = ms.getAddressService().getAddress(AccountServer.class);
					ms.sendMessage(new MsgGetUserId(address, to, userName, sessionId));
				} else {
					requestStatus = "error";
					requestStatusText = "Your name is not valid: " + userName;
					return;
				}
			}
			
			if (!authenticatedSessions.containsKey(sessionId)) {
				session.removeAttribute("userName");
				return;
			}
			
			userId = ((Integer) 
					authenticatedSessions.get(sessionId).getAttribute("userId")).toString();
		}
		
		@Override
		public String toString() {
			return "{\"userLoginData\": {"
					+ "\"sessionId\": \"" + sessionId + "\","
					+ "\"userName\": \"" + (userName == null ? "" : userName) + "\","
					+ "\"userId\": \"" + userId + "\""
					+ "},"
					+ "\"requestStatus\": {"
					+ "\"type\": \"" + requestStatus + "\","
					+ "\"text\": \"" + requestStatusText + "\""
					+ "}"
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
				
//		if (!"/servlet".equals(target))
//			response.sendRedirect("/");
		
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			
			LoginDataBuilder loginData = new LoginDataBuilder();
			loginData.handleRequest(request);
			
			response.getWriter().println(loginData.toString());
			
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
		if (userId != 0)
			authenticatedSessions.get(sessionId).setAttribute("userId", userId);
		else
			authenticatedSessions.remove(sessionId);
	}
}
