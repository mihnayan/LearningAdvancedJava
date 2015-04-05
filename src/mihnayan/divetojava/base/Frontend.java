package mihnayan.divetojava.base;

/**
 * The frontend for end user.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public interface Frontend extends Abonent {

    /**
     * Sets the session of authenticated user.
     * @param userSession User session that contains user data if user was successfully
     * authenticated, or null if authentication was failed.
     */
    void setAuthenticatedUserSession(UserSession userSession);

    /**
     * Sets the current game data.
     * @param gameData Current game data
     */
    void setGameData(GameData gameData, User forUser);
    
    int getMinPlayersCount();
}
