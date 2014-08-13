package mihnayan.divetojava;

import mihnayan.divetojava.msgsystem.MessageSystem;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;

public class DTJServer {

	public static void main(String[] args) throws Exception {
		
		MessageSystem ms = new MessageSystem();
		
		Server server = new Server(8080);
		
		Frontend frontend = new Frontend(ms);
		AccountServer accountServer = new AccountServer(ms);
		
		(new Thread(frontend)).start();
		(new Thread(accountServer)).start();
		
		ResourceHandler contentHandler = new ResourceHandler();
		contentHandler.setDirectoriesListed(false);
		contentHandler.setWelcomeFiles(new String[] { "index.html" });
		contentHandler.setResourceBase("content");
		
		HandlerList handlers = new HandlerList();
		handlers.addHandler(new SessionHandler());
		handlers.addHandler(frontend);
		
		server.setHandler(handlers);
		
		server.start();
		server.join();
	}
}
