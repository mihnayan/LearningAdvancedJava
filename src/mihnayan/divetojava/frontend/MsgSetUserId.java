package mihnayan.divetojava.frontend;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.UserId;
import mihnayan.divetojava.msgsystem.MsgToFrontend;

public class MsgSetUserId extends MsgToFrontend {
	
	final private String sessionId;
	final private UserId userId;
	final private String userName;

	public MsgSetUserId(Address from, Address to, String sessionId, UserId userId, String userName) {
		super(from, to);
		this.sessionId = sessionId;
		this.userId = userId;
		this.userName = userName;
	}

	@Override
	public void exec(Frontend frontend) {
		frontend.setUser(sessionId, userId, userName);
	}

}