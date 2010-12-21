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
		System.out.println("Listening for UDP packet");
		
		DatagramSocket socket;
		try {
			socket = new DatagramSocket(2500);
			byte[] receiveData = new byte[1024];
			
			while (true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {
					socket.receive(receivePacket);
					String sentence = new String(receivePacket.getData());
					InetAddress IPAddress = receivePacket.getAddress();
					int port = receivePacket.getPort();
					System.out.println("Received: " + sentence);
					System.out.println("From: " + IPAddress.toString() + ":" + port);
					
					//TODO
					//if this is a new computer that is not in our list, send a reply
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
	}
}
