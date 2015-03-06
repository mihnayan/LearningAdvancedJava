package mihnayan.divetojava;

import mihnayan.divetojava.accountsrv.AccountServer;
import mihnayan.divetojava.base.CreateServiceException;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.dbservice.MainDatabaseService;
import mihnayan.divetojava.frontend.GameFrontend;
import mihnayan.divetojava.gamemechanics.MainGameMechanics;
import mihnayan.divetojava.msgsystem.MessageSystem;
import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceLoadingException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;

/**
 * The main class that starts the application.
 * @author Mikhail Mangushev (Mihnayan)
 */
final class DTJServer {

    private static final int SERVER_PORT = 8080;

    /**
     * The main method that runs application.
     * @param args No parameters are required.
     * @throws Exception An exception may occur if there was an error loading application resources.
     */
    public static void main(String[] args) throws Exception {

        try {
            ResourceFactory.instance().loadResources();
        } catch (ResourceLoadingException e) {
            System.out.println("Can't run server: resources loading error!");
            throw new Exception(e);
        }

        MessageService ms = new MessageSystem();

        MainDatabaseService dbService = null;
        try {
            dbService = new MainDatabaseService(ms);
        } catch (CreateServiceException e) {
            System.out.println("Can't run server: "
                    + "error occurred when creating the database service");
            throw new Exception(e);
        }

        Server server = new Server(SERVER_PORT);

        GameFrontend frontend = new GameFrontend(ms);
        AccountServer accountServer = new AccountServer(ms);
        MainGameMechanics gameMechanics = new MainGameMechanics(ms, frontend);

        (new Thread(frontend)).start();
        (new Thread(accountServer)).start();
        (new Thread(gameMechanics)).start();
        (new Thread(dbService)).start();

        ResourceHandler contentHandler = new ResourceHandler();
        contentHandler.setDirectoriesListed(false);
        contentHandler.setWelcomeFiles(new String[]{"index.html"});
        contentHandler.setResourceBase("content");

        HandlerList handlers = new HandlerList();
        handlers.addHandler(contentHandler);
        handlers.addHandler(new SessionHandler());
        handlers.addHandler(frontend);

        server.setHandler(handlers);

        server.start();
        server.join();
    }

    private DTJServer() { }
}
