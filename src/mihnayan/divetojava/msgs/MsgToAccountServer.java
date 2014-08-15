package mihnayan.divetojava.msgs;

import mihnayan.divetojava.AccountServer;
import mihnayan.divetojava.msgsystem.Abonent;
import mihnayan.divetojava.msgsystem.Address;
import mihnayan.divetojava.msgsystem.Msg;
import mihnayan.divetojava.msgsystem.WrongAbonentClassException;

public class MsgToAccountServer extends Msg {

	public MsgToAccountServer(Address from, Address to) {
		super(from, to);
	}

	@Override
	public void exec(Abonent abonent) {
		if (abonent instanceof AccountServer) {
			exec((AccountServer) abonent);
		} else {
			throw new WrongAbonentClassException(
					"Wrong abonent class: " + abonent.getClass().getName() + "!");
		}
	}

}
