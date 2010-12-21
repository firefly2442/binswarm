import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;


public class UDPBroadcast implements Runnable
{
	private final Timer timer = new Timer();

	public UDPBroadcast()
	{
		//Constructor
		Thread broadcastThread = new Thread(this);
		broadcastThread.start();
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
	
	public void start(int seconds) {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                sendBroadcast();
            }
        }, 0, seconds * 1000);
    }


	public void run()
	{
		//if a certain amount of time has gone by, send out a broadcast
		start(10); //run every 10 seconds
	}
}
