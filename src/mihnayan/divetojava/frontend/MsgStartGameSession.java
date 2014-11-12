package mihnayan.divetojava.frontend;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.msgsystem.MsgToGM;

public class MsgStartGameSession extends MsgToGM {

	public MsgStartGameSession(Address from, Address to, int playerId, int opponentId) {
		super(from, to);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exec(GameMechanics gm) {
		// TODO Auto-generated method stub

	}

}
