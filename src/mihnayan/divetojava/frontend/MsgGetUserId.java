package mihnayan.divetojava.frontend;

import mihnayan.divetojava.base.AccountService;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.Msg;
import mihnayan.divetojava.msgsystem.MsgToAccountService;

public class MsgGetUserId extends MsgToAccountService {

	final private String userName;
	final private String sessionId;

	public MsgGetUserId(Address from, Address to, String userName, String sessionId) {
		super(from, to);
		this.userName = userName;
		this.sessionId = sessionId;
	}

	@Override
	public void exec(AccountService accountService) {
		int userId = accountService.getUserId(userName);
		Msg message = new MsgSetUserId(to, from, sessionId, userId);
		accountService.getMessageService().sendMessage(message);
	}
	
	
}
