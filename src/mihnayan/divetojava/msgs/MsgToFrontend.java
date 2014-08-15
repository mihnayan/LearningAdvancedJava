package mihnayan.divetojava.msgs;

import mihnayan.divetojava.Frontend;
import mihnayan.divetojava.msgsystem.Abonent;
import mihnayan.divetojava.msgsystem.Address;
import mihnayan.divetojava.msgsystem.Msg;
import mihnayan.divetojava.msgsystem.WrongAbonentClassException;

public abstract class MsgToFrontend extends Msg {

	public MsgToFrontend(Address from, Address to) {
		super(from, to);
	}

	@Override
	public void exec(Abonent abonent) {
		if (abonent instanceof Frontend) {
			exec((Frontend) abonent);
		} else {
			throw new WrongAbonentClassException(
					"Wrong abonent class: " + abonent.getClass().getName() + "!");
		}
	}

}
