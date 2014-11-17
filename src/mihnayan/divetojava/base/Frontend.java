package mihnayan.divetojava.base;


public interface Frontend extends Abonent {
	
	public void setUser(String sessionId, int userId, String userName);
	
	public void setGameData(GameData gameData);
}
