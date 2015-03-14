package mihnayan.divetojava.base;

/**
 * The interface for authentication service.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public interface AccountService extends Abonent {

    /**
     * Returns user Id by it username.
     * @param userName The username
     * @return UserId object
     */
//    UserId getUserId(String userName);
    
    void authenticateUserSession(UserSession session, String userName);
}
