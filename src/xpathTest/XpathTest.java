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

import robotParser.GXMLparser;

public class XpathTest {

	public static void main(String[] args) 
	{
		// create a new GXML parser
		GXMLparser parser = new GXMLparser("src/xpathTest/gxmlExport.xml");
		System.out.println(parser.parsePID("IntakeLift/PID").toString());
		
	}
}


























