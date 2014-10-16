package mihnayan.divetojava.gamemechanics;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.MessageService;

public class MainGameMechanics implements Abonent, Runnable {

	private MessageService ms;
	private Address address;
	
	public MainGameMechanics(MessageService ms) {
		this.ms = ms;
		address = new Address();
		ms.getAddressService().setAddress(this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public MessageService getMessageService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

}
