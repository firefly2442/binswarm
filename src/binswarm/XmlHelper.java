package binswarm;

import java.util.UUID;
import java.util.logging.Level;

import org.w3c.dom.Element;

public class XmlHelper {

	public static String ParseString(Element e) {
		String value = null;
		try {
			value = e.getTextContent().trim();
		} catch (NumberFormatException ex) {
			Log.log("Preferences.loadXMLFile(): An Exception occured while parsing the string '"
					+ e.getNodeName() + "'. value=" + e.getTextContent().trim(),
					Level.INFO);
		}
		return value;
	}

	public static int ParseInt(Element e) {
		int value = 0;
		try {
			value = Integer.parseInt(e.getTextContent().trim());
		} catch (NumberFormatException ex) {
			Log.log("Preferences.loadXMLFile(): An Exception occured while parsing the integer '"
					+ e.getNodeName() + "'. value=" + e.getTextContent().trim(),
					Level.INFO);
		}
		return value;
	}

	public static boolean ParseBoolean(Element e) {
		boolean value = false;
		try {
			value = Boolean.parseBoolean(e.getTextContent().trim());
		} catch (Exception ex) {
			Log.log("Preferences.loadXMLFile(): An Exception occured while parsing the boolean '"
					+ e.getNodeName() + "'. value=" + e.getTextContent().trim(),
					Level.INFO);
		}
		return value;
	}

	public static UUID ParseUUID(Element e) {
		UUID uuid = null;
		try {
			uuid = UUID.fromString(e.getTextContent().trim());
		} catch (IllegalArgumentException ex) {
			Log.log("Preferences.loadXMLFile(): An Exception occured while parsing the uuid '"
					+ e.getNodeName() + "'. value=" + e.getTextContent().trim(),
					Level.INFO);
		}
		return uuid;
	}

	public static String FormatElement(String name, String value) {
		return String.format("<%1$s>%2$s</%1$s>\n", name, value);
	}
}
