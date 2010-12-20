import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UDPBroadcast
{
	public UDPBroadcast()
	{
		//Constructor
		sendBroadcast();
	}
	
	public void sendBroadcast()
	{
		try {
			System.out.println("About to broadcast UDP");
			DatagramSocket socket = new DatagramSocket();

			byte[] b = new byte[128];
			b = Binswarm.VERSION.getBytes();
			DatagramPacket dgram = null;
			dgram = new DatagramPacket(b, b.length, InetAddress.getByName("255.255.255.255"), 2500);
			System.out.println("Sending " + b.length + " bytes to " + dgram.getAddress() + ':' + dgram.getPort());
			socket.send(dgram);
			System.out.println("UDP packet sent");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
