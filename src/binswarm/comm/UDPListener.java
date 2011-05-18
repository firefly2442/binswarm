package binswarm.comm;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
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
import binswarm.config.Preferences;


public class UDPListener implements Runnable
{
	Map<String, MessageListener> listeners = null;
	public UDPListener() 
	{
		//Constructor
		Thread listenerThread = new Thread(this);
		listenerThread.start();
		this.listeners = new HashMap<String, MessageListener>();
	}
	
	public void addMessageListener(String messageType, MessageListener listener)
	{
		if(messageType != null && listener != null)
			listeners.put(messageType, listener);
	}

	public void run()
	{
		Log.log("Listening for UDP packets...", Level.INFO);
		
		DatagramSocket socket;
		try {
			socket = new DatagramSocket(Preferences.UDPStatusPort);
			byte[] receiveData = new byte[1024];
			
			while (true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {
					socket.receive(receivePacket);
					
					InetAddress IPAddress = receivePacket.getAddress();
					String ipString = IPAddress.toString().replaceAll("/", "");
					
					String received = new String(receivePacket.getData()).trim();
					boolean found = false;
					
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					try
					{
						MessageHeader header = null;
						NodeList innerNodes = null;
						
						DocumentBuilder db = dbf.newDocumentBuilder();
						ByteArrayInputStream stream = new ByteArrayInputStream(received.getBytes());
						Document dom = db.parse(stream);
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
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
	}
	
	private void OnRecievedMessage(MessageHeader header, String messageType, NodeList innerNodes, String ip)
	{
		Log.log(String.format("Received message from %1$s (%2$s): %3$s", header.getUUID().toString(), ip, messageType), Level.INFO);
		MessageListener listener = listeners.get(messageType);
		if(listener != null)
			listener.messageRecieved(header, messageType, innerNodes, ip);
	}
}
