package mihnayan.divetojava.frontend;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.msgsystem.MsgToGM;

public class MsgStartGameSession extends MsgToGM {
	
	private int user1;
	private int user2;

	public MsgStartGameSession(Address from, Address to, int user1, int user2) {
		super(from, to);
		this.user1 = user1;
		this.user2 = user2;
	}

	@Override
	public void exec(GameMechanics gm) {
		gm.startGameSession(user1, user2);
	}

}
