package Feistel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;


public class CBCmode {
	
	private static byte[] iv = new byte[(FeistelFunction.BLOCK_SIZE/4)/2];
    private static byte[] plaintext;
    private byte[] ciphertext;

    public static void CBCstart() {
    	//readFiles(); // Read the plaintext and ciphertext files.
    	String text = "thigfedlourhgftg";
    	plaintext = text.getBytes();
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
    	for (int i = 0; i < (FeistelFunction.BLOCK_SIZE/4)/2; i++) { 
    		iv[i] = (byte) (i % 2); 
    	} 
    } 
    
    // Encrypt the given text.
    public static void encrypt() {
    	try {
    		// Create the file to print the result in.
			FileOutputStream out = new FileOutputStream("ciphertext-result.txt");
			// Create the first ciphertext with the IV.
			
			byte[] nextPart = FeistelFunction.getHalf(plaintext,0, 7);
			byte[] res = FeistelFunction.xor(iv, nextPart);
			res = FeistelFunction.encryptBlock(res);
			// Decode in Base 64.
			byte[] encodedBytes = Base64.encodeBase64(res);
			System.out.println("First Part:  " + new String(encodedBytes));
			out.write(encodedBytes);
			
			// Create the rest of the ciphertext.
			int index = 8;
			while (index < (plaintext.length - 8)) {
				nextPart = FeistelFunction.getHalf(plaintext, index, index + 8);
				res = FeistelFunction.xor(res, nextPart);
				res = FeistelFunction.encryptBlock(res);
				encodedBytes = Base64.encodeBase64(res);
				System.out.println("part " + (index/8) + ": " + new String(encodedBytes));
				out.write(encodedBytes);
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
				res = FeistelFunction.encryptBlock(res);
				encodedBytes = Base64.encodeBase64(res);
				System.out.println("Last part: " + new String(encodedBytes));
				out.write(res);
			}
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not create the file: \"ciphertext-result.txt\".");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Could not write to the file: \"ciphertext-result.txt\".");
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
			// Decode in Base 64.
			byte[] decodedBytes = Base64.decodeBase64(res);
			System.out.println("First Part: " + new String(decodedBytes));
			out.write(res);
			// Create the rest of the ciphertext.
			int index = 8;
			while (index < (ciphertext.length - 8)) {
				nextPart = FeistelFunction.getHalf(ciphertext, index, index + 8);
				nextPart = FeistelFunction.decryptBlock(nextPart);
				res = FeistelFunction.xor(res, nextPart);
				decodedBytes = Base64.decodeBase64(res);
				System.out.println("part " + (index/8) + ": " + new String(decodedBytes));
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
				decodedBytes = Base64.decodeBase64(res);
				System.out.println("Last part: " + new String(decodedBytes));
				out.write(res);
			}
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not create the file: \"plaintext-result.txt\".");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Could not write to the file: \"plaintext-result.txt\".");
			System.exit(1);
		}
    }	
}

