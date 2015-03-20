package mihnayan.divetojava.gamemechanics;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import mihnayan.divetojava.base.GameSession;
import mihnayan.divetojava.base.GameState;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.base.UserId;

/**
 * The class describing the game session.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class GameSessionImpl implements GameSession {

    private long startTime = 0L;
    private Set<User> players;
    private GameState gameState;
    private Integer minUsers;
    private Integer maxUsers;

    /**
     * Creates GameSession object.
     */
    public GameSessionImpl(Integer minUsers, Integer maxUsers) {
        this.startTime = (new Date()).getTime();
        this.players = new HashSet<>();
        this.minUsers = minUsers;
        this.maxUsers = maxUsers;
        this.gameState = GameState.WAITING_FOR_QUORUM;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public Set<User> getPlayers() {
        return players;
    }

    @Override
    public void addPlayer(User player) {
        if (players.size() > maxUsers) {
            return;
        }
        players.add(player);
        updateGameState();
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }
    
    @Override
    public void startGame() {
        if (gameState == GameState.WAITING_FOR_GAME) {
            gameState = GameState.GAMEPLAY;
        }
    }

    private void updateGameState() {
        if (gameState == GameState.GAMEPLAY) {
            return;
        }
        if (gameState == GameState.WAITING_FOR_QUORUM && players.size() >= minUsers) {
            gameState = GameState.WAITING_FOR_GAME;
        }
    }
    
}
