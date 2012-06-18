package binswarm;

import java.util.logging.Level;

import binswarm.config.LocationSettings;
import binswarm.config.Preferences;
import binswarm.ui.GUI;
import binswarm.ui.TrayGUI;

public class Binswarm {
	public static final String VERSION = "0.1";

	public static void main(String[] args) {
		Log.log("Binswarm version: " + VERSION + " started.", Level.INFO);
		Preferences prefs = new Preferences();
		LocationSettings locations = new LocationSettings();
		GUI theGUI = new GUI();
		TrayGUI trayGUI = new TrayGUI();
		Networking theNetwork = new Networking();
	}
}
