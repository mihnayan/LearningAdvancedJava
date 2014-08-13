package mihnayan.divetojava.msgs;

import java.util.logging.Level;
import java.util.logging.Logger;

import mihnayan.divetojava.AccountServer;
import mihnayan.divetojava.msgsystem.Abonent;
import mihnayan.divetojava.msgsystem.Address;
import mihnayan.divetojava.msgsystem.Msg;

public class MsgToAccountServer extends Msg {
	
	private static Logger log = Logger.getLogger(MsgToAccountServer.class.getName());

	public MsgToAccountServer(Address from, Address to) {
		super(from, to);
	}

	@Override
	public void exec(Abonent abonent) {
		if (abonent instanceof AccountServer) {
			exec((AccountServer) abonent);
		} else {
			log.log(Level.SEVERE, "Wrong sender class: " + abonent.getClass().getName() + "!");
		}
	}

}
