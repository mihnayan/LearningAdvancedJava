package mihnayan.divetojava.gamemechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mihnayan.divetojava.base.UserId;

public class GameSession {
	
	private long startTime;
	private List<UserId> userIds;

	public GameSession(UserId user1, UserId user2) {
		startTime = (new Date()).getTime();
		userIds = new ArrayList<UserId>(2);
		userIds.add(user1);
		userIds.add(user2);
	}

	public long getStartTime() {
		return startTime;
	}
	
	public List<UserId> getUserIds() {
		return Collections.unmodifiableList(userIds);
	}
}
