package mihnayan.divetojava.base;

/**
 * Service for getting the address of recipient which is subscriber of message system.
 * @author Mikhail Mangushev (Mihnayan)
 */
public interface AddressService {

    /**
     * Allows to get the address of recipient by it class name.
     * @param abonentClass The class of recipient
     * @return Address of recipient
     */
    Address getAddress(Class<? extends Abonent> abonentClass);

    /**
     * Sets the correspondence between recipient class name and it address.
     * @param abonent The class of recipient
     */
    void setAddress(Abonent abonent);
}
