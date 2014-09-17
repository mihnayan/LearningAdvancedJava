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
	
	private final String _htmlHeader = "<!DOCTYPE html><html>"
				+ "<head><meta charset=\"UTF-8\">";
	
	// field for user name in html form
	private final String FRM_USER_NAME = "user-name";
	
	public GameFrontend(MessageService ms) {
		super();
		this.ms = ms;
		address = new Address();
		
		handleCount = new AtomicInteger();
		
		error = "";
	}

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		baseRequest.setHandled(true);
		
		handleCount.incrementAndGet();
		
		try {
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			
			response.getWriter().println(buildPage(request));
			
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
	
	private String buildPage(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		
		if (session.isNew()) return welcomePage(sessionId);
		
		String userName = (String) session.getAttribute("userName");
		if (userName == null) {
			userName = request.getParameter(FRM_USER_NAME);
			
			if (userName == null)
				return welcomePage(sessionId);
			
			if (AccountServer.isValidUserName(userName)) {
				session.setAttribute("userName", userName);
				session.setAttribute("userId", 0);
				authenticatedSessions.put(sessionId, session);
				login(userName, sessionId);
			} else {
				error = "Your name is not valid: " + userName;
				return welcomePage(sessionId);				
			}
		}
		
		if (!authenticatedSessions.containsKey(sessionId)) {
			session.removeAttribute("userName");
			return notAuthenticatedPage(userName);
		}
		
		Integer userId = (Integer) authenticatedSessions.get(sessionId).getAttribute("userId");
		
		if (userId == 0)
			return idlePage("Wait for authorization, please...");
		
		return "You were successfully authenticated :-) And your name is <strong>" + userName
				+ "</strong>. And you userId is " + userId;
	}
	
	private String welcomePage(String sessionId) {
		
		String page = _htmlHeader
				+ "<title>Advanced Java: Welcome</title></head>"
				+ "<body>"
				+ getError(true)
				+ "<h2>Welcom to Advanced Java course!</h2>"
				+ "<p>Your sessionId: " + sessionId + "</p>"
				+ "<form method=\"post\">"
				+ "<label for=\"user-name-id\">Enter your name, please: </label>"
				+ "<input type=\"text\" id=\"user-name-id\" name=\"user-name\">"
				+ "<input type=\"submit\">"
				+ "</form>"
				+ "</body></html>";
		
		return page;
	}
	
	private String idlePage(String message) {
		return _htmlHeader
				+ "<title>Advanced Java: idle...</title></head>"
				+ "<body>\n"
				+ "<p>" + message + "</p>"
				+ "<script>"
				+ "window.onload = function () {"
				+ "    setInterval('location.replace(location.href)', 1000);"
				+ "};"
				+ "</script>"
				+ "</body></html>";
	}
	
	private String notAuthenticatedPage(String userName) {
		return _htmlHeader
				+ "<title>Advanced Java: not authenticated</title></head>"
				+ "<body>\n"
				+ "<p>Sorry, you can not be authenticated with name <strong>" + userName 
				+ "</strong></p>"
				+ "<p>You can <a href=\"http://localhost:8080\">try to sign in again</a></p>"
				+ "</body></html>";
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
}
