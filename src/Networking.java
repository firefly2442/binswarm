import java.util.UUID;
import java.util.Vector;


public class Networking
{
	//all known computers in the network running this app
	public static Vector<Computer> computers = new Vector<Computer>();
	
	public Networking()
	{
		//Constructor
		UDPListener listener = new UDPListener();
		UDPBroadcast broadcast = new UDPBroadcast();
	}
	
	public static void addComputer(UUID uuid, String IPAddress)
	{
		computers.add(new Computer(uuid, IPAddress));
	}
	
	public static void removeComputer(UUID uuid)
	{
		for (int i = 0; i < computers.size(); i++)
		{
			if (computers.get(i).uuid == uuid)
			{
				computers.remove(i);
				break;
			}
		}
	}
	
	public static boolean computerInList(UUID uuid)
	{
		//Check to see if a computer is already in our listing
		for (int i = 0; i < computers.size(); i++)
		{
			if (computers.get(i).uuid.equals(uuid))
			{
				return true;
			}
		}
		return false;
	}
}
