package mihnayan.divetojava.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class VFSImpl implements VFS {
	
	private String root;

	public VFSImpl(String root) {
		this.root = root;
	}

	@Override
	public boolean isExist(String path) {
		return getFile(path).exists();
	}

	@Override
	public boolean isDirectory(String path) {
		return getFile(path).isDirectory();
	}

	@Override
	public String getAbsolutePath(String file) {
		return getFile(file).getAbsolutePath();
	}

	@Override
	public byte[] getBytes(String file) {
		byte[] result;
		Path path = FileSystems.getDefault().getPath(root, file);
		try {
			result = Files.readAllBytes(path);
		} catch (IOException e) {
			result = new byte[0];
			e.printStackTrace();
		}
		return result;
	}
	
	public String getUFT8Text(String file) {
		File f = getFile(file);
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			fis = new FileInputStream(f);
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (isr != null)
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return sb.toString();
	}

	@Override
	public Iterator<String> getIterator(String startDir) {
		return new FileIterator(startDir);
	}
	
	private File getFile(String path) {
		String fullPath = root + File.pathSeparator + path;
		return new File(fullPath);
	}
	
	private class FileIterator implements Iterator<String> {
		
		private Queue<File> files = new LinkedList<File>();
		
		public FileIterator(String path) {
			files.add(getFile(path));
		}

		@Override
		public boolean hasNext() {
			return !files.isEmpty();
		}

		@Override
		public String next() {
			File file = files.peek();
			if (file.isDirectory()) {
				for (File subFile : file.listFiles()) {
					files.add(subFile);
				}
			}
			return files.poll().getAbsolutePath();
		}

		@Override
		public void remove() {}
		
	}

}
