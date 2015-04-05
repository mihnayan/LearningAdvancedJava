package mihnayan.divetojava.frontend;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.base.UserSession;
import mihnayan.divetojava.resourcesystem.GameSessionResource;
import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceNotExistException;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * Main class that handles all client requests.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public class GameFrontend extends AbstractHandler implements Runnable, Frontend {

    private static Logger log = Logger.getLogger(GameFrontend.class.getName());
    
    private static final int SLEEP_TIME = 100;

    private static Map<String, Class<? extends AbstractRequestHandler>> targets =
            new HashMap<String, Class<? extends AbstractRequestHandler>>();

    static {
        targets.put("/login", LoginRequestHandler.class);
        targets.put("/gameData", GameDataRequestHandler.class);
    }

    private MessageService ms;
    private Address address;
    private AtomicInteger handleCount;
    
    private int minPlayersCount;

    /**
     * @param ms Real message system for interaction with other components.
     */
    public GameFrontend(MessageService ms) {
        super();
        this.ms = ms;
        address = new Address();

        handleCount = new AtomicInteger();
        
        //TODO: ugly
        try {
            this.minPlayersCount =((GameSessionResource) ResourceFactory
                    .instance().get(GameSessionResource.class)).getMinPlayers();
        } catch (ResourceNotExistException e) {
            this.minPlayersCount = Integer.MAX_VALUE;
            log.log(Level.WARNING,
                    "Can't start game! Cause: not found resource "
                            + GameSessionResource.class.getName() + "!");
        }
    }

    @Override
    public void handle(String target, Request baseRequest,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        baseRequest.setHandled(true);

        handleCount.incrementAndGet();

        if (target == null || !targets.containsKey(target)) {
            return;
        }
        Class<? extends AbstractRequestHandler> cl = targets.get(target);
        try {
            Constructor<? extends AbstractRequestHandler> con =
                    cl.getConstructor(HttpServletRequest.class, Frontend.class);
            AbstractRequestHandler requestHandler = con.newInstance(new Object[] {request, this});
            requestHandler.buildResponse(response);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                ms.execForAbonent(this);
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                return;
            }
//            log.info("This page was handled " + handleCount + " times...");
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageService getMessageService() {
        return ms;
    }

    @Override
    public void setAuthenticatedUserSession(UserSession userSession) {
        LoginRequestHandler.setUser(userSession);
    }

    @Override
    public void setGameData(GameData gameData, User forUser) {
        Iterator<UserSession> userSessions = 
                LoginRequestHandler.authenticatedSessions.values().iterator();
        while (userSessions.hasNext()) {
            UserSession session = userSessions.next();
            if (session.getUser().equals(forUser)) {
                session.setCurrentGameData(gameData);
            }
        }
    }

    @Override
    public int getMinPlayersCount() {
        return minPlayersCount;
    }
    
}
