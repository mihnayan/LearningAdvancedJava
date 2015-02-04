package mihnayan.divetojava.frontend;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.UserId;
import mihnayan.divetojava.msgsystem.MsgToFrontend;

/**
 * Message to Frontend for binding user id with it session.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public class MsgSetUserId extends MsgToFrontend {

    private final String sessionId;
    private final UserId userId;
    private final String userName;

    /**
     * Creates message to Frontend for binding user id with it session.
     * @param from Address of sender.
     * @param to Address of recipient (concrete Frontend implementation).
     * @param sessionId Id of session, which will be binding with the user.
     * @param userId User Id which will be binding with the session.
     * @param userName The username corresponding to user Id.
     */
    public MsgSetUserId(Address from, Address to, String sessionId, UserId userId,
            String userName) {
        super(from, to);
        this.sessionId = sessionId;
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public void exec(Frontend frontend) {
        frontend.setUser(sessionId, userId, userName);
    }
}
