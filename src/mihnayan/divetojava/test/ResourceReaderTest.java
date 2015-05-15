package mihnayan.divetojava.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

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

    private ResourceFactory resourceFactory;
    
    @Before
    public void setUp() throws ResourceLoadingException {
        resourceFactory = ResourceFactory.instance();
        resourceFactory.loadResources();
    }
    
    @Test
    public void gameSessionResourceTest() throws ResourceNotExistException {
        Resource resource = resourceFactory.get(GameSessionResource.class);
        assertNotNull(resource);
        assertEquals(GameSessionResource.class, resource.getClass());
        
        GameSessionResource GSResource = (GameSessionResource) resource;
        int boardGridSize = GSResource.getBoardGridSize();
        assertTrue(boardGridSize > 0);
        
        int minPlayers = GSResource.getMinPlayers();
        assertTrue(minPlayers > 0);
        
        int maxPlayers = GSResource.getMaxPlayers();
        assertTrue(maxPlayers > 0);
    }
    
}
