package mihnayan.divetojava;

public class AccountServer implements Runnable {

	/**
	 * Returns 0 if userName cannot be authenticated. Otherwise returns the user Id.
	 * @param userName
	 * @return user Id
	 */
	public int login(String userName) {
		
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
