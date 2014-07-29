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
	
	private final String _htmlHeader = "<!DOCTYPE html><html>"
				+ "<head><meta charset=\"UTF-8\">";
	
	// field for user name in html form
	private final String FRM_USER_NAME = "user-name";
	
	public Frontend() {
		super();
		handleCount = new AtomicInteger();
	}

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		baseRequest.setHandled(true);
		
		handleCount.incrementAndGet();
		
		HttpSession session;
		String userName = request.getParameter(FRM_USER_NAME);
		
		try {
			session = request.getSession();
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			
			response.getWriter().println(buildPage(session, userName));
			
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
	
	private String buildPage(HttpSession session, String userName) {
		if (session.isNew()) return welcomePage(session);
		
		return "Your session is not new :-) And your name is " + userName;
	}
	
	private String welcomePage(HttpSession session) {
		
		String page = _htmlHeader
				+ "<title>Advanced Java: Welcome</title></head>"
				+ "<body>"
				+ "<h2>Welcom to Advanced Java course!</h2>"
				+ "<p>Your sessionId: " + session.getId() + "</p>"
				+ "<form method=\"post\">"
				+ "<label for=\"user-name-id\">Enter your name, please:</label>"
				+ "<input type=\"text\" id=\"user-name-id\" name=\"user-name\">"
				+ "<input type=\"submit\">"
				+ "</form>"
//				+ "<script>"
//				+ "window.onload = function () {"
//				+ "    setInterval('location.reload(true)', 1000);"
//				+ "};"
//				+ "</script>"
				+ "</body></html>";
		
		return page;
		
	}

}
