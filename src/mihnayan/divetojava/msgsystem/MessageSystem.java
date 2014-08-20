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
		getQueue(message.getTo()).add(message);
	}
	
	public void execForAbonent(Abonent abonent) {
		Queue<Msg> messageQueue = getQueue(abonent.getAddress());
		while(!messageQueue.isEmpty()) {
			messageQueue.poll().exec(abonent);
		}
	}
	
	public AddressService getAddressService() {
		return addressService;
	}
	
	private Queue<Msg> getQueue(Address address) {
		if (!messages.containsKey(address))
			messages.put(address, new ConcurrentLinkedQueue<Msg>());
		return messages.get(address);
	}

}
