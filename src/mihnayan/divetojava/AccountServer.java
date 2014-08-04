package mihnayan.divetojava;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

//TODO: Find and return useId by username
//TODO: userDb implemented by HashMap

//TODO: pass HashMap with session-userId from Frontend. AccountServer must to change the HashMap
//after finding userId
public class AccountServer implements Runnable {
	
	public final static int NOT_LOGGED = -1;
	public final static int WAITING = 0;
	public final static int LOGGED = 2;
	
	private ConcurrentHashMap<String, Integer> users;
	private String userName;
	
	private HashMap<String, Integer> userDb;
	
	public AccountServer(ConcurrentHashMap<String, Integer> users, String userName) {
		super();
		
		this.users = users;
		this.userName = userName;
		
		userDb = new HashMap<String, Integer>();
		userDb.put("Anakin Skywalker", 1);
		userDb.put("Yoda", 2);
		userDb.put("Mace Windu", 3);
		userDb.put("Obi-Wan Kenobi", 4);
	}
	
	/**
	 * Starts the process of user authentication
	 * @param userName a unique user name
	 */
	public void login(String userName) {
		users.put(userName, 0);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		users.replace(userName, 1);
	}
	
	/**
	 * Checks user authentication state
	 * @param userName a unique user name
	 * @return AccountServer.NOT_LOGGED if the user has not been authenticated;<br /> 
	 * AccountServer.WAITING if not finished the process of authenticating the user;<br />
	 * AccountServer.LOGGED if the user has been authenticated
	 */
//	public int loginStatus(String userName) {
//		if (registeredUsers.contains(userName))
//			return LOGGED;
//		
//		return WAITING;
//	}
//	
	/**
	 * Returns 0 if user has not been authenticated. Otherwise returns the user Id.
	 * @param userName a unique user name
	 * @return user Id
	 */
//	public int getUserId(String userName) {
//		if (loginStatus(userName) == LOGGED)
//			return registeredUsers.get(userName);
//		else
//			return 0;
//	}
	
	public static boolean isValidUserName(String userName) {
		return userName != null && userName.trim() != "";
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
			users.put(userName, userDb.get(userName));
		} else {
			users.remove(userName);
		}
	}
}
