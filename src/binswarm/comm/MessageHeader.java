package binswarm.comm;

import java.util.UUID;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import binswarm.XmlHelper;

public class MessageHeader {
	private final static String HEAD_UUID = "uuid";
	private UUID uuid;
	private String xml = null;
	
	private MessageHeader()
	{
	}
	
	public MessageHeader(UUID uuid)
	{
		this.uuid = uuid;
	}
	
	public static MessageHeader parse(Element headerElement)
	{
		MessageHeader header = new MessageHeader();
		Element singleElement = null;
		
		for(int i = 0; i < headerElement.getChildNodes().getLength(); i++)
		{
			if(headerElement.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE)
			{
				singleElement = (Element)headerElement.getChildNodes().item(i);
				
				if(singleElement.getNodeName().equals(MessageHeader.HEAD_UUID))
				{
					header.uuid = XmlHelper.ParseUUID(singleElement);
				}
			}
		}
		
		return header;
	}
	
	public String toXml()
	{
		if(xml == null)
		{
			xml = String.format("<head><uuid>%1$s</uuid></head>", uuid);
		}
		return xml;
	}

	public UUID getUUID()
	{
		return uuid;
	}
}
