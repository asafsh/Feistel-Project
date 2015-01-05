package Feistel;
import java.util.ArrayList;

public class FeistelFunction {
	
	final int ROUND_NUMBER = 0;
	final int BLOCK_SIZE = 64;
	final int KEY_SIZE = 56;
	
	//neue Funktionen für Feistel
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
	//Generator für Padding
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
		
	})
} 