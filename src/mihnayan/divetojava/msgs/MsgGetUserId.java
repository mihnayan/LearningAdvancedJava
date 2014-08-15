package mihnayan.divetojava.msgs;

import mihnayan.divetojava.AccountServer;
import mihnayan.divetojava.msgsystem.Address;

public class MsgGetUserId extends MsgToAccountServer {

	private String userName;
	private String sessionId;

	public MsgGetUserId(Address from, Address to, String userName, String sessionId) {
		super(from, to);
		this.userName = userName;
		this.sessionId = sessionId;
	}

	public void exec(AccountServer accountServer) {
		int id = accountServer.getUserId(userName);
		accountServer.getMessageSystem().sendMessage(null);
	}
	
	
}
