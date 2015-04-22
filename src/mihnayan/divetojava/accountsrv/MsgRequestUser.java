package mihnayan.divetojava.accountsrv;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.DatabaseService;
import mihnayan.divetojava.msgsystem.MsgToDB;

public class MsgRequestUser extends MsgToDB {
    
    private String username;

    public MsgRequestUser(Address from, Address to, String username) {
        super(from, to);
        this.username = username;
    }
    
    @Override
    public void exec(DatabaseService dbService) {
        dbService.requestUserByName(username);
    }

}
