package mihnayan.divetojava.resourcesystem;

@SuppressWarnings("serial")
public class ResourceLoadingException extends Exception {

	public ResourceLoadingException(String message) {
		super(message);
	}

	public ResourceLoadingException(Throwable cause) {
		super(cause);
	}

}
