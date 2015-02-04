package mihnayan.divetojava.gamemechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mihnayan.divetojava.base.UserId;

/**
 * The class describing the game session.
 * @author Mikhail Mangushev (Mihnayan)
 */
public class GameSession {

    private long startTime;
    private List<UserId> userIds;

    /**
     * Creates GameSession object.
     * @param user1 First player Id.
     * @param user2 Second player Id.
     */
    public GameSession(UserId user1, UserId user2) {
        startTime = (new Date()).getTime();
        userIds = new ArrayList<UserId>(2);
        userIds.add(user1);
        userIds.add(user2);
    }

    /**
     * Returns game start time in milliseconds.
     * @see Date#getTime()
     * @return The Long number representing the time in milliseconds.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Returns The list of Id of users participating in the session.
     * @return List of UserId object.
     */
    public List<UserId> getUserIds() {
        return Collections.unmodifiableList(userIds);
    }
}
