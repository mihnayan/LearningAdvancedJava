package mihnayan.divetojava.frontend;

import mihnayan.divetojava.base.AccountService;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Msg;
import mihnayan.divetojava.base.UserId;
import mihnayan.divetojava.msgsystem.MsgToAccountService;

/**
 * Message to AccountService for getting user Id by username.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class MsgGetUserId extends MsgToAccountService {

    private final String userName;
    private final String sessionId;

    /**
     * Creates message to AccountService for getting user Id by username.
     * @param from Address of sender.
     * @param to Address of recipient (concrete AccountService implementation).
     * @param userName Username which Id must be getting.
     * @param sessionId Id of session, which will be binding with the user.
     */
    public MsgGetUserId(Address from, Address to, String userName, String sessionId) {
        super(from, to);
        this.userName = userName;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(AccountService accountService) {
        UserId userId = accountService.getUserId(userName);
        Msg message = new MsgSetUserId(to, from, sessionId, userId, userName);
        accountService.getMessageService().sendMessage(message);
    }
}
