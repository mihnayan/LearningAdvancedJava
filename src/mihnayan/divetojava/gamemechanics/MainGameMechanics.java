package mihnayan.divetojava.gamemechanics;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.UserId;
import mihnayan.divetojava.resourcesystem.GameSessionResource;
import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceNotExistException;

public class MainGameMechanics implements GameMechanics, Runnable {

	private static Logger log = Logger.getLogger(MainGameMechanics.class.getName());
	
	private MessageService ms;
	private Address address;
	private GameSession gs;
	private Frontend frontend;
	
	public MainGameMechanics(MessageService ms, Frontend frontend) {
		this.ms = ms;
		this.frontend = frontend;
		address = new Address();
		ms.getAddressService().setAddress(this);
	}
	
	public static int getRequiredPlayerCount() {
		try {
			GameSessionResource gsr = 
					(GameSessionResource) ResourceFactory.instance().get(GameSessionResource.class);
			return gsr.getRequiredPlayers();
			
		} catch (ResourceNotExistException e) {
			log.log(Level.WARNING, "Can't start game! Cause: not found resource " 
					+ GameSessionResource.class.getName() + "!");
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public void run() {
		while (true) {
			try {		
				ms.execForAbonent(this);
				sendGameData();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public MessageService getMessageService() {
		return ms;
	}

	@Override
	public Address getAddress() {
		return address;
	}
	
	@Override
	public void startGameSession(UserId user1, UserId user2) {
		if (gs == null) {
			gs = new GameSession(user1, user2);
		}
	}
	
	private void sendGameData() {
		if (gs == null) return;
		GameDataImpl gd = new GameDataImpl();
		gd.setElapsedTime((new Date()).getTime() - gs.getStartTime());
		MsgSetGameData msg = new MsgSetGameData(address, frontend.getAddress(), gd);
		ms.sendMessage(msg);
	}

}
