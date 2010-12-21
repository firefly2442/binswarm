import java.util.UUID;
import java.util.Vector;


public class Networking
{
	//all known computers in the network running this app
	public Vector<Computer> computers = new Vector<Computer>();
	
	public Networking()
	{
		//Constructor
		UDPListener listener = new UDPListener();
		UDPBroadcast broadcast = new UDPBroadcast();
	}
	
	public void addComputer(UUID uuid, String IPAddress)
	{
		computers.add(new Computer(uuid, IPAddress));
	}
	
	public void removeComputer(UUID uuid)
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
}
