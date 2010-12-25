import java.util.Calendar;


public class Logger
{
	private static boolean log_to_file;
	private static boolean print_to_console;
	private static Calendar cal;
	
	public Logger(boolean log_file, boolean print_console)
	{
		log_to_file = log_file;
		print_to_console = print_console;
		cal = Calendar.getInstance();
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
