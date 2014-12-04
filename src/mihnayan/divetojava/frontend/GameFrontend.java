package mihnayan.divetojava.frontend;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.UserId;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class GameFrontend extends AbstractHandler implements Runnable, Frontend {
	
	private static Logger log = Logger.getLogger(GameFrontend.class.getName());
	
	private static Map<String, Class<? extends RequestHandler>> targets = 
			new HashMap<String, Class<? extends RequestHandler>>();
	
	static {
		targets.put("/login", LoginRequestHandler.class);
		targets.put("/gameData", GameDataRequestHandler.class);
	}
	
	private MessageService ms;
	private Address address;
	private AtomicInteger handleCount;
	
	public GameFrontend(MessageService ms) {
		super();
		this.ms = ms;
		address = new Address();
		
		handleCount = new AtomicInteger();
	}

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		baseRequest.setHandled(true);
		
		handleCount.incrementAndGet();
		
		if (target == null || !targets.containsKey(target)) return;
		Class<? extends RequestHandler> cl = targets.get(target);
		try {
			Constructor<? extends RequestHandler> con = 
					cl.getConstructor(HttpServletRequest.class, Abonent.class);
			RequestHandler requestHandler = con.newInstance(new Object[] {request, this});
			requestHandler.handleRequest(response);
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
				Thread.sleep(100);
			} catch(InterruptedException e) {
				return;
			}
//			log.info("This page was handled " + handleCount + " times...");
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
	public void setUser(String sessionId, UserId userId, String userName) {
		LoginRequestHandler.setUser(sessionId, userId, userName);
	}
	
	@Override
	public void setGameData(GameData gameData) {
		GameDataRequestHandler.gameData = gameData;
	}
}
