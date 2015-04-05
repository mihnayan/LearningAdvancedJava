package mihnayan.divetojava.frontend;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.msgsystem.MsgToGM;

public class MsgRequestGameData extends MsgToGM {

    private User user;
    
    MsgRequestGameData(Address from, Address to, User forUser) {
        super(from, to);
        this.user = forUser;
    }
    
    @Override
    public void exec(GameMechanics gm) {
        gm.requestGameData(user);
    }

}
