package mihnayan.divetojava.base;

import java.util.Date;
import java.util.Set;

public interface GameSession {

    /**
     * Returns game start time in milliseconds.
     * @see Date#getTime()
     * @return The Long number representing the time in milliseconds.
     */
    long getStartTime();
    
    /**
     * Returns the set of players participating in the session.
     * @return List of UserId object.
     */
    Set<User> getPlayers();
    
    void addPlayer(User player);
    
    GameState getGameState();
    
    void startGame();
}
