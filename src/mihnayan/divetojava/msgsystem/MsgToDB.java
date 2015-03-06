package mihnayan.divetojava.msgsystem;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Msg;

public class MsgToDB extends Msg {

    public MsgToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
//        if (abonent instanceof DatabaseService) {
//            exec((GameMechanics) abonent);
//        } else {
//            throw new WrongAbonentClassException("Wrong abonent class: "
//                    + abonent.getClass().getName() + "!");
//        }
    }

}
