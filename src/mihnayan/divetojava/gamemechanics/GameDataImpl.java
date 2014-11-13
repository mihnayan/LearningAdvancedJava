package mihnayan.divetojava.gamemechanics;

import mihnayan.divetojava.base.GameData;

public class GameDataImpl implements GameData {
	
	private long elapsedTime;

	@Override
	public long getElapsedTime() {
		return elapsedTime;
	}
	
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

}
