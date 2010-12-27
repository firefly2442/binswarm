import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public class Log
{
	private static Calendar cal = Calendar.getInstance();
	private static String logfile;
	private static FileHandler fh;
	private static Logger logger;
	
	
	public Log()
	{
		//Constructor - no-op
	}
	
	public static void log(String message, Level leveltype)
	{
		//check and see if the file is ready and setup yet
		if (Preferences.LogToFile && logfile == null)
		{
			try
			{
				//check and see if there is a log directory, if not create one
				if (!(new File("./logs/")).exists())
					if (!(new File("./logs/")).mkdir())
						Log.log("Unable to create 'logs' directory, check permissions", Level.SEVERE);
					else
						Log.log("Creating 'logs' directory to save log files", Level.INFO);
				
				//Setup log file for writing to
				logfile = "log_" + cal.hashCode() + ".html";
			    fh = new FileHandler("./logs/" + logfile);
			    fh.setFormatter(new HTMLLogFormat());
			    logger = Logger.getLogger(logfile);
			    if (!Preferences.PrintToConsole)
			    {
			    	//this is needed to prevent the logger from defaulting to displaying messages in console
			    	logger.setUseParentHandlers(false);
			    }
			    logger.addHandler(fh);
			}
			catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
				
		if (Preferences.LogToFile)
		{
			try {
				logger.log(leveltype, message);
			}
			catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
	}
}

class HTMLLogFormat extends Formatter
{
	public String format(LogRecord rec)
	{
		StringBuffer buf = new StringBuffer(1000);
		// Bold any levels >= WARNING
		buf.append("<tr>");
		buf.append("<td>");

		if (rec.getLevel().intValue() >= Level.WARNING.intValue())
		{
			buf.append("<b>");
			buf.append(rec.getLevel());
			buf.append("</b>");
		} else
		{
			buf.append(rec.getLevel());
		}
		buf.append("</td>");
		buf.append("<td>");
		buf.append(calcDate(rec.getMillis()));
		buf.append("</td><td>\n");
		buf.append(formatMessage(rec));
		buf.append('\n');
		buf.append("</td>");
		buf.append("</tr>\n");
		return buf.toString();
	}

	private String calcDate(long millisecs)
	{
		SimpleDateFormat date_format = new SimpleDateFormat("MMM dd, yyyy HH:mm a");
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}

	public String getHead(Handler h)
	{
		return "<HTML>\n<HEAD><TITLE>BinSwarm Log File</TITLE></HEAD>\n<BODY>\n"
				+ "<table border='1'>\n"
				+ "<tr><th>Type</th><th>Time</th><th>Log Message</th></tr>\n";
	}

	public String getTail(Handler h)
	{
		return "</table>\n</BODY>\n</HTML>\n";
	}
}
