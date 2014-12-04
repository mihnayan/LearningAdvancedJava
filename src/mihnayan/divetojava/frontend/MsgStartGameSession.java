package mihnayan.divetojava.frontend;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.base.UserId;
import mihnayan.divetojava.msgsystem.MsgToGM;

public class MsgStartGameSession extends MsgToGM {
	
	private UserId user1;
	private UserId user2;

	public MsgStartGameSession(Address from, Address to, UserId user1, UserId user2) {
		super(from, to);
		this.user1 = user1;
		this.user2 = user2;
	}

	@Override
	public void exec(GameMechanics gm) {
		gm.startGameSession(user1, user2);
	}

}
