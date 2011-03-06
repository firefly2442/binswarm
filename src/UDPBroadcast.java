import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;


public class UDPBroadcast implements Runnable
{
	private final Timer timer = new Timer();

	public UDPBroadcast()
	{
		//Constructor
		Thread broadcastThread = new Thread(this);
		broadcastThread.start();
	}
	
	public static void sendBroadcast()
	{
		try {
			DatagramSocket socket = new DatagramSocket();

			byte[] b = new byte[128];
			String temp = Binswarm.VERSION + "/" + Preferences.uuid.toString();
			b = temp.getBytes();
			DatagramPacket dgram = null;
			///@todo: This needs to be changed so the broadcast doesn't go too far.
			dgram = new DatagramPacket(b, b.length, InetAddress.getByName("255.255.255.255"), Preferences.UDPStatusPort);
			Log.log("Sending UDP status packet of size " + b.length + " bytes to " + dgram.getAddress().toString().replaceAll("/", "") + ':' + dgram.getPort(), Level.INFO);
			socket.send(dgram);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start(int seconds) {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                sendBroadcast();
                Networking.removeOldComputers();
            }
        }, 0, seconds * 1000);
    }


	public void run()
	{
		//if a certain amount of time has gone by, send out a broadcast
		start(Preferences.BroadcastInterval);
	}
}
