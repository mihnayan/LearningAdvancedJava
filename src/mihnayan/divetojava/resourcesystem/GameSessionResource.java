package mihnayan.divetojava.resourcesystem;

/**
 * Resource that contains the initialization parameters of the game session.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class GameSessionResource implements Resource {

    private int minPlayers;
    private int maxPlayers;
    private int boardGridSize;

    /**
     * Returns minimum number of players is needed for start game.
     * @return int.
     */
    public int getMinPlayers() {
        return minPlayers;
    }
    
    /**
     * Returns the maximum number of players that can participate in the game.
     * @return int
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Returns the number of cells on one side of the game board.
     * @return int.
     */
    public int getBoardGridSize() {
        return boardGridSize;
    }

}
