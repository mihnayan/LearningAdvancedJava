package mihnayan.divetojava.resourcesystem;

import mihnayan.divetojava.utils.VFS;
import mihnayan.divetojava.utils.VFSImpl;

/**
 * Singleton 
 * @author Mikhail Mangushev
 *
 */
public class ResourceFactory {
	
	private static ResourceFactory resourceFactory;
	private static final String RESOURCE_DIRECTORY = "data\\";
	
	private VFS fileSystem;
	
	private ResourceFactory() {
		fileSystem = new VFSImpl(RESOURCE_DIRECTORY);
	}
	
	public static ResourceFactory instance() {
		if (resourceFactory == null) {
			resourceFactory = new ResourceFactory();
		}
		return resourceFactory;
	}
	
	public Resource get(String resourceFile) throws ResourceNotExistException {
		if (!fileSystem.isExist(resourceFile)) {
			throw new ResourceNotExistException("Resource " + resourceFile + " was not found!");
		}
		return null;
	}
}
