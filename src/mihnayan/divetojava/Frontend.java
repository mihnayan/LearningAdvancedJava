package mihnayan.divetojava;

import java.io.IOException;
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
	
	private AtomicInteger handleCount;
	private AccountServer accountServer;
	private String error;
	
	private final String _htmlHeader = "<!DOCTYPE html><html>"
				+ "<head><meta charset=\"UTF-8\">";
	
	// field for user name in html form
	private final String FRM_USER_NAME = "user-name";
	
	public Frontend() {
		super();
		handleCount = new AtomicInteger();
		
		accountServer = new AccountServer();
		Thread accountServerThread = new Thread(accountServer);
		accountServerThread.start();
		
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
			if (!accountServer.isValidUserName(userName)) {
				error = "Your name is not valid: " + userName;
				return welcomePage(session);
			} else {
				session.setAttribute("userName", userName);
				accountServer.login(userName);
			}
		}
		
		if (accountServer.isLogged(userName) == AccountServer.WAITING)
			return idlePage("Wait for authorization, please...");
		
		return "You were successfully authenticated :-) And your name is " + userName;
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
				+ "    setInterval('location.reload(true)', 1000);"
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

}
