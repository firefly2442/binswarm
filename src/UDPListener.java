import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
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
					String version = new String(receivePacket.getData());
					InetAddress IPAddress = receivePacket.getAddress();
					Log.log("Received UDP status from: " + IPAddress.toString() + " running version: " + version, Level.INFO);
					
					//if the computer is running the same version we are
					if (version.equals(Binswarm.VERSION))
					{
						//if this is a new computer that is not in our list, send a reply
						//if (Networking.computerInList(uuid);
					
						//otherwise, update timestamp, IP, and other information
					}
					else
					{
						Log.log("Received UDP status from: " + IPAddress.toString() + ", but they are running a different version.", Level.INFO);
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
