package mihnayan.divetojava.msgsystem;

/**
 * Occurs when attempting to execute a statements are not intended for this type of message.
 * @author Mikhail Mangushev (Mihnayan)
 */
@SuppressWarnings("serial")
public class WrongAbonentClassException extends RuntimeException {

    /**
     * Constructs an WrongAbonentClassException with the specified detail message.
     * @param s - the detail message.
     */
    public WrongAbonentClassException(String s) {
        super(s);
    }

}
