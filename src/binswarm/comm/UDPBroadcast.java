package binswarm.comm;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Level;

import binswarm.Binswarm;
import binswarm.Log;
import binswarm.Networking;
import binswarm.config.Preferences;


public class UDPBroadcast
{
	private final Timer timer = new Timer();
	MessageHeader header;
	DatagramSocket socket;
	
	public UDPBroadcast(MessageHeader header)
	{
		this.header = header;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String formatMessage(String messageType, String innerElements)
	{
		String message = String.format("<binswarm>%1$s<body type='%2$s'>%3$s</body></binswarm>", header.toXml(), messageType, innerElements);
		return message;
	}
	
	public boolean send(Message message)
	{
		String messageText = formatMessage(message.getMessageType(), message.getXml());
		DatagramPacket data;
		try {
			data = new DatagramPacket(messageText.getBytes(), messageText.length(), InetAddress.getByName("255.255.255.255"), Preferences.UDPStatusPort);
			socket.send(data);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
