import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Level;


public class UDPListener implements Runnable
{
	public UDPListener() 
	{
		//Constructor
		Thread listenerThread = new Thread(this);
		listenerThread.start();
	}

	public void run()
	{
		Log.log("Listening for UDP packets...", Level.INFO);
		
		DatagramSocket socket;
		try {
			socket = new DatagramSocket(Preferences.UDPStatusPort);
			byte[] receiveData = new byte[128];
			
			while (true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {
					socket.receive(receivePacket);
					String received = new String(receivePacket.getData()).trim();
					StringTokenizer tokens = new StringTokenizer(received, "/");
					String version = tokens.nextToken();
					String universalID = tokens.nextToken();
					
					InetAddress IPAddress = receivePacket.getAddress();
					Log.log("Received UDP status from: " + IPAddress.toString() + " running version: " + version, Level.INFO);
					
					//if the computer is running the same version we are
					if (version.equals(Binswarm.VERSION))
					{
						//make sure we don't add messages from ourself to the listing
						if (!universalID.equals(Preferences.uuid.toString()))
						{
							//if this is a new computer that is not in our list, send a reply
							if (!Networking.computerInList(UUID.fromString(universalID)))
							{
								UDPBroadcast.sendBroadcast();
								//and create a new computer and add it to the list
								Networking.addComputer(UUID.fromString(universalID), IPAddress.toString());
							}
							else
							{
								//the computer already is in the list, so update timestamp, IP, and other information
								Networking.updateList(UUID.fromString(universalID), IPAddress.toString());
							}
						}
					}
					else
					{
						Log.log("Received UDP status from: " + IPAddress.toString() + ", but they are running a different version: (" + version + ")", Level.INFO);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
	}
}
