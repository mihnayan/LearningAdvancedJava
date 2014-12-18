package mihnayan.divetojava.test;

import mihnayan.divetojava.resourcesystem.Resource;
import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceNotExistException;

public class ResourceReaderTest {

	public static void main(String[] args) {
		
		Resource resource = null;
		
		ResourceFactory resFactory = ResourceFactory.instance();
		try {
			resource = resFactory.get("GameResource.xml");
			
		} catch (ResourceNotExistException e) {
			e.printStackTrace();
		}
		


	}

}
