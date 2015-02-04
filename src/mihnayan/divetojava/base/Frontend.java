package mihnayan.divetojava.base;

/**
 * The frontend for end user.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public interface Frontend extends Abonent {

    /**
     * Sets the session of authenticated user.
     * @param sessionId The Id of user session
     * @param userId The user Id received from the authentication system
     * @param userName The username
     */
    void setUser(String sessionId, UserId userId, String userName);

    /**
     * Sets the current game data.
     * @param gameData Current game data
     */
    void setGameData(GameData gameData);
}
