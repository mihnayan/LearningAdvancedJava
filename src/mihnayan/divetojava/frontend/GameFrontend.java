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
	private String error;
	
	// field for user name in html form
	private final String FRM_USER_NAME = "user-name";
	
	private final String[] targets = {"login", "welcome"};
	
	public GameFrontend(MessageService ms) {
		super();
		this.ms = ms;
		address = new Address();
		
		handleCount = new AtomicInteger();
		
		error = "";
	}

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
	 *    authentication server. Key 'userId' is still empty.
	 * 3. If the user is logged, all the keys of 'userLoginData' object are filled with the proper values.
	 * 4. If user authentication was not successful, then the 'userId' key has the value '0'.
	 *     
	 */
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
			
			String data = buildData(request);
			
			response.getWriter().println(data);
			
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
	
	private String buildData(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		
		if (session.isNew()) return welcomeData(sessionId);
		
		String userName = (String) session.getAttribute("userName");
		if (userName == null) {
			userName = request.getParameter(FRM_USER_NAME);
			
			if (userName == null)
				return welcomeData(sessionId);
			
			if (AccountServer.isValidUserName(userName)) {
				session.setAttribute("userName", userName);
				session.setAttribute("userId", 0);
				authenticatedSessions.put(sessionId, session);
				login(userName, sessionId);
			} else {
				error = "Your name is not valid: " + userName;
				return welcomeData(sessionId);
			}
		}
		
		if (!authenticatedSessions.containsKey(sessionId)) {
			session.removeAttribute("userName");
			return notAuthenticatedPage(userName, sessionId);
		}
		
		Integer userId = (Integer) authenticatedSessions.get(sessionId).getAttribute("userId");
		
		if (userId == 0)
			return idlePage(userName, sessionId);
		
		String data = "{"
				+ "\"userLoginData\": {"
				+ "\"sessionId\": \"" + sessionId + "\","
				+ "\"userName\": \"" + userName + "\","
				+ "\"userId\": \"" + userId + "\""
				+ "},"
				+ "\"requestStatus\": {"
				+ "\"type\": \"success\","
				+ "\"text\": \"" + getError(true) + "\""
				+ "}"
				+ "}";
		
		return data;
	}
	
	private String welcomeData(String sessionId) {
		
		String data = "{"
				+ "\"userLoginData\": {"
				+ "\"sessionId\": \"" + sessionId + "\","
				+ "\"userName\": \"\","
				+ "\"userId\": \"\""
				+ "},"
				+ "\"requestStatus\": {"
				+ "\"type\": \"success\","
				+ "\"text\": \"" + getError(true) + "\""
				+ "}"
				+ "}";
		
		return data;
	}
	
	private String idlePage(String userName, String sessionId) {
		String data = "{"
				+ "\"userLoginData\": {"
				+ "\"sessionId\": \"" + sessionId + "\","
				+ "\"userName\": \"" + userName + "\","
				+ "\"userId\": \"\""
				+ "},"
				+ "\"requestStatus\": {"
				+ "\"type\": \"success\","
				+ "\"text\": \"" + getError(true) + "\""
				+ "}"
				+ "}";
		
		return data;
	}
	
	private String notAuthenticatedPage(String userName, String sessionId) {
		String data = "{"
				+ "\"userLoginData\": {"
				+ "\"sessionId\": \"" + sessionId + "\","
				+ "\"userName\": \"" + userName + "\","
				+ "\"userId\": \"0\""
				+ "},"
				+ "\"requestStatus\": {"
				+ "\"type\": \"success\","
				+ "\"text\": \"" + getError(true) + "\""
				+ "}"
				+ "}";
		
		return data;
	}
	
	private String getError(boolean clear) {
		if (error == "") return "";
		String err = error;
		if (clear) error = "";
		
		return "<div class=\"status\">" + err + "</div>";
	}
	
	private void login(String userName, String sessionId) {
		Address to = ms.getAddressService().getAddress(AccountServer.class);
		ms.sendMessage(new MsgGetUserId(address, to, userName, sessionId));
	}
	
	private String getLoginData() {
		return "{}";
	}
}
