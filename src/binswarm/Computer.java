package binswarm;

import java.util.Date;
import java.util.UUID;

public class Computer {
	public UUID uuid;
	public String IPAddress;
	public long dataStored; // in bytes
	public long availableStorage; // in bytes
	public long timeStamp; // the last time we saw this computer (in
							// milliseconds)

	public Computer(UUID id, String IP) {
		// Constructor
		uuid = id;
		IPAddress = IP;
		updateTimeStamp();
	}

	public void updateTimeStamp() {
		timeStamp = System.currentTimeMillis();
	}

	public String returnHumanReadableTimestamp() {
		String temp_date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
				.format(new Date(timeStamp));
		return temp_date;
	}
}
