package mihnayan.divetojava;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHandler;

public class DTJServer {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		
		ResourceHandler contentHandler = new ResourceHandler();
		contentHandler.setDirectoriesListed(false);
		contentHandler.setWelcomeFiles(new String[] { "index.html" });
		contentHandler.setResourceBase("content");
		
//		ServletHandler servletHandler = new ServletHandler();
//		servletHandler.addServletWithMapping(PageGeneratorServlet.class, "/servlet/*");
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addServlet(PageGeneratorServlet.class, "/servlet/*");
		
		HandlerList handlers = new HandlerList();
		handlers.addHandler(contentHandler);
//		handlers.addHandler(servletHandler);
		handlers.addHandler(context);
		
		server.setHandler(handlers);
		
		server.start();
		server.join();
	}
}
