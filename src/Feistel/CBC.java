package Feistel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import sun.net.www.content.text.plain;

public class CBC {
	
	private byte[] iv = new byte[FeistelFunction.BLOCK_SIZE];
    private byte[] plaintext;
    private byte[] ciphertext;

    public void CBC() {
    	readFiles(); // Read the plaintext and ciphertext files.
    	encrypt();
    	
    }
    
    // Read the plaintext file.
    private void readFiles() {
		File plaintext_file = new File("p.txt");
		File ciphertext_file = new File("c.txt");
		FileInputStream stream;
		try {
			stream = new FileInputStream(plaintext_file);
			stream.read(plaintext);
			stream.close();
			stream = new FileInputStream(ciphertext_file);
			stream.read(ciphertext);
			stream.close();
		} catch (FileNotFoundException e) {
			System.out.println("No file name \"p.txt\" or \"c.txt\" was found.");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Problome reading the \"p.txt\" or \"c.txt\" file.");
			System.exit(1);
		}
}
    /// Set the Initialization Vector.
    public void setIv() { 
    	// Set the VI to be 0101.... at the size of the block. 
    	for (int i = 0; i < (FeistelFunction.BLOCK_SIZE); i++) { 
    		iv[i] = (byte) (i % 2); 
    	} 
    } 
    // Encrypt the given text.
    public void encrypt() {
    	try {
    		// Create the file to print the result in.
			FileOutputStream out = new FileOutputStream("ciphertext-result.txt");
			// Create the first ciphertext with the IV.
			byte[] nextPart = FeistelFunction.getHalf(plaintext,0, 7);
			byte[] res = FeistelFunction.xor(iv, nextPart);
			res = FeistelFunction.encryptBlock(res);
			System.out.println("First part: " + res);
			out.write(res);
			// Create the rest of the ciphertext.
			int index = 8;
			while (index < (plaintext.length - 8)) {
				nextPart = FeistelFunction.getHalf(plaintext, index, index + 8);
				res = FeistelFunction.xor(res, nextPart);
				res = FeistelFunction.encryptBlock(res);
				System.out.println("part " + (index/8) + ": " + res);
				out.write(res);
				index = index + 8;
			}
			// Check if there is more bytes to encrypt.
			if (index < plaintext.length) {
				nextPart = FeistelFunction.getHalf(plaintext, index, plaintext.length - 1);
				int neededAdd = 8 - nextPart.length;
				for (int i=0; i < neededAdd; i++) {
					nextPart[nextPart.length + i] = 0;
				}
				res = FeistelFunction.xor(res, nextPart);
				res = FeistelFunction.encrypteBlock(res);
				System.out.println("Last part: " + res);
				out.write(res);
			}
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not create the file: \"ciphertext-result.txt\".");
			System.exit(1);
		}
    	
    }
    

    public void decrypt() {
       	try {
    		// Create the file to print the result in.
			FileOutputStream out = new FileOutputStream("plaintext-result.txt");
			// Create the first plaintext with the IV.
			byte[] nextPart = FeistelFunction.getHalf(ciphertext,0, 7);
			nextPart = FeistelFunction.decryptBlock(nextPart);
			byte[] res = FeistelFunction.xor(iv, nextPart);
			System.out.println("First part: " + res);
			out.write(res);
			// Create the rest of the ciphertext.
			int index = 8;
			while (index < (ciphertext.length - 8)) {
				nextPart = FeistelFunction.getHalf(ciphertext, index, index + 8);
				nextPart = FeistelFunction.decryptBlock(nextPart);
				res = FeistelFunction.xor(res, nextPart);
				System.out.println("part " + (index/8) + ": " + res);
				out.write(res);
				index = index + 8;
			}
			// Check if there is more bytes to encrypt.
			if (index < ciphertext.length) {
				nextPart = FeistelFunction.getHalf(ciphertext, index, ciphertext.length - 1);
				int neededAdd = 8 - nextPart.length;
				for (int i=0; i < neededAdd; i++) {
					nextPart[nextPart.length + i] = 0;
				}
				nextPart = FeistelFunction.decryptBlock(nextPart);
				res = FeistelFunction.xor(res, nextPart);
				System.out.println("Last part: " + res);
				out.write(res);
			}
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not create the file: \"plaintext-result.txt\".");
			System.exit(1);
		}
    }
    /*    /// Encrypt a block of bytes.
    public void encrypt( byte[] clearText, int clearOff, byte[] cipherText, int cipherOff )
	{
	xorBlock( clearText, clearOff, iv, 0, temp, 0, blockSize );
	blockCipher.encrypt( temp, 0, cipherText, cipherOff );
	copyBlock( cipherText, cipherOff, iv, 0, blockSize );
	}*/

    /// Decrypt a block of bytes.
    public void decrypt( byte[] cipherText, int cipherOff, byte[] clearText, int clearOff )
	{
	blockCipher.decrypt( cipherText, cipherOff, temp, 0 );
	xorBlock( temp, 0, iv, 0, clearText, clearOff, blockSize );
	copyBlock( cipherText, cipherOff, iv, 0, blockSize );
	}
    	
    }

