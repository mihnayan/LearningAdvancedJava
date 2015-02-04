package mihnayan.divetojava.gamemechanics;

import mihnayan.divetojava.base.GameData;

/**
 * The GameData interface implementation.
 * @see GameData.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public class GameDataImpl implements GameData {

    private long elapsedTime;

    @Override
    public long getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Sets elapsed time in milliseconds from begin of game.
     * @param elapsedTime The Long number representing the time in milliseconds.
     */
    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
