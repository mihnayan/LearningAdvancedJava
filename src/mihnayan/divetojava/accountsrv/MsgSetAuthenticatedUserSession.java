package mihnayan.divetojava.accountsrv;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.UserSession;
import mihnayan.divetojava.msgsystem.MsgToFrontend;

public final class MsgSetAuthenticatedUserSession extends MsgToFrontend {

    private UserSession session;
    
    public MsgSetAuthenticatedUserSession(Address from, Address to, UserSession session) {
        super(from, to);
        this.session = session;
    }

    @Override
    public void exec(Frontend frontend) {
        frontend.setAuthenticatedUserSession(session);
    }

}
