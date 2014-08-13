package mihnayan.divetojava.msgsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageSystem {
	
	private AddressService addressService = new AddressService();
	
	private Map<Address, ConcurrentLinkedQueue<Msg>> messages =
			new HashMap<Address, ConcurrentLinkedQueue<Msg>>();
	
	public void sendMessage(Msg message) {
//		Queue<Msg> messageQueue = messages.get(message.getTo());
//		messageQueue.add(message);
		messages.get(message.getTo()).add(message);
	}
	
	public void execForAbonent(Abonent abonent) {
		Queue<Msg> messageQueue = messages.get(abonent.getAddress());
		while(!messageQueue.isEmpty()) {
//			Msg message = messageQueue.poll();
//			message.exec(abonent);
			messageQueue.poll().exec(abonent);
		}
	}
	
	public AddressService getAddressService() {
		return addressService;
	}

}
