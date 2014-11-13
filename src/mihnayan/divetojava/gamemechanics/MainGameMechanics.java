package mihnayan.divetojava.gamemechanics;

import java.util.Date;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.base.MessageService;

public class MainGameMechanics implements GameMechanics, Runnable {

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
	public void startGameSession(int user1, int user2) {
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
