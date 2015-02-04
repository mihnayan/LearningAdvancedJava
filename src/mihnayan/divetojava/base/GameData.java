package mihnayan.divetojava.base;

/**
 * Contains Game data that will be displayed on frontend.
 * @author Mikhail Mangushev
 *
 */
public interface GameData {

    /**
     * @return The time in milliseconds elapsed since the start of the game.
     */
    long getElapsedTime();
}
