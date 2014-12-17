package mihnayan.divetojava.test;

import mihnayan.divetojava.resourcesystem.ResourceFactory;
import mihnayan.divetojava.resourcesystem.ResourceNotExistException;
import mihnayan.divetojava.resourcesystem.ResourceReader;
import mihnayan.divetojava.utils.VFS;
import mihnayan.divetojava.utils.VFSImpl;

public class ResourceReaderTest {

	public static void main(String[] args) {
		
		VFS vfs = new VFSImpl("data\\");
		
		ResourceFactory resFactory = ResourceFactory.instance();
		try {
			resFactory.get("GameResource.xml");
		} catch (ResourceNotExistException e) {
			e.printStackTrace();
		}
		
//		ResourceReader rr = new ResourceReader();
//		
//		rr.read(vfs.getAbsolutePath("GameResource.xml"));
		
//		rr.read(vfs.getAbsolutePath("unExist.file"));

	}

}
