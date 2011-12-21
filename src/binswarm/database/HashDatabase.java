package binswarm.database;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import binswarm.Log;

public class HashDatabase {

	private Map<String, FileObject> hash_table = new HashMap<String, FileObject>();

	public HashDatabase() {
		// constructor
	}

	public String addFile(String filepath, String filename) {
		FileObject fileobject = new FileObject(filepath, filename);
		hash_table.put(fileobject.getChecksum(), fileobject);
		return fileobject.getChecksum();
	}

	public void removeFile(String checksum) {
		if (hash_table.remove(checksum) == null) {
			Log.log("Attempted to remove checksum: " + checksum
					+ ", but it doesn't exist.", Level.SEVERE);
		}
	}

	public FileObject getFileObject(String checksum) {
		return (FileObject) hash_table.get(checksum);
	}
}
