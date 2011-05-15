package binswarm.comm;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import binswarm.XmlHelper;

public class HelloMessage extends Message{
	public static final String MESSAGE_Hello = "hello"; 
	private static final String FIELD_Version = "version";
	private static final String FIELD_StorageAvailable = "storageAvailable";
	private String version = null;
	private int storageAvailable = 0;
	private String xml = null;
	
	public HelloMessage() {} 
	public HelloMessage(String version, int storageAvailable)
	{
		messageType = MESSAGE_Hello;
		this.version = version;
		this.storageAvailable = storageAvailable;
	}
	
	public String getVersion()
	{
		return version;
	}
	
	public int getStorageAvailable()
	{
		return storageAvailable;
	}
	
	public String getXml()
	{
		if(xml == null)
		{
			xml = XmlHelper.FormatElement(FIELD_Version, version)+
			XmlHelper.FormatElement(this.FIELD_StorageAvailable, Integer.toString(storageAvailable));
		}
		return xml;
	}

	@Override
	public void parseXml(NodeList nodes) {
		if(nodes != null)
		{
			for(int i = 0; i< nodes.getLength(); i++)
			{
				if(nodes.item(i).getNodeType() == Node.ELEMENT_NODE)
				{
					Element e = (Element)nodes.item(i);
					if(e.getNodeName().equals(this.FIELD_Version))
					{
						version = XmlHelper.ParseString(e);
					}
					else if(e.getNodeName().equals(this.FIELD_StorageAvailable))
					{
						storageAvailable = XmlHelper.ParseInt(e);
					}
				}
			}
		}
	}
}
