import java.util.Calendar;


public class Logger
{
	private static Calendar cal = Calendar.getInstance();
	
	public Logger()
	{
		//Constructor - no-op
	}
	
	
	
	public static void log(String message)
	{
		if (Preferences.LogToFile)
		{
			
		}
		else
		{
			
		}
		
		if (Preferences.PrintToConsole)
		{
			System.out.println(cal.get(Calendar.MONTH)+1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR) + " - " + message);
		}
		else
		{
			
		}
	}
}
