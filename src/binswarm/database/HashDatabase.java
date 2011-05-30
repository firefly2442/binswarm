package binswarm.database;

import java.util.HashMap;
import java.util.Map;

public class HashDatabase {
	
	private Map<String, FileObject> hash_table = new HashMap<String, FileObject>();
	
	public HashDatabase()
	{
		//constructor
	}
	
	public String addFile(String filepath, String filename)
	{
		FileObject fileobject = new FileObject(filepath, filename);
		hash_table.put(fileobject.getChecksum(), fileobject);
		return fileobject.getChecksum();
	}
	
	public void removeFile(String checksum)
	{
		hash_table.remove(checksum);
	}
	
	public FileObject getFileObject(String checksum)
	{
		return (FileObject)hash_table.get(checksum);
	}
}
