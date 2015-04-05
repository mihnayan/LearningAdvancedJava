package mihnayan.divetojava.frontend;

import java.util.Set;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.msgsystem.MsgToGM;

/**
 * Message to GameMechanics for start game session.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class MsgStartGameSession extends MsgToGM {

    private Set<User> players;

    /**
     * Creates message to GameMechanics for start game session.
     * @param from Address of sender.
     * @param to Address of recipient (concrete GameMechanics implementation).
     * @param players Set of players.
     */
    MsgStartGameSession(Address from, Address to, Set<User> players) {
        super(from, to);
        this.players = players;
    }

    @Override
    public void exec(GameMechanics gm) {
        gm.startGameSession(players);
    }
}
