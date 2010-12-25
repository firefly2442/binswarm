import java.util.Date;
import java.util.UUID;


public class Computer
{
	public UUID uuid;
	public String IPAddress;
	public long dataStored; //in bytes
	public long availableStorage; //in bytes
	public long timeStamp; //the last time we saw this computer
	
	private Date date_object;
	
	public Computer(UUID id, String IP)
	{
		//Constructor
		uuid = id;
		IPAddress = IP;
		date_object = new Date();
		updateTimeStamp();
	}
	
	public void updateTimeStamp()
	{
		timeStamp = date_object.getTime();
	}
}
