package mihnayan.divetojava.base;


public interface Frontend extends Abonent {
	
	public void setUserId(String sessionId, int userId);
	
	public void setGameData(GameData gameData);
}
