package mihnayan.divetojava.msgs;

import mihnayan.divetojava.msgsystem.Address;

public class MsgGetUserId extends MsgToAccountServer {

	private String userName;

	public MsgGetUserId(Address from, Address to, String userName) {
		super(from, to);
		this.userName = userName;
	}
	
	
}
