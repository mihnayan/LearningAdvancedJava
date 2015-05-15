package mihnayan.divetojava.test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import mihnayan.divetojava.utils.VFS;
import mihnayan.divetojava.utils.VFSImpl;

/**
 * Simple test class for testing virtual file system.
 * @author Mikhail Mangushev (Mihnayan)
 */
public final class VFSTest {
    
    private VFS vfs;
    
    @Before
    public void setUp() {
        vfs = new VFSImpl("src\\mihnayan\\divetojava\\");
    }
    
    @Test
    public void isExistsFile() {
        boolean isExistsFile = vfs.isExist("test\\VFSTest.java");
        assertTrue(isExistsFile);
        
        isExistsFile = vfs.isExist("VFSTest.java");
        assertFalse(isExistsFile);
        
        isExistsFile = vfs.isExist("test");
        assertTrue(isExistsFile);
    }
    
    @Test
    public void isDirectory() {
        boolean isDir = vfs.isDirectory("test\\VFSTest.java");
        assertFalse(isDir);
        
        isDir = vfs.isDirectory("VFSTest.java");
        assertFalse(isDir);
        
        isDir = vfs.isDirectory("test");
        assertTrue(isDir);
    }
    
    @Test
    public void checkAbsolutePath() {
        String path = "C:\\_work\\_learning\\_java\\DiveToJava\\code\\DiveToJava\\src\\mihnayan\\"
                + "divetojava\\test\\VFSTest.java";
        String absolutePath = vfs.getAbsolutePath("test\\VFSTest.java");
        assertEquals(path, absolutePath);
    }

    /**
     * Entry point of VFSTest class.
     * @param args No parameters are required.
     */
    public static void main(String[] args) {
        VFS vfs = new VFSImpl("src\\mihnayan\\divetojava\\");
        
        VFS vfs2 = new VFSImpl("for_test\\");

        System.out.println(vfs2.isExist("bytes.txt"));
        byte[] bytes = vfs2.getBytes("bytes.txt");
        for (int i = 0, l = bytes.length; i < l; i++) {
            char c = (char) bytes[i];
            System.out.print(bytes[i] + "(" + c + ") ");
        }

        System.out.println("\n");

        System.out.println(vfs2.isExist("zerobytes.txt"));
        bytes = vfs2.getBytes("zerobytes.txt");
        System.out.println(bytes.length);

        System.out.println();

        System.out.println(vfs2.isExist("utf8text.txt"));
        String utf8text = vfs2.getUFT8Text("utf8text.txt");
        System.out.println(utf8text);

        System.out.println("\n");

        Iterator<String> files = vfs.getIterator("");
        while (files.hasNext()) {
            System.out.println(files.next());
        }
    }
}
