package mihnayan.divetojava.base;

import java.util.Set;

/**
 * Interface that describes game mechanics.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public interface GameMechanics extends Abonent {

    /**
     * Starts game session (start game). If the game session is already created,
     * then nothing happens.
     * @param players The set of users that are in the game.
     */
    void startGameSession(Set<User> players);
    
    void requestGameData(User forUser);
}
