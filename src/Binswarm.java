
public class Binswarm
{
	public static final String VERSION = "0.1";
	
	public static void main(String[] args)
	{
		Preferences prefs = new Preferences();
		GUI theGUI = new GUI();
		Networking theNetwork = new Networking();

		//TODO: this should be in preferences
		Logger.log_to_file = false;
		Logger.print_to_console = true;
	}
}
