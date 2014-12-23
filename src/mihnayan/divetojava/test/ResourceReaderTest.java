package mihnayan.divetojava.test;

import mihnayan.divetojava.resourcesystem.GameSessionResource;
import mihnayan.divetojava.resourcesystem.Resource;
import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceLoadingException;
import mihnayan.divetojava.resourcesystem.ResourceNotExistException;

public class ResourceReaderTest {

	public static void main(String[] args) {
		
		Resource resource = null;
		
		ResourceFactory resFactory = ResourceFactory.instance();
		try {
			resFactory.loadResources();
			resource = resFactory.get(GameSessionResource.class);
			System.out.println(((GameSessionResource) resource).getBoardGridSize());
			System.out.println(((GameSessionResource) resource).getRequiredPlayers());
			
		} catch (ResourceNotExistException e) {
			e.printStackTrace();
		} catch (ResourceLoadingException e) {
			System.out.println("Can't load resources!");
			e.printStackTrace();
		}
		


	}

}
