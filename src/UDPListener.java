import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


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
		Logger.log("Listening for UDP packets...");
		
		DatagramSocket socket;
		try {
			socket = new DatagramSocket(2500);
			byte[] receiveData = new byte[128];
			
			while (true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {
					socket.receive(receivePacket);
					String version = new String(receivePacket.getData());
					InetAddress IPAddress = receivePacket.getAddress();
					Logger.log("Received UDP status from: " + IPAddress.toString() + " running version: " + version);
					
					//if the computer is running the same version we are
					if (version.equals(Binswarm.VERSION))
					{
						//if this is a new computer that is not in our list, send a reply
						//if (Networking.computerInList(uuid);
					
						//otherwise, update timestamp, IP, and other information
					}
					else
					{
						Logger.log("Received UDP status from: " + IPAddress.toString() + ", but they are running a different version.");
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
