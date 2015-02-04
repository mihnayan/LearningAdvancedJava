package mihnayan.divetojava.msgsystem;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.AccountService;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Msg;

/**
 * Abstract class that creates message to AccountService.<br />
 * exec() method must be invoked for AccountService recipient. If recipient is not AccountService
 * object then WrongAbonentClassException will thrown.
 * @author Mikhail Mangushev (Mihnayan)
 */
public abstract class MsgToAccountService extends Msg {

    /**
     * Creates message.
     * @param from Address of sender.
     * @param to Address of recipient.
     */
    public MsgToAccountService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AccountService) {
            exec((AccountService) abonent);
        } else {
            throw new WrongAbonentClassException("Wrong abonent class: "
                    + abonent.getClass().getName() + "!");
        }
    }

    /**
     * Executes instructions that are members of AccountService object.
     * @param accountService AccountService implementation.
     */
    public abstract void exec(AccountService accountService);

}
