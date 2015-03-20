package mihnayan.divetojava.gamemechanics;

import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
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
                sendGameData();
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

    private void sendGameData() {
        if (gameSession == null) {
            return;
        }
        GameDataImpl gd = new GameDataImpl(gameSession);
        MsgSetGameData msg = new MsgSetGameData(address, frontend.getAddress(),
                gd);
        ms.sendMessage(msg);
    }

}
