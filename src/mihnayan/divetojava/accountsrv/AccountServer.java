package mihnayan.divetojava.accountsrv;

import java.util.HashMap;

import mihnayan.divetojava.base.AccountService;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.UserId;

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
    public AccountServer(MessageService ms) {
        this.ms = ms;
        address = new Address();
        ms.getAddressService().setAddress(this);
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

    /**
     * Returns UserId object by it username.
     * @param userName username
     * @return UserId object
     */
    public UserId getUserId(String userName) {
        if (userDb.containsKey(userName)) {
            return userDb.get(userName);
        } else {
            return null;
        }
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
