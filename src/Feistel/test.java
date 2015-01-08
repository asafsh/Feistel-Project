package Feistel;

public class test {
	public static void main(String[] args) {
		//SubkeyGeneration.initializeKey();
		//CBCmode.CBCstart();
		int[] A = new int[] {1, 5, 7, 9, 10, 50, 5000, 50000, 70000};
		int val = 70000;
		int size = 9;
		int first = 0;
		int last = size -1;
		int mid = 0;
		while (last - first > 1) {
			 mid = (last-first)/2 + first;
			if (A[mid] == val) {
				break;  
			}	
			if (A[mid] > val) {
				last = mid;
				continue;
			} else {
				first = mid;
				continue;  
			}
		}
		System.out.println(mid);
	}
}
