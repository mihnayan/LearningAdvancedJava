package mihnayan.divetojava.dbservice;

import mihnayan.divetojava.base.AccountService;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.msgsystem.MsgToAccountService;

public class MsgSetAuthenticationResult extends MsgToAccountService {
    
    private String username;
    private User user;
    private String resultText;

    public MsgSetAuthenticationResult(Address from, Address to, String username, User user,
            String resultText) {
        super(from, to);
        this.username = username;
        this.user = user;
        this.resultText = resultText;
    }

    @Override
    public void exec(AccountService accountService) {
        accountService.setAuthenticatedUser(username, user, resultText);
    }

}
