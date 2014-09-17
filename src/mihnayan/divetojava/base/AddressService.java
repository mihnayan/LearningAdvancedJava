package mihnayan.divetojava.base;


public interface AddressService {

	/**
	 * Allows to get abonent address by abonent class name
	 * @param Abonent class name
	 * @return Address of abonent
	 */
	public Address getAddress(Class<? extends Abonent> abonentClass);
	
	/**
	 * Sets the correspondence between abonent class name and address of abonent
	 * @param abonent
	 */
	public void setAddress(Abonent abonent);
}
