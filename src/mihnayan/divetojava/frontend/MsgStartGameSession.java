package mihnayan.divetojava.frontend;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.base.UserId;
import mihnayan.divetojava.msgsystem.MsgToGM;

/**
 * Message to GameMechanics for start game session.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class MsgStartGameSession extends MsgToGM {

    private UserId user1;
    private UserId user2;

    /**
     * Creates message to GameMechanics for start game session.
     * @param from Address of sender.
     * @param to Address of recipient (concrete GameMechanics implementation).
     * @param user1 First user (player) Id.
     * @param user2 Second user (player) Id.
     */
    public MsgStartGameSession(Address from, Address to, UserId user1, UserId user2) {
        super(from, to);
        this.user1 = user1;
        this.user2 = user2;
    }

    @Override
    public void exec(GameMechanics gm) {
        gm.startGameSession(user1, user2);
    }
}
