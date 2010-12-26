import java.util.Calendar;


public class Logger
{
	public static boolean log_to_file;
	public static boolean print_to_console;
	private static Calendar cal = Calendar.getInstance();
	
	public Logger()
	{
		//Constructor - no-op
	}
	
	
	
	public static void log(String message)
	{
		if (log_to_file)
		{
			
		}
		else
		{
			
		}
		
		if (print_to_console)
		{
			System.out.println(cal.get(Calendar.MONTH)+1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR) + " - " + message);
		}
		else
		{
			
		}
	}
}
