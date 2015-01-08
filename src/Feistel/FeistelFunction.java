package Feistel;

import Feistel.SubkeyGeneration;

public class FeistelFunction {

	final static int BLOCK_SIZE = 8;
	final static int KEY_SIZE = 56;
	final static int PERMUTATED_BLOCK = 6;
	private static int currentRound = 0;

	//change numbers
	private static final int[] table = {
		32, 1,  2,  3,  4,  5,   
		12, 13, 14, 15, 16, 17,
		16, 17, 18, 19, 20, 21,
		20, 21, 22, 23, 24, 25,
		24, 25, 26, 27, 28, 29,
		4,  5,  6,  7,  8,  9,
		8,  9,  10, 11, 12, 13,
		28, 29, 30, 31, 32, 1
	};

	private static int[] p_box = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5,
		18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4,
		25};
	private static int[] IP = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36,
		  28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32,
		  24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19,
		  11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };
		 // inverse initial permutation
	private static int[] invIP = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47,
		  15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13,
		  53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51,
		  19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17,
		  57, 25 };

	private static final int[][][] s_boxes = { 
		{ { 10, 9, 0, 14, 6, 15, 3, 5, 1, 13, 7, 12, 4, 11, 8, 2 },
			{ 7, 13, 9, 0, 3, 6, 4, 10, 2, 8, 5, 12, 14, 11, 1, 15 },
			{ 6, 13, 4, 8, 9, 15, 0, 3, 1, 11, 12, 2, 5, 14, 10, 7 },
			{ 10, 1, 13, 8, 9, 7, 4, 7, 15, 3, 14, 11, 5, 11, 12, 2 }
		},
		{  { 14, 4, 1, 13, 15, 2, 8, 11, 10, 8, 3, 12, 6, 5, 9, 7 },
			{ 15, 4, 7, 14, 13, 2, 13, 1, 6, 10, 12, 11, 3, 9, 5, 8 },
			{ 4, 1, 14, 13, 8, 6, 2, 11, 15, 9, 12, 7, 3, 10, 0, 5 },
			{ 15, 12, 2, 8, 9, 4, 1, 7, 11, 5, 14, 3, 10, 0, 13, 6 }
		},
		{ { 15, 8, 1, 14, 6, 3, 9, 11, 4, 7, 3, 2, 12, 0, 13, 5 },
			{ 13, 3, 7, 4, 15, 8, 2, 12, 14, 10, 1, 10, 9, 6, 11, 0 },
			{ 14, 0, 7, 10, 11, 4, 1, 13, 5, 12, 8, 6, 3, 15, 2, 9 },
			{ 8, 13, 1, 10, 15, 2, 11, 6, 3, 4, 6, 12, 7, 0, 14, 5 }
		},
		{ { 13, 8, 2, 4, 6, 11, 15, 11, 10, 3, 9, 0, 14, 12, 7, 5 },
			{ 15, 1, 10, 8, 7, 3, 13, 4, 12, 6, 5, 11, 14, 0, 9, 2 },
			{ 11, 4, 7, 1, 12, 9, 14, 0, 2, 6, 0, 13, 10, 5, 3, 15 },
			{ 1, 14, 2, 7, 4, 10, 13, 10, 15, 9, 3, 12, 6, 0, 11, 5 }
		},
		{ { 13, 7, 3, 14, 6, 0, 10, 9, 2, 1, 5, 8, 11, 12, 15, 4 },
			{ 8, 13, 5, 11, 6, 15, 3, 0, 4, 2, 7, 12, 10, 1, 9, 14 },
			{ 10, 9, 6, 12, 0, 11, 13, 7, 15, 3, 1, 14, 2, 5, 4, 8 },
			{ 15, 3, 6, 0, 1, 10, 13, 9, 4, 8, 5, 11, 2, 7, 14, 2 }
		},
		{ { 11, 2, 4, 14, 0, 8, 15, 3, 13, 7, 9, 10, 5, 1, 6, 12 },
			{ 13, 0, 11, 4, 7, 1, 9, 10, 3, 14, 5, 2, 12, 6, 8, 15 },
			{ 11, 13, 1, 4, 12, 2, 7, 3, 10, 14, 15, 8, 6, 0, 9, 5 },
			{ 11, 13, 1, 6, 10, 4, 7, 9, 5, 15, 0, 14, 3, 2, 8, 12 }
		},
		{ { 12, 4, 2, 7, 1, 10, 11, 8, 6, 5, 15, 3, 14, 0, 9, 13 },
			{ 11, 14, 12, 2, 7, 4, 13, 5, 1, 15, 0, 10, 9, 3, 6, 8 }, 
			{ 2, 4, 11, 1, 10, 7, 13, 15, 8, 6, 5, 0, 14, 9, 3, 12 },
			{ 12, 11, 8, 7, 1, 2, 14, 13, 15, 6, 10, 9, 5, 4, 3, 0 }
		},
		{ { 12, 10, 15, 1, 2, 9, 8, 6, 0, 3, 13, 14, 4, 11, 7, 5 },
			{ 15, 10, 4, 7, 2, 9, 12, 5, 8, 6, 13, 1, 14, 0, 11, 3 },
			{ 14, 15, 9, 2, 5, 8, 3, 12, 0, 7, 4, 13, 10, 11, 6, 1 },
			{ 3, 4, 12, 2, 5, 9, 10, 15, 14, 1, 11, 6, 7, 0, 13, 8 }
		}};

	// 
	private static byte[] sboxes(byte[] Ri) {
		// We'll split the block into 6-bit blocks
		Ri = splitBytes(Ri, 6); 
		byte[] result = new byte[Ri.length / 2];
		int halfB = 0;
		for (int i = 0; i < Ri.length; i++) {
			byte valByte = Ri[i];
			// get 1 and 6 
			int r = 2 * ((valByte >> 7) & 0x0001) + ((valByte >> 2) & 0x0001);
			// get the median of 4 bits
			int c = (valByte >> 3) & 0x000F; 
			// Switch to 4 bits output instead of 6
			int num = s_boxes[i][r][c]; 
			// 
			if ((i % 2) == 0) {
				// get left half byte
				halfB = num; 
			}
			// 
			else {
				result[i / 2] = (byte) (16 * halfB + num);
			}
		}
		return result;
	} 

	private static byte[] splitBytes(byte[] block, int len) {
		int numOfBytes = (8 * block.length - 1) / len + 1;
		byte[] out = new byte[numOfBytes];
		for (int i = 0; i< numOfBytes; i++) {
			for (int j = 0; j < len; j++) {
				int val = SubkeyGeneration.getBit(block, len * i + j); 
				SubkeyGeneration.setBit(out, 8 * i + j, val);
			}
		}
		return out;
	}

	public static byte[] fFunction(byte[] Ri, byte[] key) {
		// Permuted to 48 bits
		byte[] newR = permute(Ri, table);
		newR = xor(newR, key);
		newR = sboxes(newR);
		newR = permute(newR, p_box);
		return newR;
	}

	// out to a static class
	public static byte[] permute(byte[] input, int[] table) {
		// get number of bytes of the wanted permutation and create a new byte[]
		int bytesNum = (table.length - 1) / 8 + 1;
		byte[] out = new byte[bytesNum];
		// switch bits according to the table
		for (int i = 0; i < table.length; i++) {
			int val = SubkeyGeneration.getBit(input, table[i] - 1);
			SubkeyGeneration.setBit(out, i, val);
		}
		return out;
	}

	// gets a block of 64 bits, permutes with the IP table
	// and calls the round function we implemented
/*	public static byte[] encryptBlock(byte[] block) {
		byte[] Ri = new byte[BLOCK_SIZE / 2];
		byte[] Li = new byte[BLOCK_SIZE / 2];
		currentRound++;

		Li = getHalf(block, 0, (BLOCK_SIZE / 2) - 1);
		Ri = getHalf(block, BLOCK_SIZE / 2, BLOCK_SIZE - 1);

		// do 16 rounds
		for (int i = 0; i < 16; i++) {
			byte[] oldR = Ri;
			Ri = fFunction(Ri, SubkeyGeneration.makeKey(currentRound));
			Ri = xor(Ri, Li);
			Li = oldR;
		}
		return unite(Li, Ri);
	}*/
	public static byte[] encryptBlock(byte[] block) {
		byte [] perm = new byte[BLOCK_SIZE];
		byte[] Ri = new byte[BLOCK_SIZE / 2];
		byte[] Li = new byte[BLOCK_SIZE / 2];
		currentRound++;

		perm = permute(block, IP);

		Li = getHalf(perm, 0, (BLOCK_SIZE / 2) - 1);
		Ri = getHalf(perm, BLOCK_SIZE / 2, BLOCK_SIZE - 1);

		// do 16 rounds
		for (int i = 0; i < 16; i++) {
			byte[] oldR = Ri;
			Ri = fFunction(Ri, SubkeyGeneration.makeKey(currentRound));
			Ri = xor(Li, Ri);
			Li = oldR;
		}
		perm = unite(Li, Ri);
		perm = permute(perm, invIP);
		return perm;
	}
	

	public static byte[] decryptBlock(byte[] block) {
		byte[] Ri = new byte[BLOCK_SIZE / 2];
		byte[] Li = new byte[BLOCK_SIZE / 2];

		currentRound++;

		System.out.println();
		Li = getHalf(block, 0, (BLOCK_SIZE / 2) - 1);
		Ri = getHalf(block, BLOCK_SIZE / 2, BLOCK_SIZE - 1);
		// do 16 rounds
		for (int i = 16; i > 0; i--) {
			byte[] oldR = Ri;
			Ri = fFunction(Ri, SubkeyGeneration.makeKey(i));
			Ri = xor(Ri, Li);
			Li = oldR;
		}

		return unite(Li, Ri);
	}

	// out to a static class
	static byte[] xor(byte[] R, byte[] L) {
		byte[] res = new byte[R.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = (byte)(R[i] ^ L[i]);
		}
		return res;
	}

	// out to a static class
	static byte[] getHalf(byte[] block, int start, int finish) {
		byte[] res = new byte[finish - start + 1];
		//System.out.println("start: " + start + " finish: " + finish + " finish-start:  " + (finish-start));
		for (int i=0; i < (finish - start); i++) {
			res[i] = block[start + i];
		}
		return res;
	}

	// out to a static class
	private static byte[] unite(byte[] l, byte[] r) {
		byte[] union = new byte[BLOCK_SIZE];
		for (int i = 0; i < l.length; i++) {
			union[i] = l[i];
		}
		for (int j = 0; j < r.length; j++) {
			union[j + (BLOCK_SIZE / 2)] = r[j]; 
		}
		return union;
	}
}