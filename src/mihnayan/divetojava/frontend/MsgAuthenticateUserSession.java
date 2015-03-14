package mihnayan.divetojava.frontend;

import mihnayan.divetojava.base.AccountService;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.UserSession;
import mihnayan.divetojava.msgsystem.MsgToAccountService;

public final class MsgAuthenticateUserSession extends MsgToAccountService {

    private UserSession session;
    private String userName;
    
    public MsgAuthenticateUserSession(Address from, Address to, UserSession session,
            String userName) {
        super(from, to);
        this.session = session;
        this.userName = userName;
    }

    @Override
    public void exec(AccountService accountService) {
        accountService.authenticateUserSession(session, userName);
    }

}
