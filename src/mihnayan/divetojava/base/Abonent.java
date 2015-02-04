package mihnayan.divetojava.base;

/**
 * The interface for the subscriber, who may be a recipient in a messaging system.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public interface Abonent {

    /**
     * Returns a messaging system in which the subscriber is a recipient.
     * @return MessageService object
     */
    MessageService getMessageService();

    /**
     * Returns an address of the recipient.
     * @return Address object
     */
    Address getAddress();
}
