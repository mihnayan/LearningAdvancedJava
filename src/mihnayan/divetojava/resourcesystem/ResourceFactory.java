package mihnayan.divetojava.resourcesystem;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import mihnayan.divetojava.utils.ReflectionHelper;
import mihnayan.divetojava.utils.VFS;
import mihnayan.divetojava.utils.VFSImpl;

/**
 * Singleton class that contains loader of resources.
 * @author Mikhail Mangushev
 */
public final class ResourceFactory {

    // private static Logger log =
    // Logger.getLogger(ResourceFactory.class.getName());

    private static ResourceFactory resourceFactory;
    private static final String RESOURCE_DIRECTORY = "data\\";
    private static final String RESOURCE_LIST_FILE = "ResourceConfig.xml";

    private static final String PACKAGE_NAME = ResourceFactory.class
            .getPackage().getName();

    private List<String> resourceList = new ArrayList<String>();
    private Map<Class<? extends Resource>, Resource> resources =
            new HashMap<Class<? extends Resource>, Resource>();

    private VFS fileSystem;

    /**
     * SAX2 handler class for reading resource parameters.
     * @see org.xml.sax.helpers.DefaultHandler
     * @author Mikhail Mangushev (Mihnayan)
     */
    private class ResourceReader extends DefaultHandler {

        private static final String ROOT_NAME = "resource";

        private Resource resource;
        private String currentElement;

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attr) throws SAXException {
            if (!ROOT_NAME.equals(localName)) {
                currentElement = qName;
            } else {
                try {
                    createResource(attr.getValue("className"));
                } catch (ResourceLoadingException e) {
                    throw new SAXException(e);
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
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

        private void createResource(String resourceName) throws ResourceLoadingException {
            resource = (Resource) ReflectionHelper.createInstance(PACKAGE_NAME
                    + "." + resourceName);
            if (resource != null) {
                resources.put(resource.getClass(), resource);
            } else {
                throw new ResourceLoadingException("Can't create resource '" + resourceName + "'!");
            }
        }

    }

    /**
     * SAX2 handler class for loading list of resources.
     * @see org.xml.sax.helpers.DefaultHandler
     * @author Mikhail Mangushev (Mihnayan)
     */
    private class ResourceListLoader extends DefaultHandler {

        private static final String RESOURCE_TAG_NAME = "resource-file";

        private String currentElement;

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attr) throws SAXException {
            if (RESOURCE_TAG_NAME.equals(localName)) {
                currentElement = qName;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            currentElement = null;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if (currentElement != null) {
                String value = new String(ch, start, length);
                resourceList.add(value);
            }
        }
    }

    private ResourceFactory() {
        fileSystem = new VFSImpl(RESOURCE_DIRECTORY);
    }

    /**
     * @return ResourceFactory instance.
     */
    public static ResourceFactory instance() {
        if (resourceFactory == null) {
            resourceFactory = new ResourceFactory();
        }
        return resourceFactory;
    }

    /**
     * Loads all resources that describes in file.
     * @throws ResourceLoadingException occurs if was there any error when loading resource.
     */
    //TODO: File containing list of resources must be passed as a method parameter
    public void loadResources() throws ResourceLoadingException {
        DefaultHandler handler = new ResourceListLoader();
        readXMLResource(fileSystem.getAbsolutePath(RESOURCE_LIST_FILE), handler);

        handler = new ResourceReader();
        for (String resourceFile : resourceList) {
            readXMLResource(fileSystem.getAbsolutePath(resourceFile), handler);
        }
    }

    /**
     * Returns Resource by resource class.
     * @param resourceClass The class of resource that must be returned.
     * @return Resource object of resourceClass class.
     * @throws ResourceNotExistException occurs if requested resource is not exist.
     */
    public Resource get(Class<? extends Resource> resourceClass)
            throws ResourceNotExistException {
        if (!resources.containsKey(resourceClass)) {
            throw new ResourceNotExistException("Resource "
                    + resourceClass.getName() + " was not found!");
        }
        return resources.get(GameSessionResource.class);
    }

    private void readXMLResource(String resourceFile, DefaultHandler handler)
            throws ResourceLoadingException {
        XMLReader reader = null;
        FileReader fileReader = null;
        try {
            reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            fileReader = new FileReader(resourceFile);
            reader.parse(new InputSource(fileReader));
        } catch (SAXException | IOException e) {
            throw new ResourceLoadingException(e);
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
