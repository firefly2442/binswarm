package binswarm.comm;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import binswarm.config.Preferences;


public class UDPBroadcast
{
	private MessageHeader header;
	private DatagramSocket socket;
	
	public UDPBroadcast(MessageHeader header)
	{
		this.header = header;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
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
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
