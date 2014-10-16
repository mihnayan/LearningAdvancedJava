package mihnayan.divetojava.msgsystem;

import java.util.HashMap;
import java.util.Map;

import mihnayan.divetojava.base.Abonent;
import mihnayan.divetojava.base.Address;
import mihnayan.divetojava.base.AddressService;

/**
 * Service that allows to save and to get the address of abonent
 * @author Mikail Mangushev
 *
 */
public class AddressSrv implements AddressService {

	private Map<Class<?>, Address> addresses = new HashMap<Class<?>, Address>();
	
	@Override
	public Address getAddress(Class<? extends Abonent> abonentClass) {
		return addresses.get(abonentClass);
	}
	
	@Override
	public void setAddress(Abonent abonent) {
		addresses.put(abonent.getClass(), abonent.getAddress());
	}
}
