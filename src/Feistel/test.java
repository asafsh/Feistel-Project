package Feistel;

public class test {
	public static void main(String[] args) {
		byte[] iv = new byte[FeistelFunction.BLOCK_SIZE];
		iv = setIv(iv);
		for (int i = 0; i < iv.length; i++) {
			System.out.print(iv[i]);
		}
	}
	
    public static byte[] setIv( byte[] iv ) { 
    	// Set the VI to be 0101.... at the size of the block. 
    	System.out.println("iv length:" + iv.length);
    	System.out.println("block size:" + FeistelFunction.BLOCK_SIZE);
    	for (int i = 0; i < (FeistelFunction.BLOCK_SIZE); i++) { 
    		iv[i] = (byte) (i % 2); 
    	} 
    	return iv;
    } 
}
