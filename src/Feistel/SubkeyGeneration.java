// Sub-key generation will create and manage the sub key from the given key.
package Feistel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class SubkeyGeneration {

	private static byte[] key = new byte[FeistelFunction.KEY_SIZE/8];
	private static byte[] key_permuted = new byte[FeistelFunction.KEY_SIZE/8];
	private static byte[] C = new byte[(key_permuted.length / 2)];
	private static byte[] D = new byte[(key_permuted.length / 2)];
	private static int[] rotate_schedule = new int[] {1, 1, 2, 2, 1, 2, 2, 2, 1, 1, 2, 2, 2, 2, 1, 1}; 
	public static void initializeKey() {
		keyReader(); // Read the key from the file and create the permuted key.
		makeCandD(); // Create the first C and D parts.
	}
	// Read the key from the file into the program.
	private static void keyReader() {
		String test = "thisiskey";	
		key_permuted = test.getBytes();
		/**
			File file_name = new File("asaf.txt");
			FileInputStream stream;
			try {
				stream = new FileInputStream(file_name);
				stream.read(key);
				stream.close();
				key_permuted =  key; 	//makePermuted(key, 1);
			} catch (FileNotFoundException e) {
				System.out.println("No file name \"k.txt\" was found.");
				System.exit(1);
			} catch (IOException e) {
				System.out.println("Problome reading the \"k.txt\" file.");
				System.exit(1);
			}
		**/	
	}
	/**
	// Create a permuted item.
	private static byte[] makePermuted(byte[] item, int permuted_choice) {
		// If permuted choice is 1, then item is the key.
		byte[] res = new byte[item.length];
		byte temp;
		if (permuted_choice == 1) {
			for (int i=0; i < (FeistelFunction.KEY_SIZE/8) - 1; i=i+2) {
				//////// Change this!!!!:::::///////
				temp = item[i];
				res[i] = (byte) (item[i+1] * 2);
				res[i+1] = (byte) (temp * 2);
			}
		// If permuted choice is 2, the next key.
		} else {
			for (int i=0; i < (FeistelFunction.KEY_SIZE/8) - 2; i=i+2) {
				//////// Change this!!!!:::::///////
				temp = item[i];
				res[i] = (byte) (item[i+2] * 4);
				res[i+2] = (byte) (temp * 4);
			}
		}
		return res;
	}
	**/
	// Create the first C and D parts of the key.
	private static void makeCandD() {
		// Split the key to 2 parts - C and  D.
		C = FeistelFunction.getHalf(key_permuted, 0, (key_permuted.length/2) -1);
		D = FeistelFunction.getHalf(key_permuted, key_permuted.length/2, key_permuted.length-1);
	}
	
	// Rotate left the given array.
	public static byte[] rotateLeft(byte[] arr, int num) {
		//int length = (arr.length-1)/8 + 1;
	      byte[] res = new byte[arr.length];
	      for (int i=0; i<arr.length; i++) {
	         int val = getBit(arr,(i+num)%arr.length);
	         setBit(res,i,val);	
	      }
	      return res;
	   }
	
	private static byte[] permute(byte[] arr1, byte[] arr2) {
		// The array show the chosen bits for the key.
		int[] permuted_choice = new int[] {14, 17, 11, 24, 1, 5, 3, 28,
										   15, 6, 21, 10, 23, 19, 12, 4,
										   26, 8, 16, 7, 27, 20, 13, 2,
										   41, 52, 31, 37, 47, 55, 30, 40,
										   51, 45, 33, 48, 44, 49, 39, 56,
										   34, 53, 46, 42, 50, 36, 29, 32};
		byte[] full_arr = new byte[14];
		// Combine the given arrays into 1 array.
		for (int i=0; i < arr1.length; i++) {
			full_arr[i] = arr1[i];
			full_arr[i + (arr1.length -1)] = arr2[i];
		}
		// Select the return bits based on the permuted choice.
		byte[] res_arr = new byte[12];
		for (int i=0; i < 12; i++) {
			/// CHECK THIs!!! ???? ////
			res_arr[i] = full_arr[(Math.round(permuted_choice[i]/4))];//full_arr[(permuted_choice[i])-1];
		}
		return res_arr;
	}
	
	// Make key for the current round from C and D.
	public static byte[] makeKey(int currentRound) {
		C = rotateLeft(C,rotate_schedule[currentRound-1]);
		D = rotateLeft(D,rotate_schedule[currentRound-1]);
		return permute(C, D);
	}
	
	/**
	// Make key for the current round from C and D.
	public byte[] makeKey(int currentRound) {
		byte[] newC = rotateLeft(C, currentRound);
		byte[] newD = rotateLeft(D, currentRound);
		byte[] newKey = new byte[FeistelFunction.KEY_SIZE];
		for (int i=0; i < FeistelFunction.KEY_SIZE/2; i++) {
			newKey[i] = newC[i];
			newKey[(FeistelFunction.KEY_SIZE/2)+i] = newD[i];
		}
		return makePermuted(newKey, 2);
	}	
	**/
	
	public static int getBit(byte[] data, int pos) {
	      int posByte = pos/8; 
	      int posBit = pos%8;
	      byte valByte = data[posByte];
	      int valInt = valByte>>(8-(posBit+1)) & 0x0001;
	      return valInt;
	   }
	
	public static void setBit(byte[] data, int pos, int val) {
	      int posByte = pos/8; 
	      int posBit = pos%8;
	      byte oldByte = data[posByte];
	      oldByte = (byte) (((0xFF7F>>posBit) & oldByte) & 0x00FF);
	      byte newByte = (byte) ((val<<(8-(posBit+1))) | oldByte);
	      data[posByte] = newByte;
	   }
}
