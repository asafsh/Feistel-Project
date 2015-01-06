package Feistel;

import java.util.ArrayList;

import Feistel.SubkeyGeneration;

public class FeistelFunction {

	final static int BLOCK_SIZE = 64;
	final static int KEY_SIZE = 56;
	final static int PERMUTATED_BLOCK = 48;
	private static int currentRound = 0;

	//change numbers
	private static final byte[] table = {
		32, 1,  2,  3,  4,  5,   
		12, 13, 14, 15, 16, 17,
		16, 17, 18, 19, 20, 21,
		20, 21, 22, 23, 24, 25,
		24, 25, 26, 27, 28, 29,
		4,  5,  6,  7,  8,  9,
		8,  9,  10, 11, 12, 13,
		28, 29, 30, 31, 32, 1
	};

	private static final byte[] s_box1 = {
		10, 9, 0, 14, 6, 15, 3, 5, 1, 13, 7, 12, 4, 11, 8, 2,
		7, 13, 9, 0, 3, 6, 4, 10, 2, 8, 5, 12, 14, 11, 1, 15,
		6, 13, 4, 8, 9, 15, 0, 3, 1, 11, 12, 2, 5, 14, 10, 7,
		10, 1, 13, 8, 9, 7, 4, 7, 15, 3, 14, 11, 5, 11, 12, 2
	};

	private static final byte[] s_box2 = {
		14, 4, 1, 13, 15, 2, 8, 11, 10, 8, 3, 12, 6, 5, 9, 7,
		15, 4, 7, 14, 13, 2, 13, 1, 6, 10, 12, 11, 3, 9, 5, 8,
		4, 1, 14, 13, 8, 6, 2, 11, 15, 9, 12, 7, 3, 10, 0, 5,
		15, 12, 2, 8, 9, 4, 1, 7, 11, 5, 14, 3, 10, 0, 13, 6
	};

	private static final byte[] s_box3 = {
		15, 8, 1, 14, 6, 3, 9, 11, 4, 7, 3, 2, 12, 0, 13, 5,
		13, 3, 7, 4, 15, 8, 2, 12, 14, 10, 1, 10, 9, 6, 11,
		14, 0, 7, 10, 11, 4, 1, 13, 5, 12, 8, 6, 3, 15, 2, 9,
		8, 13, 1, 10, 15, 2, 11, 6, 3, 4, 6, 12, 7, 0, 14, 5
	};

	private static final byte[] s_box4 = {
		13, 8, 2, 4, 6, 11, 15, 11, 10, 3, 9, 0, 14, 12, 7, 5,
		15, 1, 10, 8, 7, 3, 13, 4, 12, 6, 5, 11, 14, 0, 9, 2,
		11, 4, 7, 1, 12, 9, 14, 0, 2, 6, 0, 13, 10, 5, 3, 15,
		1, 14, 2, 7, 4, 10, 13, 10, 15, 9, 3, 12, 6, 0, 11, 5
	};

	private static final byte[] s_box5 = {
		13, 7, 3, 14, 6, 0, 10, 9, 2, 1, 5, 8, 11, 12, 15, 4,
		8, 13, 5, 11, 6, 15, 3, 0, 4, 2, 7, 12, 10, 1, 9, 14,
		10, 9, 6, 12, 0, 11, 13, 7, 15, 3, 1, 14, 2, 5, 4, 8,
		15, 3, 6, 0, 1, 10, 13, 9, 4, 8, 5, 11, 2, 7, 14, 2
	};

	private static final byte[] s_box6 = {
		11, 2, 4, 14, 0, 8, 15, 3, 13, 7, 9, 10, 5, 1, 6, 12,
		13, 0, 11, 4, 7, 1, 9, 10, 3, 14, 5, 2, 12, 6, 8, 15,
		11, 13, 1, 4, 12, 2, 7, 3, 10, 14, 15, 8, 6, 0, 9, 5,
		11, 13, 1, 6, 10, 4, 7, 9, 5, 15, 0, 14, 3, 2, 8, 12
	};

	private static final byte[] s_box7 = {
		12, 4, 2, 7, 1, 10, 11, 8, 6, 5, 15, 3, 14, 0, 9, 13,
		11, 14, 12, 2, 7, 4, 13, 5, 1, 15, 0, 10, 9, 3, 6, 8,
		2, 4, 11, 1, 10, 7, 13, 15, 8, 6, 5, 0, 14, 9, 3, 12,
		12, 11, 8, 7, 1, 2, 14, 13, 15, 6, 10, 9, 5, 4, 3, 0
	};

	private static final byte[] s_box8 = {
		12, 10, 15, 1, 2, 9, 8, 6, 0, 3, 13, 14, 4, 11, 7, 5,
		15, 10, 4, 7, 2, 9, 12, 5, 8, 6, 13, 1, 14, 0, 11, 3,
		14, 15, 9, 2, 5, 8, 3, 12, 0, 7, 4, 13, 10, 11, 6, 1,
		3, 4, 12, 2, 5, 9, 10, 15, 14, 1, 11, 6, 7, 0, 13, 8
	};

	private static byte[] sboxes(byte[] Ri) {
		for (int i = 0; i < Ri.length; i++) {
			byte rByte = Ri[i];
			// take the LSB
			int j = ((rByte >> 7) & 1) | (rByte >> 3);
			int k = (rByte >> 2);

		}
		return null;
	}

	//gets Ri and a sub-key and  
	public byte[] fFunction(byte[] Ri, byte[] key) {
		// Permuted to 48 bits
		byte[] newR = new byte[table.length];
		for (int i = 0; i < table.length; i++) {
			newR[i] = Ri[table[i] - 1];
		}
		newR = xor(newR, key);

		//s box is a must, we need to go back to 32!
		return null;
	}

	public static byte[] perm32To48(byte[] in, byte[] table) {
		return null;
	}

	public static byte[] encryptBlock(byte[] block) {
		byte[] permutatedBlock = new byte[BLOCK_SIZE];
		byte[] Ri = new byte[BLOCK_SIZE / 2];
		byte[] Li = new byte[BLOCK_SIZE / 2];

		currentRound++;

		permutatedBlock = perm32To48(block, table);
		Li = getHalf(block, 0, PERMUTATED_BLOCK / 2 - 1);
		Ri = getHalf(block, PERMUTATED_BLOCK / 2, PERMUTATED_BLOCK - 1);

		// do 16 rounds
		for (int i = 0; i < 16; i++) {
			byte[] oldR = Ri;
			Ri = fFunction(Ri, makeKey(currentRound));
			Ri = xor(Ri, Li);
			Li = oldR;
		}
		
		return unite(Li, Ri);

	}

	public static byte[] decryptBlock(byte[] block) {
		byte[] permutatedBlock = new byte[BLOCK_SIZE];
		byte[] Ri = new byte[BLOCK_SIZE / 2];
		byte[] Li = new byte[BLOCK_SIZE / 2];

		currentRound++;

		permutatedBlock = perm32To48(block, table);
		Li = getHalf(block, 0, PERMUTATED_BLOCK / 2 - 1);
		Ri = getHalf(block, PERMUTATED_BLOCK / 2, PERMUTATED_BLOCK - 1);
		
		// do 16 rounds
		for (int i = 16; i > 0; i--) {
			byte[] oldR = Ri;
			Ri = fFunction(Ri, makeKey(i));
			Ri = xor(Ri, Li);
			Li = oldR;
		}
		
		return unite(Li, Ri);
	}

	static byte[] xor(byte[] R, byte[] L) {
		byte[] res = new byte[R.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = (byte)(R[i] ^ L[i]);
		}
		return null;
	}

	static byte[] getHalf(byte[] block, int start, int finish) {
		byte[] res = new byte[finish-start];
		for (int i=0; i < (finish-start); i++) {
			res[i] = block[start + i];
		}
		return res;
	}

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

