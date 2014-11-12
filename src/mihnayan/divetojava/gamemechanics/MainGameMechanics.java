package mihnayan.divetojava.gamemechanics;

import java.util.Date;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.MessageService;

public class MainGameMechanics implements Abonent, Runnable {

	private MessageService ms;
	private Address address;
	private GameSession gs;
	private Abonent gameDataSubscriber;
	
	public MainGameMechanics(MessageService ms) {
		this.ms = ms;
		address = new Address();
		ms.getAddressService().setAddress(this);
	}

	@Override
	public void run() {
		while (true) {
			try {		
				ms.execForAbonent(this);
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
	
	/**
	 * Starts game session (start game). 
	 * If the game session is already created, then nothing happens
	 * @param user1 user id of first opponent
	 * @param user2 user id of second opponent
	 * @param gameDataSubscriber the subscriber to which to send game data
	 */
	public void startGameSession(int user1, int user2, Abonent gameDataSubscriber) {
		if (gs == null) {
			gs = new GameSession(user1, user2);
			this.gameDataSubscriber = gameDataSubscriber;
		}
	}
	
	private void sendGameData() {
		if (gs == null) return;
		GameData gd = new GameData();
		gd.setElapsedTime((new Date()).getTime() - gs.getStartTime());
	}

}
