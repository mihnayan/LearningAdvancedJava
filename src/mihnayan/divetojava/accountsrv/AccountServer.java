package mihnayan.divetojava.accountsrv;

import java.util.Date;
import java.util.HashMap;
import java.util.NavigableSet;
import java.util.TreeSet;

import mihnayan.divetojava.base.AccountService;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Frontend;
import mihnayan.divetojava.base.MessageService;
import mihnayan.divetojava.base.User;
import mihnayan.divetojava.base.UserId;
import mihnayan.divetojava.base.UserSession;
import mihnayan.divetojava.dbservice.MainDatabaseService;

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
public final class AccountServer implements Runnable, AccountService {

    private static final int SLEEP_TIME = 5000;
    private static final long LOGIN_TIMEOUT = 30000;

    private MessageService ms;
    private Address address;
    private Frontend frontend;
    
    private static final class AuthData implements Comparable<AuthData> {
        private final UserSession session;
        private final String username;
        private final Long beginTimestamp;
        
        AuthData(UserSession session, String username) {
            this.session = session;
            this.username = username;
            this.beginTimestamp = (new Date()).getTime();
        }

        @Override
        public int compareTo(AuthData o) {
            return beginTimestamp.compareTo(o.beginTimestamp);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof AuthData)) return false;
            AuthData ad = (AuthData) obj;
            return username.equals(ad.username) && 
                    session.equals(ad.session) && 
                    beginTimestamp.equals(ad.beginTimestamp);
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + session.hashCode();
            result = 31 * result + username.hashCode();
            result = 31 * result + (int) (beginTimestamp ^ (beginTimestamp >>> 32));
            return result;
        }
        
        
    }

    private static HashMap<String, UserId> userDb = new HashMap<String, UserId>();
    static {
        int idCount = 0;
        userDb = new HashMap<String, UserId>();
        userDb.put("Anakin Skywalker", new UserId(Integer.toString(++idCount)));
        userDb.put("Yoda", new UserId(Integer.toString(++idCount)));
        userDb.put("Mace Windu", new UserId(Integer.toString(++idCount)));
        userDb.put("Obi-Wan Kenobi", new UserId(Integer.toString(++idCount)));
    }
    
    private final NavigableSet<AuthData> pendingAuth = new TreeSet<>();

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
//        UserId userId = userDb.get(userName);
//        if (userId != null) {
//            User user = new User(userId, userName);
//            session.setUser(user);
//        }
//        MsgSetAuthenticatedUserSession msg =
//                new MsgSetAuthenticatedUserSession(address, frontend.getAddress(), session);
//        ms.sendMessage(msg);
        
        pendingAuth.add(new AuthData(session, userName));
        MsgRequestUser msg =
                new MsgRequestUser(address, 
                        ms.getAddressService().getAddress(MainDatabaseService.class), userName);
        ms.sendMessage(msg);
        System.out.println("Sending message to DB. Username: " + userName);
    }
    
    @Override
    public void setAuthenticatedUser(String username, User user, String resultText) {
       for (AuthData ad : pendingAuth) {
           if (ad.username == username) {
               UserSession session = ad.session;
               session.setUser(user);
               pendingAuth.remove(ad);
               
               MsgSetAuthenticatedUserSession msg = new MsgSetAuthenticatedUserSession(address,
                       frontend.getAddress(), session);
               ms.sendMessage(msg);
           }
       }
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                // emulation of a long process
                Thread.sleep(SLEEP_TIME);
                ms.execForAbonent(this);
//                deleteExpired();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void deleteExpired() {
        long now = (new Date()).getTime();
        boolean hasExpired = true;
        
        while (!pendingAuth.isEmpty() && hasExpired) {
            hasExpired = now - pendingAuth.first().beginTimestamp > LOGIN_TIMEOUT;
            if (hasExpired) {
                AuthData ad = pendingAuth.pollFirst();
                System.out.println("\n deleted: " + ad.username + "\n");
            }
        }
    }
    
//    private void sendLoginMessage(User)
}
