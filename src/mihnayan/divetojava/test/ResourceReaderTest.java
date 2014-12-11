package mihnayan.divetojava.test;

import mihnayan.divetojava.resourcesystem.ResourceReader;
import mihnayan.divetojava.utils.VFS;
import mihnayan.divetojava.utils.VFSImpl;

public class ResourceReaderTest {

	public static void main(String[] args) {
		
		VFS vfs = new VFSImpl("data\\");
		ResourceReader rr = new ResourceReader();
		
		rr.read(vfs.getAbsolutePath("GameResource.xml"));

	}

}
