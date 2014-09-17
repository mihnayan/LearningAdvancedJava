package mihnayan.divetojava.msgsystem;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.Msg;

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
	
	public abstract void exec(Frontend frontend);

}
