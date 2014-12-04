package mihnayan.divetojava.base;

public interface GameMechanics extends Abonent {

	/**
	 * Starts game session (start game). 
	 * If the game session is already created, then nothing happens
	 * @param user1 user id of first opponent
	 * @param user2 user id of second opponent
	 */
	public void startGameSession(UserId user1, UserId user2);
}
