package mihnayan.divetojava.resourcesystem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ResourceReader extends DefaultHandler {
	
//	private String resourceFile;
	
	private final static String ROOT_NAME = "resource";

	public ResourceReader() {
		super();
//		this.resourceFile = resourceFile;
	}
	
	public void read(String resourceFile) {
		XMLReader reader;
		try {
			reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(this);
			FileReader fileReader = new FileReader(resourceFile);
			reader.parse(new InputSource(fileReader));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startElement(String uri, String localName, 
			String qName, Attributes attr) throws SAXException {
		
		if (ROOT_NAME.equals(localName)) System.out.println(attr.getValue("name"));
		
		if ("".equals (uri))
		    System.out.println("Start element: " + qName);
		else
		    System.out.println("Start element: {" + uri + "}" + localName);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		if ("".equals (uri))
		    System.out.println("End element: " + qName);
		else
		    System.out.println("End element:   {" + uri + "}" + localName);
	}

}
