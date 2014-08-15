package mihnayan.divetojava;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import mihnayan.divetojava.msgsystem.Abonent;
import mihnayan.divetojava.msgsystem.Address;
import mihnayan.divetojava.msgsystem.MessageSystem;

/**
 * Class which authenticates a specific user for a specific session. Authentication is starts 
 * after invoking start() method of class.  After success end of process puts user ID in the 
 * sessions HashMap for specific session. If user has not been authenticated removes record 
 * from the map for the session.
 * @author Mikhail Mangushev
 *
 */
public class AccountServer implements Runnable, Abonent {
	
	public final static int WAITING = 0;
	
	private MessageSystem ms;
	private Address address;
	
	private String userName;
	private String sessionId;
	private ConcurrentHashMap<String, Integer> sessions;
	
	private static HashMap<String, Integer> userDb = new HashMap<String, Integer>();
	static {
		userDb = new HashMap<String, Integer>();
		userDb.put("Anakin Skywalker", 1);
		userDb.put("Yoda", 2);
		userDb.put("Mace Windu", 3);
		userDb.put("Obi-Wan Kenobi", 4);
	}
	
	public AccountServer(MessageSystem ms) {
		this.ms = ms;
		address = new Address();
	}
	
	/**
	 * Helper method to spell-check the user name
	 * @param userName The username
	 * @return True or False
	 */
	public static boolean isValidUserName(String userName) {
		return userName != null && userName.trim() != "";
	}
	
	/**
	 * Creates a new class to authenticate a specific user for a specific session.
	 * @param userName the user name that will be checked
	 * @param sessionId the session ID for which the authenticated user
	 * @param sessions Maps session ID to user ID
	 */
	public AccountServer(String userName, String sessionId, 
			ConcurrentHashMap<String, Integer> sessions) {
		super();
		
		this.userName = userName;
		this.sessionId = sessionId;
		this.sessions = sessions;
	}
	
	@Override
	public Address getAddress() {
		return address;
	}
	
	public MessageSystem getMessageSystem() {
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
		// emulation of a long process
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (userDb.containsKey(userName)) {
			sessions.put(sessionId, userDb.get(userName));
		} else {
			sessions.remove(sessionId);
		}
	}
}
