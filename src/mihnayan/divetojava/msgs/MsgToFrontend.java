package mihnayan.divetojava.msgs;

import java.util.logging.Level;
import java.util.logging.Logger;

import mihnayan.divetojava.Frontend;
import mihnayan.divetojava.msgsystem.Abonent;
import mihnayan.divetojava.msgsystem.Address;
import mihnayan.divetojava.msgsystem.Msg;

public abstract class MsgToFrontend extends Msg {
	
	private static Logger log = Logger.getLogger(MsgToFrontend.class.getName());

	public MsgToFrontend(Address from, Address to) {
		super(from, to);
	}

	@Override
	public void exec(Abonent abonent) {
		if (abonent instanceof Frontend) {
			exec((Frontend) abonent);
		} else {
			log.log(Level.SEVERE, "Wrong sender class: " + abonent.getClass().getName() + "!");
		}
	}

}
