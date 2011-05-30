package binswarm.database;

import binswarm.file.Checksum;

public class FileObject {
	
	private String checksum;
	private String filepath;
	private String filename;
	
	public FileObject(String path, String name)
	{
		//Constructor
		filepath = path;
		filename = name;
		
		//Calculate hash
		checksum = Checksum.getChecksum(path + name);
	}
	
	public String getChecksum()
	{
		return checksum;
	}
	
	public String getFilepath()
	{
		return filepath;
	}
	
	public String getFilename()
	{
		return filename;
	}
}
