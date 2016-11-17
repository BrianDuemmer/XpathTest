package robotParser;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This class serves as a parser for GXML configuration files saved by the LabVIEW dashboard.
 * 
 * @author Brian Duemmer
 *
 */
public class GXMLparser 
{
	// References for the active document and XPath engine
	private Document doc;
	private XPath xpath;
	
	// Types that can be parsed in GXML
	public enum BASIC_TYPE
	{
		DOUBLE,
		INT,
		STRING,
		BOOL,
		ENUM
	}
	
	
	
	/**
	 * Creates a new instance of of the parser.
	 * 
	 * <i> NOTE:<i/> There should only be one GXMLparser for
	 * each XML file
	 * @param path the path to the target XML file
	 */
	public GXMLparser(String path)
	{
		try
		{
			// initialize the document
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(new File(path));
			
			//initialize xpath
			XPathFactory xpf = XPathFactory.newInstance();
			xpath = xpf.newXPath();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Print a string out
	 * @param str
	 */
	private void printString(String str)
	{
		System.out.println(str);
	}
	
	
	
	
	
	/**
	 * Gets an atomic element value given the direct parent and the name of the element.

	 * @return An Object containing the data found by the search, or null if not found.
	 * Add a cast statement to cast it to whatever type that was parsed
	 * 
	 */
	public Object getElementByName(String name, Node parent, BASIC_TYPE type)
	{
		// Child Node
		Node child = parent.getFirstChild();
		
		// Type value
		String typeVal = null;
		
		// Data value
		String dataVal = null;
		
		// Return value
		Object ret;
		
		do
		{
			// if it is an element node, and the name is correct, proceed
			if(child.getNodeType() == Node.ELEMENT_NODE && name.equals(child.getNodeName()))
			{
				// Get the type and break out of the loop
				typeVal = child.getAttributes().getNamedItem("type").getTextContent();
				break;
			}
			
			child = child.getNextSibling();
		}   
		while(child != null);
		
		// proceed only if the type attribute was found on a matching element
		if(typeVal != null)
		{
			// get the data
			dataVal = child.getTextContent();
			
			// Parse it as the proper value
			if(typeVal.equals("DBL") || typeVal.equals("SGL") || typeVal.equals("EXT") && type == BASIC_TYPE.DOUBLE)
			{
				ret = new Double(dataVal);
				return ret;
			}
			
			
			if	(	typeVal.equals("U8") || 
					typeVal.equals("U16") || 
					typeVal.equals("U32") || 
					typeVal.equals("U64") || 
					typeVal.equals("I8") || 
					typeVal.equals("I16") || 
					typeVal.equals("I32") || 
					typeVal.equals("I64") &&
					type == BASIC_TYPE.INT
				)
			{
				ret = new Integer(dataVal);
				return ret;
			}
			
			
			if(typeVal.equals("String") || typeVal.equals("Path") && type == BASIC_TYPE.STRING)
			{
				return dataVal;
			}
			
			
			if(typeVal.equals("Bool") && type == BASIC_TYPE.BOOL)
			{
				ret = new Boolean(dataVal);
				return ret;
			}
			
			
			if(typeVal.equals("Enum U8") || typeVal.equals("Enum U16") || typeVal.equals("Enum U32") && type == BASIC_TYPE.ENUM)
			{
				ret = child.getAttributes().getNamedItem("sel").getTextContent();
				ret += "___" + dataVal;
				return ret;
			}
		}
		
		// if nothing is found, return null
		return null;
	}
	
	
	
	
	
	/**
	 * Parses a PID loop from the document, and returns a PIDData object containing the data
	 * @param path the path to the PID Element in the document, without the root "GXML" element
	 * @return a data element containing the PID data
	 */
	public PIDData parsePID(String path)
	{
		// PIDData element
		PIDData data = new PIDData(0,0,0,0,0,0);
		
		// Expression to find the PID node
		XPathExpression pidExp;
		
		//PID node
		Node pidNode;
		
		// Try to compile the expression and find the PID node. If it fails, return null and print a warning
		try 
		{
			pidExp = xpath.compile("GXML/" + path);
			pidNode = (Node) pidExp.evaluate(doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			printString("Error: expression \"" + path + "\" failed to compile. DETAILS:");
			e.printStackTrace();
			return null;
		}
		
		// Make sure that the node is found
		if(pidNode == null)
		{
			printString("Failed to find a PID with path \"" + path + "\"");
			return null;
		}
		
		// parse out the elements
		data.kp = (Double)getElementByName("kp", pidNode, BASIC_TYPE.DOUBLE);
		data.ki = (Double)getElementByName("ki", pidNode, BASIC_TYPE.DOUBLE);
		data.kd = (Double)getElementByName("kd", pidNode, BASIC_TYPE.DOUBLE);
		data.kf = (Double)getElementByName("kf", pidNode, BASIC_TYPE.DOUBLE);
		data.period = (Double)getElementByName("period", pidNode, BASIC_TYPE.DOUBLE);
		data.tolerance = (Double)getElementByName("tolerance", pidNode, BASIC_TYPE.DOUBLE);
		
		// return the parsed values
		return data;
	}
}

















