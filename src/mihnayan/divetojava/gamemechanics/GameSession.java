package mihnayan.divetojava.gamemechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GameSession {
	
	private int user1;
	private int user2;
	private long startTime;
	private List<Integer> userIds;

	public GameSession(int user1, int user2) {
		this.user1 = user1;
		this.user2 = user2;
		startTime = (new Date()).getTime();
		userIds = new ArrayList<Integer>(2);
		userIds.add(user1);
		userIds.add(user2);
	}

	public long getStartTime() {
		return startTime;
	}
	
	public List<Integer> getUserIds() {
		return Collections.unmodifiableList(userIds);
	}
}
