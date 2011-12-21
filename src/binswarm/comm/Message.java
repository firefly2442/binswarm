package binswarm.comm;

import org.w3c.dom.NodeList;

public abstract class Message {

	protected String messageType;

	public String getMessageType() {
		return messageType;
	}

	public abstract String getXml();

	public abstract void parseXml(NodeList nodes);
}
