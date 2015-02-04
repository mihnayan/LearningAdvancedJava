package mihnayan.divetojava.base;

/**
 * The message service interface. Describes the commands for sending messages and manipulating them.
 * @author Mikhail Mangushev (Mihnayan)
 */
public interface MessageService {

    /**
     * Sends the message.
     * @param message The outgoing message.
     */
    void sendMessage(Msg message);

    /**
     * Executes all commands for recipient.
     * @param abonent The recipient whose commands must be executed.
     */
    void execForAbonent(Abonent abonent);

    /**
     * Returns address service for this message service.
     * @return AddressService object.
     */
    AddressService getAddressService();
}
