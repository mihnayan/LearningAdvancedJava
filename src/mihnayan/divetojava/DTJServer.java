package mihnayan.divetojava;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.AbstractSessionManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class DTJServer {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		
		Frontend frontend = new Frontend();
		Thread thread = new Thread(frontend);
		thread.start();
		
		AccountServer accountServer = new AccountServer();
		Thread accountServerThread = new Thread(accountServer);
		accountServerThread.start();
		
		ResourceHandler contentHandler = new ResourceHandler();
		contentHandler.setDirectoriesListed(false);
		contentHandler.setWelcomeFiles(new String[] { "index.html" });
		contentHandler.setResourceBase("content");
		
//		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//		context.setSessionHandler(new SessionHandler(new HashSessionManager()));
//		context.setContextPath("/");
//		context.addServlet(PageGeneratorServlet.class, "/servlet/*");
//		context.setHandler(frontend);
		
		HandlerList handlers = new HandlerList();
//		handlers.addHandler(contentHandler);
//		handlers.addHandler(context);
		handlers.addHandler(new SessionHandler());
		handlers.addHandler(frontend);
		
		server.setHandler(handlers);
		
		server.start();
		server.join();
	}
}
