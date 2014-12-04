package mihnayan.divetojava.base;


public interface Frontend extends Abonent {
	
	public void setUser(String sessionId, UserId userId, String userName);
	
	public void setGameData(GameData gameData);
}
