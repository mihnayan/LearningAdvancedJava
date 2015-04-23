package mihnayan.divetojava.base;

/**
 * The interface for authentication service.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public interface AccountService extends Abonent {
    
    void authenticateUserSession(UserSession session, String userName);
    
    void setAuthenticatedUser(String username, User user, String resultText);
}
