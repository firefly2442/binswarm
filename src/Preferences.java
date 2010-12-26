import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
			}
			catch (Exception e) {
				  System.err.println("Error: " + e.getMessage());
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
			
			//grab the elements in preferences
			Element docEle = dom.getDocumentElement();
			System.out.println("here");
			
			NodeList nl = docEle.getElementsByTagName("Preferences");
			for (int i = 0; i < nl.getLength(); i++)
			{
				//get the element
				Element el = (Element)nl.item(i);
				System.out.println("element: " + el.getNodeValue());
			}
			
		}
		catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void saveXMLFile()
	{
		
	}
}
