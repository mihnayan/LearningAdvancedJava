package mihnayan.divetojava.resourcesystem;

/**
 * Resource that contains the initialization parameters of the game session.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class GameSessionResource implements Resource {

    private int requiredPlayers;
    private int boardGridSize;

    /**
     * Returns number of players is needed for start game.
     * @return int.
     */
    public int getRequiredPlayers() {
        return requiredPlayers;
    }

    /**
     * Returns the number of cells on one side of the game board.
     * @return int.
     */
    public int getBoardGridSize() {
        return boardGridSize;
    }

}
