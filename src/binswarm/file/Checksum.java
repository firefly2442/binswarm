package binswarm.file;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Checksum {
	
	public Checksum()
	{
		//constructor
	}
	
	public static String getChecksum(String fullfile_path)
	{
		InputStream fis;
		String result = "";
		
		try {
			fis = new FileInputStream(fullfile_path);
			
			byte[] buffer = new byte[1024];
			///@todo: Add support for different hash types besides MD5
			MessageDigest complete = MessageDigest.getInstance("MD5");
			int numRead;
			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
			fis.close();
			
			byte[] b = complete.digest();
			for (int i = 0; i < b.length; i++) {
			  result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring(1);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
	    return result;
	}

}
