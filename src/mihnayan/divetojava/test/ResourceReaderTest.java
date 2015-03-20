package mihnayan.divetojava.test;

import mihnayan.divetojava.resourcesystem.GameSessionResource;
import mihnayan.divetojava.resourcesystem.Resource;
import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceLoadingException;
import mihnayan.divetojava.resourcesystem.ResourceNotExistException;

/**
 * Simple test class for testing resource system.
 * @author Mikhail Mangushev (Mihnayan)
 */
public final class ResourceReaderTest {

    /**
     * Entry point of ResourceReaderTest class.
     * @param args No parameters are required.
     */
    public static void main(String[] args) {

        Resource resource = null;

        ResourceFactory resFactory = ResourceFactory.instance();
        try {
            resFactory.loadResources();
            resource = resFactory.get(GameSessionResource.class);
            System.out.println(((GameSessionResource) resource)
                    .getBoardGridSize());
            System.out.println(((GameSessionResource) resource)
                    .getMinPlayers());
            System.out.println(((GameSessionResource) resource)
                    .getMaxPlayers());

        } catch (ResourceNotExistException e) {
            e.printStackTrace();
        } catch (ResourceLoadingException e) {
            System.out.println("Can't load resources!");
            e.printStackTrace();
        }

    }

    private ResourceReaderTest() {

    }
}
