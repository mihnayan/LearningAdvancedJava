package mihnayan.divetojava.gamemechanics;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.msgsystem.MsgToFrontend;

/**
 * Message for updating game data in the Frontend object.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class MsgSetGameData extends MsgToFrontend {

    private GameData gameData;
    private User user;

    /**
     * Creates message.
     * @param from Address of sender.
     * @param to Address of recipient (concrete Frontend implementation).
     * @param gameData The GameData object that contains game data for updating.
     */
    public MsgSetGameData(Address from, Address to, GameData gameData, User forUser) {
        super(from, to);
        this.gameData = gameData;
        this.user = forUser;
    }

    @Override
    public void exec(Frontend frontend) {
        frontend.setGameData(gameData, user);
    }

}
