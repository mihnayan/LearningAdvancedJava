package mihnayan.divetojava;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Frontend extends AbstractHandler implements Runnable {
	
	private static Logger log = Logger.getLogger(Frontend.class.getName());
	
	private ConcurrentHashMap<String, Integer> users;
	
	private AtomicInteger handleCount;
	private String error;
	
	private final String _htmlHeader = "<!DOCTYPE html><html>"
				+ "<head><meta charset=\"UTF-8\">";
	
	// field for user name in html form
	private final String FRM_USER_NAME = "user-name";
	
	public Frontend() {
		super();
		handleCount = new AtomicInteger();
		users = new ConcurrentHashMap<String, Integer>();
		
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
				Thread.sleep(5000);
			} catch(InterruptedException e) {
				return;
			}
			log.info("This page was handled " + handleCount + " times...");
		}
	}
	
	private String buildPage(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		if (session.isNew()) return welcomePage(session);
		
		String userName = (String) session.getAttribute("userName");
		if (userName == null) {
			userName = request.getParameter(FRM_USER_NAME);
			if (AccountServer.isValidUserName(userName)) {
				session.setAttribute("userName", userName);
				login(userName);
			} else {
				error = "Your name is not valid: " + userName;
				return welcomePage(session);				
			}
		}
		
		//TODO: allow the user to re-enter the registration data
		if (!users.containsKey(userName))
			return "You can not be authenticated!" + users.toString();
		
		if (users.get(userName) == 0)
			return idlePage("Wait for authorization, please...");
		
		return "You were successfully authenticated :-) And your name is <strong>" + userName
				+ "</strong>. And you userId is " + users.get(userName);
	}
	
	private String welcomePage(HttpSession session) {
		
		String page = _htmlHeader
				+ "<title>Advanced Java: Welcome</title></head>"
				+ "<body>"
				+ getError(true)
				+ "<h2>Welcom to Advanced Java course!</h2>"
				+ "<p>Your sessionId: " + session.getId() + "</p>"
				+ "<form method=\"post\">"
				+ "<label for=\"user-name-id\">Enter your name, please:</label>"
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
	
	private String getError(boolean clear) {
		if (error == "") return "";
		String err = error;
		if (clear) error = "";
		
		return "<div class=\"status\">" + err + "</div>";
	}
	
	private void login(String userName) {
		users.put(userName, 0);
		
		AccountServer accountServer = new AccountServer(users, userName);
		Thread accountServerThread = new Thread(accountServer);
		accountServerThread.start();
	}

}
