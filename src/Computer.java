import java.util.UUID;


public class Computer
{
	public UUID uuid;
	public String IPAddress;
	public long dataStored; //in bytes
	public long availableStorage; //in bytes
	
	public Computer(UUID id, String IP)
	{
		//Constructor
		uuid = id;
		IPAddress = IP;
	}
}
