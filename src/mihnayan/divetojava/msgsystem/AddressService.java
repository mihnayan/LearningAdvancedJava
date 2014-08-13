package mihnayan.divetojava.msgsystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Service that allows to save and to get the address of abonent
 * @author Mikail Mangushev
 *
 */
public class AddressService {

	private Map<Class<?>, Address> addresses = new HashMap<Class<?>, Address>();
	
	/**
	 * Allows to get abonent address by abonent class name
	 * @param Abonent class name
	 * @return Address of abonent
	 */
	public Address getAddress(Class<?> abonentClass) {
		return addresses.get(abonentClass);
	}
	
	/**
	 * Sets the correspondence between abonent class name and address of abonent
	 * @param abonent
	 */
	public void setAddress(Abonent abonent) {
		addresses.put(abonent.getClass(), abonent.getAddress());
	}
}
