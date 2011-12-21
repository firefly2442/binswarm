package binswarm.comm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import binswarm.Log;


public class MulticastComm implements Runnable {
	Map<String, MessageListener> listeners = null;
	
	MessageHeader header = null;
	UUID uuid;
	InetAddress group;
	MulticastSocket receiveSocket = null;
	DatagramSocket sendSocket = null;
	int port = 0;
	
	public MulticastComm(UUID uuid, String groupAddress, int port)
	{
		//get around the fact that Java doesn't support optional parameters
		this(uuid, groupAddress, port, 1);
	}
	
	public MulticastComm(UUID uuid, String groupAddress, int port, int ttl)
	{
		this.uuid = uuid;
		this.header = new MessageHeader(uuid);
		try {
			group = InetAddress.getByName(groupAddress);
			this.port = port;
			receiveSocket = new MulticastSocket(port);
			receiveSocket.setTimeToLive(ttl);
			sendSocket = new DatagramSocket();
			this.open(); ///@todo <- where do we call open, since it starts the threading, where should this go?
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.listeners = new HashMap<String, MessageListener>();
	}
	
	public void addMessageListener(String messageType, MessageListener listener)
	{
		if(messageType != null && listener != null)
		{
			listeners.put(messageType, listener);
			Log.log("Multicast listener running", Level.INFO);
		}
	}
	
	public boolean open()
	{
		boolean success = false;
		try {
			receiveSocket.joinGroup(group);
			Log.log("Joined multicast group", Level.INFO);
			
			Thread broadcastThread = new Thread(this);
			broadcastThread.start();
			
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	public boolean close()
	{
		boolean success = false;
		try {
			receiveSocket.leaveGroup(group);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	private String formatMessage(String messageType, String innerElements)
	{
		String message = String.format("<binswarm>%1$s<body type='%2$s'>%3$s</body></binswarm>", header.toXml(), messageType, innerElements);
		return message;
	}
	
	public boolean send(Message message)
	{
		String messageText = formatMessage(message.getMessageType(), message.getXml());
		DatagramPacket data = new DatagramPacket(messageText.getBytes(), messageText.length(), group, port);
		try {
			sendSocket.send(data);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		Log.log("Sending multicast message", Level.INFO);
		return true;
	}
	
	public void run()
	{
		Log.log("Listening for packets...", Level.INFO);
		
		byte[] receiveData = new byte[1024]; // this should be larger than the largest message.
		
		while (true)
		{
			String ipString = null;
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				receiveSocket.receive(receivePacket);
				
				InetAddress IPAddress = receivePacket.getAddress();
				ipString = IPAddress.toString().replaceAll("/", "");
				
				String received = new String(receivePacket.getData()).trim();
				boolean found = false;
				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				try
				{
					MessageHeader header = null;
					NodeList innerNodes = null;
					
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document dom = db.parse(received);
					String messageType = null;
					
					NodeList list = dom.getElementsByTagName("body");
					if(list != null && list.getLength() > 0)
					{
						Node bodyElement = list.item(0);
						if(bodyElement != null)
						{
							if(bodyElement.getAttributes().getLength() > 0)
							{
								Node typeAttribute = bodyElement.getAttributes().getNamedItem("type");
								if(typeAttribute != null)
								{
									messageType = typeAttribute.getTextContent();
									
									list = dom.getElementsByTagName("head");
									if(list != null && list.getLength() > 0)
									{
										Element headElement = (Element)list.item(0);
										header = MessageHeader.parse(headElement);
									}
									
									innerNodes = bodyElement.getChildNodes();
									
									OnRecievedMessage(header, messageType, innerNodes, ipString);
									
									found = true;
								}								
							}
						}
					}
					if(!found)
					{
						// malformed message
						Log.log("Malformed message: " + received, Level.INFO);
					}
				} catch(ParserConfigurationException pce) {
					pce.printStackTrace();
				} catch (SAXException saxe) {
					saxe.printStackTrace();
				}
				
				Log.log("Received multicast message from: " + ipString, Level.INFO);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void OnRecievedMessage(MessageHeader header, String messageType, NodeList innerNodes, String ip)
	{
		Log.log(String.format("Received message from %1$ (%2$): %3$", header.getUUID().toString(), ip, messageType), Level.INFO);
		MessageListener listener = listeners.get(messageType);
		if(listener != null)
			listener.messageRecieved(header, messageType, innerNodes, ip);
	}
}
