package mihnayan.divetojava;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class DTJServer {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		
		Frontend frontend = new Frontend();
		Thread thread = new Thread(frontend);
		thread.start();
		
		ResourceHandler contentHandler = new ResourceHandler();
		contentHandler.setDirectoriesListed(false);
		contentHandler.setWelcomeFiles(new String[] { "index.html" });
		contentHandler.setResourceBase("content");
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addServlet(PageGeneratorServlet.class, "/servlet/*");
		
		HandlerList handlers = new HandlerList();
		handlers.addHandler(frontend);
		handlers.addHandler(contentHandler);
		handlers.addHandler(context);
		
		server.setHandler(handlers);
		
		server.start();
		server.join();
	}
}
