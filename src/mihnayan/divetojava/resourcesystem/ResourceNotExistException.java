package mihnayan.divetojava.resourcesystem;

/**
 * ResourceNotExistException occurs if requested resource is not exist.
 * @author Mikhail Mangushev (Mihnayan)
 */
@SuppressWarnings("serial")
public class ResourceNotExistException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message.
     */
    public ResourceNotExistException(String message) {
        super(message);
    }

}
