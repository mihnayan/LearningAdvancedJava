package mihnayan.divetojava.utils;

import java.util.Iterator;

/**
 * Interface that describes virtual file system (wrapper over physical file system).
 * @author Mikhail Mangushev (Mihnayan)
 */
public interface VFS {

    /**
     * Checks is file exist.
     * @param path Path to file.
     * @return True if file exists, false otherwise.
     */
    boolean isExist(String path);

    /**
     * Checks if the file is a directory.
     * @param path Path to file.
     * @return True if file is directory, false otherwise.
     */
    boolean isDirectory(String path);

    /**
     * Returns the absolute pathname string of the file.
     * @param file The file path should be returned.
     * @return String of absolute path of the file.
     */
    String getAbsolutePath(String file);

    /**
     * Reads all the bytes from a file.
     * @param file The file from which to read bytes.
     * @return Array of bytes.
     */
    byte[] getBytes(String file);

    /**
     * Returns the text read from the file in utf8 format.
     * @param file The file which should be read.
     * @return String in utf8 format.
     */
    String getUFT8Text(String file);

    /**
     * Returns an iterator over the file tree for a given directory.
     * @param startDir Starting directory for iterator (root of the file tree).
     * @return Iterator of strings that are the names of files and directories.
     */
    Iterator<String> getIterator(String startDir);
}
