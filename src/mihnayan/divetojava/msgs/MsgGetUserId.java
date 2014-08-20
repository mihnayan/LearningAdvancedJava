package mihnayan.divetojava.msgs;

import mihnayan.divetojava.AccountServer;
import mihnayan.divetojava.msgsystem.Address;
import mihnayan.divetojava.msgsystem.Msg;

public class MsgGetUserId extends MsgToAccountServer {

	final private String userName;
	final private String sessionId;

	public MsgGetUserId(Address from, Address to, String userName, String sessionId) {
		super(from, to);
		this.userName = userName;
		this.sessionId = sessionId;
	}

	@Override
	public void exec(AccountServer accountServer) {
		int userId = accountServer.getUserId(userName);
		Msg message = new MsgSetUserId(to, from, sessionId, userId);
		accountServer.getMessageSystem().sendMessage(message);
	}
	
	
}
