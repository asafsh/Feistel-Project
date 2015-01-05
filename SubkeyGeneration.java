// Sub-key generation will create and manage the sub key from the given key.
package Feistel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SubkeyGeneration {

	private byte[] key = new byte[KEY_SIZE];
	private byte[] C = new byte[KEY_SIZE / 2];
	private byte[] D = new byte[KEY_SIZE / 2];
	
	public void SubKeyGeneration() {
		keyReader(); // Read the key from the file.
	}
	
	// Read the key from the file into the program.
	private void keyReader() {
			File file_name = new File("k.txt");
			FileInputStream stream;
			try {
				stream = new FileInputStream(file_name);
				stream.read(key);
				stream.close();
			} catch (FileNotFoundException e) {
				System.out.println("No file named \"k.txt\" was found.");
				System.exit(1);
			} catch (IOException e) {
				System.out.println("Problome reading the \"k.txt\" file.");
				System.exit(1);
			}
	}
	
	// Create the C and D parts of the key.
	private void makeCandD() {
		// Split the key to 2 parts - C and  D.
		for (int i = 0; i < (KEY_SIZE / 2); i++) {
			C[i] = key[i];
			D[i] = key[(KEY_SIZE / 2) + i];
		}
	}
	
	
}
