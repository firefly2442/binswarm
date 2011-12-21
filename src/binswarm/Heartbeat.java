package binswarm;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import org.w3c.dom.NodeList;

import binswarm.comm.HelloMessage;
import binswarm.comm.MessageHeader;
import binswarm.comm.MessageListener;
import binswarm.comm.MulticastComm;
import binswarm.comm.UDPBroadcast;
import binswarm.comm.UDPListener;
import binswarm.config.Preferences;

public class Heartbeat implements Runnable, MessageListener {
	private final Timer timer = new Timer();
	UDPBroadcast broadcast;
	UDPListener listener;
	MulticastComm comm;

	public Heartbeat(UDPBroadcast broadcast, UDPListener listener) {
		this.broadcast = broadcast;
		this.listener = listener;
		this.listener.addMessageListener(HelloMessage.MESSAGE_Hello, this);
		Thread broadcastThread = new Thread(this);
		broadcastThread.start();
	}

	public Heartbeat(MulticastComm comm) {
		this.comm = comm;
		this.comm.addMessageListener(HelloMessage.MESSAGE_Hello, this);
		Thread broadcastThread = new Thread(this);
		broadcastThread.start();
	}

	public void send() {
		HelloMessage message = new HelloMessage(Binswarm.VERSION, 1024);
		if (broadcast != null)
			broadcast.send(message); // send UDP broadcast message
		else
			comm.send(message); // send multicast message
	}

	public void start(int seconds) {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				send();
				Networking.removeOldComputers();
			}
		}, 0, seconds * 1000);
	}

	public void run() {
		// if a certain amount of time has gone by, send out a broadcast
		start(Preferences.BroadcastInterval);
	}

	@Override
	public void messageRecieved(MessageHeader header, String messageType,
			NodeList innerNodes, String ip) {

		HelloMessage m = new HelloMessage();
		m.parseXml(innerNodes);
		if (m.getVersion().equals(Binswarm.VERSION)) {
			// make sure we don't add messages from ourself to the listing
			if (!header.getUUID().equals(Preferences.uuid)) {
				// if this is a new computer that is not in our list, send a
				// reply
				if (!Networking.computerInList(header.getUUID())) {
					send();
					// and create a new computer and add it to the list
					Networking.addComputer(header.getUUID(), ip);
				} else {
					// the computer already is in the list, so update timestamp,
					// IP, and other information
					Networking.updateList(header.getUUID(), ip);
				}
			}
		} else {
			Log.log("Received heartbeat from: " + ip
					+ ", but they are running a different version: ("
					+ m.getVersion() + ")", Level.INFO);
		}
	}
}
