package mihnayan.divetojava.gamemechanics;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.msgsystem.MsgToFrontend;

public class MsgSetGameData extends MsgToFrontend {
	
	private GameData gameData;

	public MsgSetGameData(Address from, Address to, GameData gameData) {
		super(from, to);
		this.gameData = gameData;
	}

	@Override
	public void exec(Frontend frontend) {
		frontend.setGameData(gameData);
	}

}
