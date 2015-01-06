package Feistel;
import java.util.ArrayList;

public class FeistelFunction {

	final static int BLOCK_SIZE = 64;
	final static int KEY_SIZE = 56;
	final int PERMUTATED_BLOCK = 48;
	private byte[] plaintext;
	private int currentRound = 0;

	//change numbers
	private static final byte[] Etable = {
		32, 1,  2,  3,  4,  5,   
		12, 13, 14, 15, 16, 17,
		16, 17, 18, 19, 20, 21,
		20, 21, 22, 23, 24, 25,
		24, 25, 26, 27, 28, 29,
		4,  5,  6,  7,  8,  9,
		8,  9,  10, 11, 12, 13,
		28, 29, 30, 31, 32, 1
	};

	//gets Ri and a sub-key and 
	public byte[] fFunction(byte[] Ri, byte[] key) {
		byte newR; // = permutation function
		newR = xor(newR, key);
		//newR = 
		
	}

	public static byte[] perm32To48(byte[] in, byte[] table) {

	}

	public byte[] encryptBlock(byte[] block, byte[] key) {
		byte[] permutatedBlock = new byte[BLOCK_SIZE];
		byte[] Ri = new byte[BLOCK_SIZE / 2];
		byte[] Li = new byte[BLOCK_SIZE / 2];

		permutatedBlock = perm32To48(block, Etable);
		//Li = getHalf(block, 0, PERMUTATED_BLOCK / 2 - 1);
		//Ri = getHalf(block, PERMUTATED_BLOCK / 2, PERMUTATED_BLOCK - 1);

		for (int i = 0; i < 16; i++) {
			byte[] oldR = Ri;
			//Ri = fFunction(Ri, makeKey(currentRound));
			Ri = xor(Ri, Li);
			Li = oldR;
		}
	}
	
	public static byte[] getHalf(byte[] block, int start, int finish) {
		byte[] res = new byte[finish-start];
		for (int i=0; i < (finish-start); i++) {
			res[i] = block[start + i];
		}
		return res;
	}
	
	private static byte[] xor(byte[] R, byte[] L) {
		byte[] res = new byte[R.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = (byte)(R[i] ^ L[i]);
		}
	}

	//public ArrayList <byte[]> encodeFeistel(String plaintext);
	//public String decodeFeistel(ArrayList <byte[]>);      
	String encodeString ="";         
	public byte[] functionF(){ 
		byte[] returnvalue = new byte[2]; 
		returnvalue [0] = 1;
		returnvalue [1] = 0;
		return returnvalue;                
	} 

	public String encode(String plaintext, int length) {
		byte[] ri,li,temp;
		ArrayList<String> hilfsText = block(plaintext,length);
		//System.out.print(hilfsText);
		ArrayList<String> encoded = new ArrayList<String>();
		for(String block:hilfsText) {
			String L0 = block.substring(0, 2);
			String R0 = block.substring(2, 4);
			System.out.println(L0 +""+ R0);
			ri = R0.getBytes();
			li = L0.getBytes();
			temp = functionF();
			//mit Key XOR
			for(int i = 0;i < ri.length;i++) {
				temp[i] = (byte) (ri[i]^temp[i]);
			}
			//mit LI XOR
			for(int i = 0;i < ri.length;i++) {
				temp[i] = (byte) (temp[i]^li[i]);                       
			}
			li = ri;
			System.out.println(temp+""+ri);
			encoded.add(li.toString()+temp.toString());
		}
		return Block2String(encoded);
	}
	/*
	 * Decoden
	 */
	public String decode(String codedText, int length)
	{
		byte[] ri,li,temp;
		ArrayList<String> hilfsText = block(codedText,length);
		System.out.print(hilfsText);
		ArrayList<String> encoded = new ArrayList<String>();
		for(String block:hilfsText) {
			String L0 = block.substring(2, 4);
			String R0 = block.substring(0, 2);
			ri = R0.getBytes();
			li = L0.getBytes();
			temp = functionF();
			//mit Key XOR
			for(int i = 0;i < ri.length;i++) {
				temp[i] = (byte) (temp[i]^ri[i]);
			}
			//mit LI XOR
			for(int i = 0;i < ri.length;i++)
			{
				temp[i] = (byte) (li[i]^temp[i]);
			}
			li = ri;
			encoded.add(ri.toString()+temp.toString());
		}
		return Block2String(encoded);
	}
	/*
	 * Blockifizieren
	 */
	//Blockifizieren
	static ArrayList<String> block(String plaintext, int key)
	{
		ArrayList<String> blocks= new ArrayList<String>();
		while(plaintext.length()>=key)
		{
			blocks.add(plaintext.subSequence(0, key).toString());
			plaintext = plaintext.subSequence(key,plaintext.length()).toString();
		}
		if(plaintext.length() == 0)
		{                         
		} 
		else 
		{ 
			while(plaintext.length() < key) 
			{
				plaintext += (char)randomGenerator();
			}
			blocks.add(plaintext.subSequence(0, plaintext.length()).toString());
		}
		return blocks;
	}      

	private static int randomGenerator(){                  
		Random generator = new Random();
		int pad = generator.nextInt(255);        
		return pad;        
	}
	//Aus Arraylist wieder String machen
	private static String Block2String(ArrayList<String> cryptText) {
		String text="";
		for(String block:cryptText) {
			text += block;
		}
		return text;
	}

	// A sub-key generator
	private static int[] subKeyGenerator(String key) {

	}
}

