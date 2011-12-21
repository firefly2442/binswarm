package binswarm.file;

import java.util.logging.Level;

import binswarm.Log;

public class FileWatcher implements Runnable {

	private boolean running = false;
	private Thread WatcherThread = new Thread(this);

	public FileWatcher() {
		// Constructor
		startWatching();
	}

	public boolean isRunning() {
		return running;
	}

	public void startWatching() {
		if (!running && !WatcherThread.isAlive()) {
			running = true;
			WatcherThread.start();
		} else {
			Log.log("Tried to start watching filesystem but thread already running!",
					Level.SEVERE);
		}
	}

	public void stopWatching() {
		running = false;
		while (WatcherThread.isAlive()) {
			// busy wait until the thread finishes up
		}
	}

	public void run() {
		Log.log("Started watching local filesystem for file changes.",
				Level.INFO);

		while (running) {

		}
	}

}
