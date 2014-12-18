package mihnayan.divetojava.resourcesystem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import mihnayan.divetojava.frontend.GameFrontend;
import mihnayan.divetojava.utils.ReflectionHelper;
import mihnayan.divetojava.utils.VFS;
import mihnayan.divetojava.utils.VFSImpl;

/**
 * Singleton 
 * @author Mikhail Mangushev
 *
 */
public class ResourceFactory {
	
	private static Logger log = Logger.getLogger(ResourceFactory.class.getName());
	
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
			log.info("Reading resource " + resourceFile);
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
				createResource(attr.getValue("className"));
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			currentElement = null;
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (currentElement != null) {
				String value = new String(ch, start, length);
				ReflectionHelper.setFieldValue(resource, currentElement, value);
			}
		}

		private void createResource(String resourceName) {
			resource = 
					(Resource) ReflectionHelper.createInstance(PACKAGE_NAME + "." + resourceName);
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
