package mihnayan.divetojava.frontend;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.msgsystem.MsgToFrontend;

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