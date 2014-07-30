package mihnayan.divetojava;

public class AccountServer implements Runnable {
	
	public final static int NOT_LOGGED = 0;
	public final static int WAITING = 1;
	public final static int LOGGED = 2;
	
	/**
	 * Starts the process of user authentication
	 * @param userName a unique user name
	 */
	public void login(String userName) {
		
	}
	
	/**
	 * Checks user authentication state
	 * @param userName a unique user name
	 * @return AccountServer.NOT_LOGGED if the user has not been authenticated;<br /> 
	 * AccountServer.WAITING if not finished the process of authenticating the user;<br />
	 * AccountServer.LOGGED if the user has been authenticated
	 */
	public int isLogged(String userName) {
		return WAITING;
	}
	
	/**
	 * Returns 0 if user has not been authenticated. Otherwise returns the user Id.
	 * @param userName a unique user name
	 * @return user Id
	 */
	public int getUserId(String userName) {
		return 0;
	}
	
	public boolean isValidUserName(String userName) {
		return userName != null && userName.trim() != "";
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
