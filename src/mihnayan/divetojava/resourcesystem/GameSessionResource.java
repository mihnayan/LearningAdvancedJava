package mihnayan.divetojava.resourcesystem;

public class GameSessionResource implements Resource {

	private int requiredPlayers;
	private int boardGridSize;
	
	public int getRequiredPlayers() {
		return requiredPlayers;
	}
	public int getBoardGridSize() {
		return boardGridSize;
	}
	
}
