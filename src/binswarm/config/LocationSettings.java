package binswarm.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

public class LocationSettings {
	final String SETTING_PATH = "Path"; //location of files or where files will be stored
	final String SETTING_SIZE_LIMIT = "SizeLimit"; //how many bytes can be stored in the sink
	final String SETTING_BACKUP = "BackupNumber"; //how many places the files should be backed up

	public static ArrayList<Source> source_locations = new ArrayList<Source>();
	public static ArrayList<Sink> sink_locations = new ArrayList<Sink>();
	
	public class Location {
		public String path;
		public Location(String location_path) {
			//constructor
			path = location_path;
		}
	}
	
	public class Sink extends Location {
		public int size_limit;
		public Sink(String location_path, int size_limit_bytes) {
			//constructor
			super(location_path);
			size_limit = size_limit_bytes;
		}
		
		public void print() {
			System.out.println("Sink:");
			System.out.println("Path: " + path);
			System.out.println("Size limit bytes: " + size_limit);
		}
	}
	
	public class Source extends Location {
		public int backup_locations;
		public Source(String location_path, int number_times_backedup) {
			//constructor
			super(location_path);
			backup_locations = number_times_backedup;
		}
		
		public void print() {
			System.out.println("Source:");
			System.out.println("Path: " + path);
			System.out.println("Backup number: " + backup_locations);
		}
	}


	public LocationSettings() {
		// Constructor
		File locationsettings = new File("locationsettings.xml");
		if (!locationsettings.exists()) {
			Log.log("Creating new locationsettings.xml file", Level.INFO);
			saveXMLFile();
		} else {
			Log.log("Loading locationsettings.xml file", Level.INFO);
			loadXMLFile();
		}
		
		//debugging, print sink and source locations
		/*for (int i = 0; i < source_locations.size(); i++) {
			source_locations.get(i).print();
		}
		for (int i = 0; i < sink_locations.size(); i++) {
			sink_locations.get(i).print();
		}*/
	}

	private void loadXMLFile() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse("locationsettings.xml");
			
			String path_loc = "";
			int backup_number = 0;
			int size_limit = 0;

			NodeList nl = dom.getElementsByTagName("Source");
			for (int i = 0; i < nl.getLength(); i++) {
				Node firstnode = nl.item(i);
				if (firstnode.getNodeType() == Node.ELEMENT_NODE) {
					Element singleElement = (Element) firstnode;
					
					NodeList theChildren = singleElement.getChildNodes();

					for (int j = 0; j < theChildren.getLength(); j++) {
						// if the child is an element
						if (theChildren.item(j).getNodeType() == Node.ELEMENT_NODE) {
							singleElement = (Element) theChildren.item(j);
									
							// if it's an expected name
							if (singleElement.getNodeName().equals(SETTING_PATH)) {
								path_loc = XmlHelper.ParseString(singleElement);
							} else if (singleElement.getNodeName().equals(SETTING_BACKUP)) {
								backup_number = XmlHelper.ParseInt(singleElement);
							} else {
								Log.log("Found unexpected 'Source' element while loading locationsettings.xml file. elementName=" + singleElement.getNodeName(), Level.INFO);
							}
						}
					}
					
					//create and add the source location
					Source temp_source = new Source(path_loc, backup_number);
					source_locations.add(temp_source);			
				}
			}
			
			nl = dom.getElementsByTagName("Sink");
			for (int i = 0; i < nl.getLength(); i++) {
				Node firstnode = nl.item(i);
				if (firstnode.getNodeType() == Node.ELEMENT_NODE) {
					Element singleElement = (Element) firstnode;
					
					NodeList theChildren = singleElement.getChildNodes();

					for (int j = 0; j < theChildren.getLength(); j++) {
						// if the child is an element
						if (theChildren.item(j).getNodeType() == Node.ELEMENT_NODE) {
							singleElement = (Element) theChildren.item(j);
							
							// if it's an expected name
							if (singleElement.getNodeName().equals(SETTING_PATH)) {
								path_loc = XmlHelper.ParseString(singleElement);
							} else if (singleElement.getNodeName().equals(SETTING_SIZE_LIMIT)) {
								size_limit = XmlHelper.ParseInt(singleElement);
							} else {
								Log.log("Found unexpected 'Sink' element while loading locationsettings.xml file. elementName=" + singleElement.getNodeName(), Level.INFO);
							}
						}
					}
					
					//create and add the sink location
					Sink temp_sink = new Sink(path_loc, size_limit);
					sink_locations.add(temp_sink);
				}
			}
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		Log.log("Finished loading locationsettings.xml file", Level.INFO);
	}

	public void saveXMLFile() {
		try {
			FileWriter fstream = new FileWriter("locationsettings.xml");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("<?xml version='1.0' encoding='UTF-8'?>\n");
			out.write("<LocationSettings>\n");

			for (int i = 0; i < source_locations.size(); i++) {
				out.write("<Source>\n");
				out.write(XmlHelper.FormatElement(SETTING_PATH, source_locations.get(i).path.toString()));
				out.write(XmlHelper.FormatElement(SETTING_BACKUP, Integer.toString(source_locations.get(i).backup_locations)));
				out.write("</Source>\n");
			}
			
			for (int i = 0; i < sink_locations.size(); i++) {
				out.write("<Sink>\n");
				out.write(XmlHelper.FormatElement(SETTING_PATH, sink_locations.get(i).path.toString()));
				out.write(XmlHelper.FormatElement(SETTING_SIZE_LIMIT, Integer.toString(sink_locations.get(i).size_limit)));
				out.write("</Sink>\n");
			}

			out.write("</LocationSettings>\n");

			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.log("Finished saving locationsettings.xml file", Level.INFO);
	}
}
