package mihnayan.divetojava.msgsystem;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.AccountService;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Msg;

public abstract class MsgToAccountService extends Msg {

	public MsgToAccountService(Address from, Address to) {
		super(from, to);
	}

	@Override
	public void exec(Abonent abonent) {
		if (abonent instanceof AccountService) {
			exec((AccountService) abonent);
		} else {
			throw new WrongAbonentClassException(
					"Wrong abonent class: " + abonent.getClass().getName() + "!");
		}
	}
	
	public abstract void exec(AccountService accountService);

}
