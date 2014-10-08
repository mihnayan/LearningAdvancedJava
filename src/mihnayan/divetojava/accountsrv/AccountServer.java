package mihnayan.divetojava.accountsrv;

import java.util.HashMap;

import mihnayan.divetojava.base.AccountService;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.MessageService;

/**
 * Class which authenticates a specific user for a specific session. Authentication is starts 
 * after invoking start() method of class.  After success end of process puts user ID in the 
 * sessions HashMap for specific session. If user has not been authenticated removes record 
 * from the map for the session.
 * @author Mikhail Mangushev
 *
 */
public class AccountServer implements Runnable, AccountService {
	
	private MessageService ms;
	private Address address;
	
	private static HashMap<String, Integer> userDb = new HashMap<String, Integer>();
	static {
		userDb = new HashMap<String, Integer>();
		userDb.put("Anakin Skywalker", 1);
		userDb.put("Yoda", 2);
		userDb.put("Mace Windu", 3);
		userDb.put("Obi-Wan Kenobi", 4);
	}
	
	public AccountServer(MessageService ms) {
		this.ms = ms;
		address = new Address();
		ms.getAddressService().setAddress(this);
	}
	
	/**
	 * Helper method to spell-check the user name
	 * @param userName The username
	 * @return True or False
	 */
	public static boolean isValidUserName(String userName) {
		return userName != null && userName.trim() != "";
	}
	
	@Override
	public Address getAddress() {
		return address;
	}
	
	@Override
	public MessageService getMessageService() {
		return ms;
	}

	public int getUserId(String userName) {
		if (userDb.containsKey(userName)) {
			return userDb.get(userName);
		} else {
			return 0;
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				// emulation of a long process			
				Thread.sleep(5000);
				ms.execForAbonent(this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}