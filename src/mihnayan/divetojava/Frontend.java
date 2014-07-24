package mihnayan.divetojava;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Frontend extends AbstractHandler implements Runnable {
	
	private static Logger log = Logger.getLogger(Frontend.class.getName());
	
	private AtomicInteger handleCount;
	
	public Frontend() {
		super();
		handleCount = new AtomicInteger();
	}

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		handleCount.incrementAndGet();
		
		String page = "<!DOCTYPE html><html>"
				+ "<head><meta charset=\"UTF-8\">"
				+ "<title>Page generator test</title></head>"
				+ "<body>"
				+ "<h2>Hello, from Frontend!</h2>"
				+ "<script>"
				+ "window.onload = function () {"
				+ "    setInterval('location.reload(true)', 1000);"
				+ "};"
				+ "</script>"
				+ "</body></html>";
		
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		
		response.getWriter().println(page);
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

}
