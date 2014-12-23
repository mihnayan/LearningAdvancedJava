package mihnayan.divetojava;

import mihnayan.divetojava.accountsrv.AccountServer;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.frontend.GameFrontend;
import mihnayan.divetojava.gamemechanics.MainGameMechanics;
import mihnayan.divetojava.msgsystem.MessageSystem;
import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceLoadingException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;

public class DTJServer {

	public static void main(String[] args) throws Exception {
		
		ResourceFactory resourceFactory = ResourceFactory.instance();
		try {
			resourceFactory.loadResources();
		} catch(ResourceLoadingException e) {
			System.out.println("Can't run server: resources loading error!");
			throw new Exception(e);
		}
		
		MessageService ms = new MessageSystem();
		
		Server server = new Server(8080);
		
		GameFrontend frontend = new GameFrontend(ms);
		AccountServer accountServer = new AccountServer(ms);
		MainGameMechanics gameMechanics = new MainGameMechanics(ms, frontend);
		
		(new Thread(frontend)).start();
		(new Thread(accountServer)).start();
		(new Thread(gameMechanics)).start();
		
		ResourceHandler contentHandler = new ResourceHandler();
		contentHandler.setDirectoriesListed(false);
		contentHandler.setWelcomeFiles(new String[] { "index.html" });
		contentHandler.setResourceBase("content");
		
		HandlerList handlers = new HandlerList();
		handlers.addHandler(contentHandler);
		handlers.addHandler(new SessionHandler());
		handlers.addHandler(frontend);
		
		server.setHandler(handlers);
		
		server.start();
		server.join();
	}
}
