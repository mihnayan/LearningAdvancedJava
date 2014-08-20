package mihnayan.divetojava.msgs;

import mihnayan.divetojava.Frontend;
import mihnayan.divetojava.msgsystem.Address;

public class MsgSetUserId extends MsgToFrontend {
	
	final private String sessionId;
	final private int userId;

	public MsgSetUserId(Address from, Address to, String sessionId, int userId) {
		super(from, to);
		this.sessionId = sessionId;
		this.userId = userId;
	}

	@Override
	public void exec(Frontend frontend) {
		frontend.setUserId(sessionId, userId);
	}
	
	

}