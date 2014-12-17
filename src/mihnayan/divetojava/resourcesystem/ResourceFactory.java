package mihnayan.divetojava.resourcesystem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import mihnayan.divetojava.utils.VFS;
import mihnayan.divetojava.utils.VFSImpl;

/**
 * Singleton 
 * @author Mikhail Mangushev
 *
 */
public class ResourceFactory {
	
	private static ResourceFactory resourceFactory;
	private static final String RESOURCE_DIRECTORY = "data\\";
	private static final String PACKAGE_NAME = ResourceFactory.class.getPackage().getName();
	
	private VFS fileSystem;
	
	private class ResourceReader extends DefaultHandler {
		
		private final static String ROOT_NAME = "resource";
		
		private Resource resource;
		private String currentElement;
		
		public Resource getResource() {
			return resource;
		}
		
		public void read(String resourceFile) {
			XMLReader reader = null;
			FileReader fileReader = null;
			try {
				reader = XMLReaderFactory.createXMLReader();
				reader.setContentHandler(this);
				fileReader = new FileReader(resourceFile);
				reader.parse(new InputSource(fileReader));
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fileReader != null) fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void startElement(String uri, String localName, 
				String qName, Attributes attr) throws SAXException {
			if (!ROOT_NAME.equals(localName)) {
				currentElement = qName;
			} else {
				createResource(attr.getValue("name"));
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			currentElement = null;
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
		}

		private void createResource(String resourceName) {
			try {
				resource = (Resource) Class.forName(PACKAGE_NAME + "." + resourceName).newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private ResourceFactory() {
		fileSystem = new VFSImpl(RESOURCE_DIRECTORY);
	}
	
	public static ResourceFactory instance() {
		if (resourceFactory == null) {
			resourceFactory = new ResourceFactory();
		}
		return resourceFactory;
	}
	
	public Resource get(String resourceFile) throws ResourceNotExistException {
		if (!fileSystem.isExist(resourceFile)) {
			throw new ResourceNotExistException("Resource " + resourceFile + " was not found!");
		}
		ResourceReader reader = new ResourceReader();
		reader.read(fileSystem.getAbsolutePath(resourceFile));
		return null;
	}
	
}
