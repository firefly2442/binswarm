package binswarm.comm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
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

import binswarm.Binswarm;
import binswarm.Log;
import binswarm.XmlHelper;


public class MulticastComm implements Runnable {
	Map<String, MessageListener> listeners = null;
	
	MessageHeader header = null;
	UUID uuid;
	InetAddress group;
	MulticastSocket socket = null;
	int port = 0;
	private final Timer timer = new Timer();
	
	public MulticastComm(UUID uuid, String groupAddress, int port)
	{ 
		this(uuid, groupAddress, port, 1);
	}
	
	public MulticastComm(UUID uuid, String groupAddress, int port, int ttl)
	{
		this.uuid = uuid;
		this.header = new MessageHeader(uuid);
		try {
			group = InetAddress.getByName(groupAddress);
			this.port = port;
			socket = new MulticastSocket(port);
			socket.setTimeToLive(ttl);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.listeners = new HashMap<String, MessageListener>();
	}
	
	public void addMessageListener(String messageType, MessageListener listener)
	{
		if(messageType != null && listener != null)
			listeners.put(messageType, listener);
	}
	
	public boolean open()
	{
		boolean success = false;
		try {
			socket.joinGroup(group);
			
			Thread broadcastThread = new Thread(this);
			broadcastThread.start();
			
			success = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	public boolean close()
	{
		boolean success = false;
		try {
			socket.leaveGroup(group);
			success = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	private String formatMessage(String messageType, String innerElements)
	{
		String message = String.format("<binswarm>%1$<body type='%2$'>%3$</body></binswarm>", header.toXml(), messageType, innerElements);
		return message;
	}
	
	public boolean send(Message message)
	{
		String messageText = formatMessage(message.getMessageType(), message.getXml());
		DatagramPacket data = new DatagramPacket(messageText.getBytes(), messageText.length(), group, port);
		try {
			socket.send(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
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
				socket.receive(receivePacket);
				
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
				
				
				Log.log("Received packet from: " + ipString, Level.INFO);
				
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
