package xpathTest;

import javax.xml.xpath.XPathExpressionException;

import robotParser.GXMLparser;

public class XpathTest {

	public static void main(String[] args) 
	{
		long startTime = System.currentTimeMillis();
		// create a new GXML parser
		GXMLparser parser = new GXMLparser("src/xpathTest/gxmlExport.xml");
		
		// parse some stuff
//		System.out.println(parser.parsePID("IntakeLift/PID"));
//		System.out.println(parser.parseMotor("IntakeLift/motor"));
//		System.out.println(parser.parseEncoder("IntakeLift/encoder"));
//		System.out.println(parser.parseLimit("IntakeLift/limit"));
		
//		System.out.println(parser.parseDriveSide("DriveTrain/leftSide"));
		
//		System.out.println(parser.parseTankCascade("DriveTrain"));
		
		try {
			System.out.println(parser.parseSetpoint("IntakeLift/setpoints", "maxDown"));
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		for(int i=0; i<10000; i++)
			parser.parseTankCascade("DriveTrain");
		System.out.println(System.currentTimeMillis() - startTime);
	}
}


























