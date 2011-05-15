package binswarm.comm;

import org.w3c.dom.NodeList;

public interface MessageListener {
	public void messageRecieved(MessageHeader header, String messageType, NodeList innerNodes, String ip);
}
