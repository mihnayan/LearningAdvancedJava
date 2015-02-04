package mihnayan.divetojava.msgsystem;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.base.Msg;

/**
 * Abstract class that creates message to GameMechanics.<br />
 * exec() method must be invoked for GameMechanics recipient. If recipient is not GameMechanics
 * object then WrongAbonentClassException will thrown.
 * @author Mikhail Mangushev (Mihnayan)
 */
public abstract class MsgToGM extends Msg {

    /**
     * Creates message.
     * @param from Address of sender.
     * @param to Address of recipient.
     */
    public MsgToGM(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GameMechanics) {
            exec((GameMechanics) abonent);
        } else {
            throw new WrongAbonentClassException("Wrong abonent class: "
                    + abonent.getClass().getName() + "!");
        }

    }

    /**
     * Executes instructions that are members of GameMechanics object.
     * @param gm GameMechanics implementation.
     */
    public abstract void exec(GameMechanics gm);

}
