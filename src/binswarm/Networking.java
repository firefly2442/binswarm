package binswarm;

import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;

import binswarm.comm.MessageHeader;
import binswarm.comm.MulticastComm;
import binswarm.comm.UDPBroadcast;
import binswarm.comm.UDPListener;
import binswarm.config.Preferences;

public class Networking {
	// all known computers in the network running this app
	public static Vector<Computer> computers = new Vector<Computer>();
	UDPBroadcast broadcast = null;
	UDPListener listener = null;
	Heartbeat hb = null;
	MulticastComm comm = null;

	public Networking() {
		// Constructor
		if (Preferences.UseUDPBroadcast == true) {
			// UDP Broadcast
			broadcast = new UDPBroadcast(new MessageHeader(Preferences.uuid));
			listener = new UDPListener();
			hb = new Heartbeat(broadcast, listener);
		} else {
			// Multicast
			comm = new MulticastComm(Preferences.uuid,
					Preferences.MulticastGroupAddress,
					Preferences.MulticastGroupPort, 4); // /@todo add number of
														// hops (4) as a
														// preference option
			hb = new Heartbeat(comm);
		}
	}

	public static void addComputer(UUID uuid, String IPAddress) {
		Log.log("Computer added to listing with IP: " + IPAddress
				+ " and UUID: " + uuid.toString(), Level.INFO);
		computers.add(new Computer(uuid, IPAddress));
	}

	public static void removeComputer(UUID uuid) {
		for (int i = 0; i < computers.size(); i++) {
			if (computers.get(i).uuid == uuid) {
				Log.log("Computer removed from listing with UUID: "
						+ uuid.toString(), Level.INFO);
				computers.remove(i);
				break;
			}
		}
	}

	public static void removeOldComputers() {
		// Remove stale listings
		for (int i = 0; i < computers.size(); i++) {
			long diff = (System.currentTimeMillis() - computers.get(i).timeStamp) / 1000;
			if (diff > Preferences.CountDowntoRemoval) {
				removeComputer(computers.get(i).uuid);
			}
		}
	}

	public static boolean computerInList(UUID uuid) {
		// Check to see if a computer is already in our listing
		for (int i = 0; i < computers.size(); i++) {
			if (computers.get(i).uuid.equals(uuid)) {
				return true;
			}
		}
		return false;
	}

	public static void updateList(UUID uuid, String IP) {
		// updates the specified UUID computer
		for (int i = 0; i < computers.size(); i++) {
			if (computers.get(i).uuid.equals(uuid)) {
				computers.get(i).IPAddress = IP;
				computers.get(i).updateTimeStamp(); // update the time
				Log.log("Computer in list updated with IP: " + IP
						+ " and UUID: " + uuid.toString(), Level.INFO);
				return;
			}
		}
		// should never get here, at this point, the UUID hasn't been found in
		// the list
		Log.log("Running updateList() method but unable to find the specified UUID.",
				Level.SEVERE);
	}
}
