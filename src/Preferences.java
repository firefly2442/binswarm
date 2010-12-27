import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Preferences
{
	public static UUID uuid;
	public static int UDPStatusPort;
	public static int TCPDataPort;
	public static int BroadcastInterval; //time (in seconds)
	public static int CountDowntoRemoval; //time (in seconds)
	public static boolean LogToFile;
	public static boolean PrintToConsole;
	
	public Preferences()
	{
		//Constructor
		File prefs = new File("preferences.xml");
		if (!prefs.exists())
		{
			//create new file with defaults
			try
			{
				FileWriter fstream = new FileWriter("preferences.xml");
				BufferedWriter out = new BufferedWriter(fstream);
				out.write("<?xml version='1.0' encoding='UTF-8'?>\n");
				out.write("<Settings>\n");
					out.write("<Preferences>\n");
						out.write("<UUID>\n");
							//generate UUID since this is the first time the program has been run
							uuid = UUID.randomUUID();
							out.write(uuid.toString() + "\n");
						out.write("</UUID>\n");
						out.write("<UDPStatusPort\n>");
							UDPStatusPort = 2500;
							out.write(UDPStatusPort + "\n");
						out.write("</UDPStatusPort\n>");
						out.write("<TCPDataPort\n>");
							TCPDataPort = 2501;
							out.write(TCPDataPort + "\n");
						out.write("</TCPDataPort\n>");
						out.write("<BroadcastInterval>\n");
							//time (in seconds) between sending UDP status messages
							BroadcastInterval = 30;
							out.write(BroadcastInterval + "\n");
						out.write("</BroadcastInterval>\n");
						out.write("<CountDowntoRemoval>\n");
							//time (in seconds) before we remove a computer from our listing
							//after not getting a UDP status message from it
							CountDowntoRemoval = 180;
							out.write(CountDowntoRemoval + "\n");
						out.write("</CountDowntoRemoval>\n");
						out.write("<LogToFile>\n");
							//whether or not the logger saves output to file
							LogToFile = true;
							out.write("true\n");
						out.write("</LogToFile>\n");
						out.write("<PrintToConsole>\n");
							//whether or not the logger prints output to console
							PrintToConsole = true;
							out.write("true\n");
						out.write("</PrintToConsole>\n");
					out.write("</Preferences>\n");
				out.write("</Settings>\n");
				
				out.close();
				Log.log("New preferences.xml file created", Level.INFO);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		loadXMLFile();
	}
	
	private void loadXMLFile()
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse("preferences.xml");
			
			NodeList nl = dom.getElementsByTagName("Preferences");
			for (int i = 0; i < nl.getLength(); i++)
			{
				Node firstnode = nl.item(i);
				if (firstnode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element)firstnode;
					NodeList ElementList;
					Element singleElement;
					NodeList theChildrenElements;
					
					ElementList = element.getElementsByTagName("UUID");
					singleElement = (Element)ElementList.item(0);
					theChildrenElements = singleElement.getChildNodes();
					uuid = UUID.fromString(((Node)theChildrenElements.item(0)).getNodeValue().trim());
					
					ElementList = element.getElementsByTagName("UDPStatusPort");
					singleElement = (Element)ElementList.item(0);
					theChildrenElements = singleElement.getChildNodes();
					UDPStatusPort = Integer.parseInt(((Node)theChildrenElements.item(0)).getNodeValue().trim());
					
					ElementList = element.getElementsByTagName("TCPDataPort");
					singleElement = (Element)ElementList.item(0);
					theChildrenElements = singleElement.getChildNodes();
					TCPDataPort = Integer.parseInt(((Node)theChildrenElements.item(0)).getNodeValue().trim());
					
					ElementList = element.getElementsByTagName("BroadcastInterval");
					singleElement = (Element)ElementList.item(0);
					theChildrenElements = singleElement.getChildNodes();
					BroadcastInterval = Integer.parseInt(((Node)theChildrenElements.item(0)).getNodeValue().trim());
					
					ElementList = element.getElementsByTagName("CountDowntoRemoval");
					singleElement = (Element)ElementList.item(0);
					theChildrenElements = singleElement.getChildNodes();
					CountDowntoRemoval = Integer.parseInt(((Node)theChildrenElements.item(0)).getNodeValue().trim());
					
					ElementList = element.getElementsByTagName("LogToFile");
					singleElement = (Element)ElementList.item(0);
					theChildrenElements = singleElement.getChildNodes();
					LogToFile = Boolean.valueOf(((Node)theChildrenElements.item(0)).getNodeValue().trim());
					
					ElementList = element.getElementsByTagName("PrintToConsole");
					singleElement = (Element)ElementList.item(0);
					theChildrenElements = singleElement.getChildNodes();
					PrintToConsole = Boolean.valueOf(((Node)theChildrenElements.item(0)).getNodeValue().trim());
				}
			}
			
		}
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		catch (SAXException se) {
			se.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		Log.log("Finished loading preferences.xml file", Level.INFO);
	}
	
	public void saveXMLFile()
	{
		
	}
}
