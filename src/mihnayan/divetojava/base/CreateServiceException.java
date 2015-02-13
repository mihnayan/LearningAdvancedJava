package mihnayan.divetojava.base;

/**
 * Occurs when the service cannot be created for some reason.
 * @author Mikhail Mangushev (Mihnayan)
 */
@SuppressWarnings("serial")
public class CreateServiceException extends Exception {

    /**
     * Constructs an CreateServiceException with the specified detail message.
     * @param message - the detail message.
     */
    public CreateServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public CreateServiceException(Throwable cause) {
        super(cause);
    }

}
