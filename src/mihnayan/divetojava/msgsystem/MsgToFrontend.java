package mihnayan.divetojava.msgsystem;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.Msg;

/**
 * Abstract class that creates message to Frontend.<br />
 * exec() method must be invoked for Frontend recipient. If recipient is not Frontend
 * object then WrongAbonentClassException will thrown.
 * @author Mikhail Mangushev (Mihnayan)
 */
public abstract class MsgToFrontend extends Msg {

    /**
     * Creates message.
     * @param from Address of sender.
     * @param to Address of recipient.
     */
    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof Frontend) {
            exec((Frontend) abonent);
        } else {
            throw new WrongAbonentClassException("Wrong abonent class: "
                    + abonent.getClass().getName() + "!");
        }
    }

    /**
     * Executes instructions that are members of Frontend object.
     * @param frontend Frontend implementation.
     */
    public abstract void exec(Frontend frontend);

}
