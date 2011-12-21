package binswarm.config;

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

import binswarm.Log;
import binswarm.XmlHelper;

public class Preferences {
	final String PREF_UUID = "UUID";
	final String PREF_UDPStatusPort = "UDPStatusPort";
	final String PREF_TCPDataPort = "TCPDataPort";
	final String PREF_BroadcastInterval = "BroadcastInterval";
	final String PREF_CountDowntoRemoval = "CountDowntoRemoval";
	final String PREF_LogToFile = "LogToFile";
	final String PREF_PrintToConsole = "PrintToConsole";
	final String PREF_MulticastGroupAddress = "MulticastGroupAddress";
	final String PREF_MulticastGroupPort = "MulticastGroupPort";
	final String PREF_UseUDPBroadcast = "UseUDPBroadcast";

	public static UUID uuid;
	public static int UDPStatusPort;
	public static int TCPDataPort;
	public static int BroadcastInterval; // time (in seconds)
	public static int CountDowntoRemoval; // time (in seconds)
	public static boolean LogToFile;
	public static boolean PrintToConsole;
	public static String MulticastGroupAddress;
	public static int MulticastGroupPort;

	// set to either use UDP broadcast (true) or multicast (false)
	public static boolean UseUDPBroadcast;

	public Preferences() {
		setDefaults();
		// Constructor
		File prefs = new File("preferences.xml");
		if (!prefs.exists()) {
			Log.log("Creating new preferences.xml file", Level.INFO);
			this.saveXMLFile();
		} else {
			loadXMLFile();
		}
	}

	private void setDefaults() {
		// set defaults
		// generate UUID since this is the first time the program has been run
		uuid = UUID.randomUUID();
		UDPStatusPort = 2500;
		TCPDataPort = 2501;
		// time (in seconds) between sending UDP status messages
		BroadcastInterval = 30;
		// time (in seconds) before we remove a computer from our listing
		// after not getting a UDP status message from it
		CountDowntoRemoval = 180;
		// whether or not the logger saves output to file
		LogToFile = true;
		// whether or not the logger prints output to console
		PrintToConsole = true;
		// FROM:
		// http://download.oracle.com/javase/6/docs/api/java/net/MulticastSocket.html
		// A multicast group is specified by a class D IP address and by a
		// standard
		// UDP port number. Class D IP addresses are in the range 224.0.0.0 to
		// 239.255.255.255, inclusive. The address 224.0.0.0 is reserved and
		// should not be used.
		MulticastGroupAddress = "224.0.0.1"; // This will send to ALL hosts on
												// the subnet
		MulticastGroupPort = 1337;
		// Set whether we are using UDP broadcast or multicast
		UseUDPBroadcast = true;
	}

	private void loadXMLFile() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse("preferences.xml");

			NodeList nl = dom.getElementsByTagName("Preferences");
			for (int i = 0; i < nl.getLength(); i++) {
				Node firstnode = nl.item(i);
				if (firstnode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) firstnode;
					Element singleElement;
					// get all children
					NodeList theChildren = element.getChildNodes();

					for (int j = 0; j < theChildren.getLength(); j++) {
						// if the child is an element
						if (theChildren.item(j).getNodeType() == Node.ELEMENT_NODE) {
							singleElement = (Element) theChildren.item(j);

							// if it's an expected name
							if (singleElement.getNodeName().equals(
									this.PREF_UUID)) {
								UUID pUUID = XmlHelper.ParseUUID(singleElement);
								if (pUUID != null) // if it's null keep value
													// set using setDefaults()
									uuid = pUUID;
							} else if (singleElement.getNodeName().equals(
									this.PREF_UDPStatusPort)) {
								int pUDPStatusPort = XmlHelper
										.ParseInt(singleElement);
								if (pUDPStatusPort != 0) // if it's 0 keep value
															// set using
															// setDefaults()
									UDPStatusPort = pUDPStatusPort;
							} else if (singleElement.getNodeName().equals(
									this.PREF_TCPDataPort)) {
								int pTCPDataPort = XmlHelper
										.ParseInt(singleElement);
								if (pTCPDataPort != 0) // if it's 0 keep value
														// set using
														// setDefaults()
									TCPDataPort = pTCPDataPort;
							} else if (singleElement.getNodeName().equals(
									this.PREF_BroadcastInterval)) {
								int pBroadcastInterval = XmlHelper
										.ParseInt(singleElement);
								if (pBroadcastInterval != 0) // if it's 0 keep
																// value set
																// using
																// setDefaults()
									BroadcastInterval = pBroadcastInterval;
							} else if (singleElement.getNodeName().equals(
									this.PREF_CountDowntoRemoval)) {
								int pCountDowntoRemoval = XmlHelper
										.ParseInt(singleElement);
								if (pCountDowntoRemoval != 0) // if it's 0 keep
																// value set
																// using
																// setDefaults()
									CountDowntoRemoval = pCountDowntoRemoval;
							} else if (singleElement.getNodeName().equals(
									this.PREF_LogToFile)) {
								LogToFile = XmlHelper
										.ParseBoolean(singleElement);
							} else if (singleElement.getNodeName().equals(
									this.PREF_PrintToConsole)) {
								PrintToConsole = XmlHelper
										.ParseBoolean(singleElement);
							} else if (singleElement.getNodeName().equals(
									this.PREF_MulticastGroupAddress)) {
								String pMulticastGroupAddress = XmlHelper
										.ParseString(singleElement);
								if (pMulticastGroupAddress != null) // if it's
																	// null keep
																	// value set
																	// using
																	// setDefaults()
									MulticastGroupAddress = pMulticastGroupAddress;
							} else if (singleElement.getNodeName().equals(
									this.PREF_MulticastGroupPort)) {
								int pMulticastGroupPort = XmlHelper
										.ParseInt(singleElement);
								if (pMulticastGroupPort != 0) // if it's 0 keep
																// value set
																// using
																// setDefaults()
									MulticastGroupPort = pMulticastGroupPort;
							} else if (singleElement.getNodeName().equals(
									this.PREF_UseUDPBroadcast)) {
								UseUDPBroadcast = XmlHelper
										.ParseBoolean(singleElement);
							} else {
								Log.log("Found unexpected element while loading preferences.xml file. elementName="
										+ singleElement.getNodeName(),
										Level.INFO);
							}
						}
					}
				}
			}

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		Log.log("Finished loading preferences.xml file", Level.INFO);
	}

	public void saveXMLFile() {
		try {
			FileWriter fstream = new FileWriter("preferences.xml");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("<?xml version='1.0' encoding='UTF-8'?>\n");
			out.write("<Settings>\n");
			out.write("<Preferences>\n");
			// generate UUID since this is the first time the program has been
			// run
			// uuid = UUID.randomUUID();
			out.write(XmlHelper.FormatElement(PREF_UUID, uuid.toString()));
			// UDPStatusPort = 2500;
			out.write(XmlHelper.FormatElement(PREF_UDPStatusPort,
					Integer.toString(UDPStatusPort)));
			// TCPDataPort = 2501;
			out.write(XmlHelper.FormatElement(PREF_TCPDataPort,
					Integer.toString(TCPDataPort)));
			// time (in seconds) between sending UDP status messages
			// BroadcastInterval = 30;
			out.write(XmlHelper.FormatElement(PREF_BroadcastInterval,
					Integer.toString(BroadcastInterval)));
			// time (in seconds) before we remove a computer from our listing
			// after not getting a UDP status message from it
			// CountDowntoRemoval = 180;
			out.write(XmlHelper.FormatElement(PREF_CountDowntoRemoval,
					Integer.toString(CountDowntoRemoval)));
			// whether or not the logger saves output to file
			// LogToFile = true;
			out.write(XmlHelper.FormatElement(PREF_LogToFile,
					Boolean.toString(LogToFile)));
			// whether or not the logger prints output to console
			// PrintToConsole = true;
			out.write(XmlHelper.FormatElement(PREF_PrintToConsole,
					Boolean.toString(PrintToConsole)));
			out.write(XmlHelper.FormatElement(PREF_MulticastGroupAddress,
					MulticastGroupAddress));
			out.write(XmlHelper.FormatElement(PREF_MulticastGroupPort,
					Integer.toString(MulticastGroupPort)));
			// whether to use UDP broadcast or multicast
			out.write(XmlHelper.FormatElement(PREF_UseUDPBroadcast,
					Boolean.toString(UseUDPBroadcast)));
			out.write("</Preferences>\n");
			out.write("</Settings>\n");

			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.log("Finished saving preferences.xml file", Level.INFO);
	}
}
