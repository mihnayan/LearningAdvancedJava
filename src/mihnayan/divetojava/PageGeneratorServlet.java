package mihnayan.divetojava;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class PageGeneratorServlet extends HttpServlet {
	
	final String _htmlHeader = "<!DOCTYPE html><html>"
			+ "<head><meta charset=\"UTF-8\">"
			+ "<title>Page generator test</title></head>";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession(false);
		
		resp.setContentType("text/html;charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		
		if (session != null)
			resp.getWriter().println(getPage(session.getId()));
		else
			resp.getWriter().println(getHelloPage());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.sendRedirect("/servlet");
	}
	
	private String getHelloPage() {
		return _htmlHeader 
				+ "<body>"
				+ "<h2>Hello, new User!</h2>"
				+ "<p>You can post request for create session.</p>"
				+ "<form method=\"post\">"
				+ "<input type=\"submit\" value=\"Send\">"
				+ "</form>"
				+ "</body></html>";
	}

	private String getPage(String sessionId) {
		return _htmlHeader
				+ "<body>"
				+ "<h2>Hello, User!</h2>"
				+ "<p>Your session id: " + sessionId + "</p>"
				+ "<script>"
				+ "window.onload = function () {"
				+ "    setInterval('location.reload(true)', 1000);"
				+ "};"
				+ "</script>"
				+ "</body></html>";
	}

}
