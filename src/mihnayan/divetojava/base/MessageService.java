package mihnayan.divetojava.base;

import mihnayan.divetojava.msgsystem.AddressSrv;

public interface MessageService {

	public void sendMessage(Msg message);
	
	public void execForAbonent(Abonent abonent);
	
	public AddressSrv getAddressService();
}
