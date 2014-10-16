package mihnayan.divetojava.msgsystem;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.base.Msg;

public abstract class MsgToGM extends Msg {

	public MsgToGM(Address from, Address to) {
		super(from, to);
	}

	@Override
	public void exec(Abonent abonent) {
		if (abonent instanceof GameMechanics) {
			exec((GameMechanics) abonent);
		} else {
			throw new WrongAbonentClassException(
					"Wrong abonent class: " + abonent.getClass().getName() + "!");
		}

	}
	
	public abstract void exec(GameMechanics gm);

}
