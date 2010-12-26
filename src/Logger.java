import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;


public class Logger
{
	private static Calendar cal = Calendar.getInstance();
	private static String logfile;
	private static FileWriter fstream;
	private static BufferedWriter out;
	
	public Logger()
	{
		//Constructor - no-op
	}
	
	public static void log(String message)
	{
		//check and see if the file is ready and setup yet
		if (Preferences.LogToFile && logfile == null)
		{
			try
			{
				//Setup log file for writing to
				logfile = "log_" + cal.hashCode() + ".txt";
			    fstream = new FileWriter(logfile);
			    out = new BufferedWriter(fstream);
			}
			catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
		
		String date_timestamp = "";
		//convert minutes into correct format
		String minutes = "";
		if (cal.get(Calendar.MINUTE) < 10) {
			minutes = "0";
		}
		minutes += cal.get(Calendar.MINUTE);
		//figure out AM/PM
		String AM_PM = "AM";
		if (cal.get(Calendar.AM_PM) == 1)
			AM_PM = "PM";
		
		date_timestamp = cal.get(Calendar.MONTH)+1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR) + " - " +
						cal.get(Calendar.HOUR_OF_DAY) + ":" + minutes + " " + AM_PM + " - ";
		
		if (Preferences.LogToFile)
		{
			try {
				out.write(date_timestamp + message);
			}
			catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
		
		if (Preferences.PrintToConsole)
		{
			System.out.println(date_timestamp + message);
		}
		
		//TODO: fix me
		//This isn't working, will this work with asynchronous logging and writing
		//to the file?
		try {
			out.flush(); //flush output straight to file immediately
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
