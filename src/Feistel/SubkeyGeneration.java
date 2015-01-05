// Sub-key generation will create and manage the sub key from the given key.
package Feistel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SubkeyGeneration {

	private String key;
	private byte[] key_bin;
	private byte[] C;
	private byte[] D;
	
	public SubKeyGeneration() {
	}
	
	// Read the key from the file into the program.
	private void keyReader() {
			File file_name = new File("k.txt");
			FileInputStream stream;
			try {
				stream = new FileInputStream(file_name);
			} catch (FileNotFoundException e) {
				System.out.println("No file named \"k.txt\" was found.");
				return;
			}
			byte[] data = new byte[(int) file_name.length()];
			try {
				stream.read(data);
				stream.close();
			} catch (IOException e) {
				System.out.println("Problome reading the \"k.txt\" file.");
				return;
			}
			try {
				key = new String(data, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("Could not changed the key into a string");
				return;
			}
		
	}
}
