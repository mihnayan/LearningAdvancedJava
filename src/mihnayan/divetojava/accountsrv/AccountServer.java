package mihnayan.divetojava.accountsrv;

import java.util.HashMap;

import mihnayan.divetojava.base.AccountService;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.base.UserId;
import mihnayan.divetojava.base.UserSession;
import mihnayan.divetojava.frontend.AuthState;

/**
 * Class which authenticates a specific user for a specific session.
 * Authentication is starts after invoking start() method of class. After
 * success end of process puts user ID in the sessions HashMap for specific
 * session. If user has not been authenticated removes record from the map for
 * the session.
 *
 * @author Mikhail Mangushev
 *
 */
public class AccountServer implements Runnable, AccountService {

    private static final int SLEEP_TIME = 5000;

    private MessageService ms;
    private Address address;
    private Frontend frontend;
    
    private HashMap<UserId, User> users = new HashMap<>();

    private static HashMap<String, UserId> userDb = new HashMap<String, UserId>();
    static {
        int idCount = 0;
        userDb = new HashMap<String, UserId>();
        userDb.put("Anakin Skywalker", new UserId(Integer.toString(++idCount)));
        userDb.put("Yoda", new UserId(Integer.toString(++idCount)));
        userDb.put("Mace Windu", new UserId(Integer.toString(++idCount)));
        userDb.put("Obi-Wan Kenobi", new UserId(Integer.toString(++idCount)));
    }

    /**
     * @param ms Real message system for interaction with other components.
     */
    public AccountServer(MessageService ms, Frontend frontend) {
        this.ms = ms;
        address = new Address();
        ms.getAddressService().setAddress(this);
        this.frontend = frontend;
    }

    /**
     * Helper method to spell-check the user name.
     * @param userName The username
     * @return True or False
     */
    public static boolean isValidUserName(String userName) {
        return userName != null && userName.trim() != "";
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageService getMessageService() {
        return ms;
    }
    
    @Override
    public void authenticateUserSession(UserSession session, String userName) {
        UserId userId = userDb.get(userName);
        if (userId != null) {
            User user = new User(userId, userName);
            session.setUser(user);
        }
        MsgSetAuthenticatedUserSession msg =
                new MsgSetAuthenticatedUserSession(address, frontend.getAddress(), session);
        ms.sendMessage(msg);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // emulation of a long process
                Thread.sleep(SLEEP_TIME);
                ms.execForAbonent(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
