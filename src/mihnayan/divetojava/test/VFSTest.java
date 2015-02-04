package mihnayan.divetojava.test;

import java.util.Iterator;

import mihnayan.divetojava.utils.VFS;
import mihnayan.divetojava.utils.VFSImpl;

/**
 * Simple test class for testing virtual file system.
 * @author Mikhail Mangushev (Mihnayan)
 */
public final class VFSTest {

    /**
     * Entry point of VFSTest class.
     * @param args No parameters are required.
     */
    public static void main(String[] args) {
        VFS vfs = new VFSImpl("src\\mihnayan\\divetojava\\");

        System.out.println(vfs.isExist("test\\VFSTest.java")); // true
        System.out.println(vfs.isExist("VFSTest.java")); // false
        System.out.println(vfs.isExist("test")); // true

        System.out.println();

        System.out.println(vfs.isDirectory("test\\VFSTest.java")); // false
        System.out.println(vfs.isDirectory("VFSTest.java")); // false
        System.out.println(vfs.isDirectory("test")); // true

        System.out.println(vfs.getAbsolutePath("test\\VFSTest.java"));

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

    private VFSTest() {

    }
}
