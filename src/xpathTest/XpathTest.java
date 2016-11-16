package xpathTest;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XpathTest {

	public static void main(String[] args) {
		
		try
		{
			// initialize the document
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File("src/xpathTest/gxmlExport.xml"));
			
			//initialize xpath
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			
			// print the PID for the intakeLiftSubsystem
			printSubsysPID(xpath, doc, "IntakeLift", "PID");
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void printSubsysPID(XPath xpath, Document doc, String subsys, String pidName)
	{
		try
		{
			// create an expression to find the PID node
			XPathExpression pidExpr = xpath.compile("*/" + subsys + "/" + pidName);
			
			// find the PID node
			Node PIDnode = (Node) pidExpr.evaluate(doc, XPathConstants.NODE);
			
			// if nothing is found, return
			if(PIDnode == null)
			{
				System.out.println("There is no PID named \"" + pidName + "\" in the subsystem \"" + subsys + "\"");
				return;
			}
			
			// get the first child
			Node pidChild = PIDnode.getFirstChild();
			
			// print out the information
			System.out.println("PID values are:\n");
			do
			{
				if(pidChild.getNodeType() == Node.ELEMENT_NODE)
				{
					String name = pidChild.getNodeName();
					String val = pidChild.getTextContent();
					
					System.out.println(name + " = " + val);
					System.out.println(pidChild.getAttributes().getNamedItem("type").getTextContent());
				}
				
				pidChild = pidChild.getNextSibling();
			}   
			while(pidChild != null);
				
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}


























