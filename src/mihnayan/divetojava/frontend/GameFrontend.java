package mihnayan.divetojava.frontend;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.base.MessageService;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class GameFrontend extends AbstractHandler implements Runnable, Frontend {
	
	private static Logger log = Logger.getLogger(GameFrontend.class.getName());
	
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
		
		if ("/login".equals(target)) {			
			LoginRequestHandler loginHandler = new LoginRequestHandler(request, this);
			loginHandler.handleRequest(response);
		}
		
		if ("/gameData".equals(target)) {
			GameDataRequestHandler gameHandler = new GameDataRequestHandler(request, this);
			gameHandler.handleRequest(response);
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
	public void setUser(String sessionId, int userId, String userName) {
		LoginRequestHandler.setUser(sessionId, userId, userName);
	}
	
	@Override
	public void setGameData(GameData gameData) {
		GameDataRequestHandler.gameData = gameData;
	}
}
