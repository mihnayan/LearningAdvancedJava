package mihnayan.divetojava.gamemechanics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.GameData;
import mihnayan.divetojava.base.GameMechanics;
import mihnayan.divetojava.base.GameState;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.resourcesystem.GameSessionResource;
import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceNotExistException;

/**
 * Class represented the main game mechanics.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class MainGameMechanics implements GameMechanics, Runnable {

    private static Logger log = Logger.getLogger(MainGameMechanics.class
            .getName());

    private static final int SLEEP_TIME = 100;

    private MessageService ms;
    private Address address;
    private GameSessionImpl gameSession;
    private Frontend frontend;
    private int minPlayersCount;
    private int maxPlayersCount;
    
    private class GameDataImpl implements GameData {

        private User player;
        private long elapsedTime = 0L;
        private List<User> opponents = new ArrayList<>();

        @Override
        public long getElapsedTime() {
            return elapsedTime;
        }

        @Override
        public User getPlayer() {
            return player;
        }

        @Override
        public List<User> getOpponents() {
            return opponents;
        }
    }

    /**
     * Creates MainGameMechanics object.
     * @param ms Real message system for interaction with other components.
     * @param frontend The Frontend object, in the address which will be sent the current
     *        state of game.
     */
    public MainGameMechanics(MessageService ms, Frontend frontend) {
        this.ms = ms;
        this.frontend = frontend;
        address = new Address();
        ms.getAddressService().setAddress(this);
        
        try {
            GameSessionResource gsr = (GameSessionResource) ResourceFactory
                    .instance().get(GameSessionResource.class);
            this.minPlayersCount = gsr.getMinPlayers();
            this.maxPlayersCount = gsr.getMaxPlayers();

        } catch (ResourceNotExistException e) {
            log.log(Level.WARNING,
                    "Can't start game! Cause: not found resource "
                            + GameSessionResource.class.getName() + "!");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                ms.execForAbonent(this);
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MessageService getMessageService() {
        return ms;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void startGameSession(Set<User> players) {
        if (gameSession == null) {
            gameSession = new GameSessionImpl(minPlayersCount, maxPlayersCount);
        }
        if (gameSession.getGameState() != GameState.GAMEPLAY) {
            for (User player : players) {
                gameSession.addPlayer(player);
            }
            gameSession.startGame();
        }
    }

    @Override
    public void requestGameData(User forUser) {
        GameData gd = buildGameData(forUser);
        MsgSetGameData msg = new MsgSetGameData(address, frontend.getAddress(), gd, forUser);
        ms.sendMessage(msg);
    }

    private GameData buildGameData(User player) {
        GameDataImpl gd = new GameDataImpl();
        gd.player = player;
        if (gameSession.getGameState() != GameState.GAMEPLAY) return gd;

        gd.elapsedTime = (new Date()).getTime() - gameSession.getStartTime();
        
        for (User opponent : gameSession.getPlayers()) {
            if (!opponent.equals(player)) {
                gd.opponents.add(opponent);
            }
        }
        
        return gd;
    }
}
