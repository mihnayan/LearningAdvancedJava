package mihnayan.divetojava.gamemechanics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.base.GameSession;
import mihnayan.divetojava.base.GameState;
import mihnayan.divetojava.base.User;

/**
 * The GameData interface implementation.
 * @see GameData.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public class GameDataImpl implements GameData {

    private GameSession gameSession;

    GameDataImpl(GameSession gameSession) {
        this.gameSession = gameSession;
    }
    
    @Override
    public long getElapsedTime() {
        if (gameSession.getGameState() != GameState.GAMEPLAY) {
            return 0L;
        }
        return (new Date()).getTime() - gameSession.getStartTime();
    }

    @Override
    public List<User> getOpponents(User player) {
        List<User> opponents = new ArrayList<>();
        for (User opponent : gameSession.getPlayers()) {
            if (!opponent.equals(player)) {
                opponents.add(opponent);
            }
        }
        return opponents;
    }
}
