package utils;

public class BitMatrix {
	private int[][] bitmat;
	public BitMatrix() {
		bitmat = new int[8][8];
	}
	
	public int[][] getFlip(long val) {
		String bits = Long.toBinaryString(val);
		int k = 0;
		int length = bits.length();
		for(int i = 0; i < bitmat.length; i++) {
			for(int j = 0; j < bitmat[0].length; j++) {
				if(length < 64) {
					bitmat[i][j] = 0;
					length++;
				} else {
					String bval = "" + bits.charAt(k);
					bitmat[i][j] = Integer.parseInt(bval);
					k++;
				}
			}
		}
		
		
		int [][] flipmat = new int[8][8];
		for(int i = 0; i < bitmat.length; i++) {
			for(int j = 0; j < bitmat[0].length; j++) {
				flipmat[i][bitmat[0].length - j - 1] = bitmat[i][j];
			}
		}
		return flipmat;
	}
	
	public String getFlipString(long val) {
		return getString(getFlip(val));
	}
	
	public long getFlipLong(long val) {
		return BitUtils.bitsToLong(getFlipString(val));
	}
	
	public int[][] getBitmat() {
		return bitmat;
	}
	
	@Override
	public String toString() {
		return getString(bitmat);
	}
	
	public String getString(int[][] bits) {
		String S = "";
		for(int i = 0; i < bits.length; i++) {
			for(int j = 0; j < bits[0].length; j++) {
				S += bits[i][j];
			}
		}
		return S;
	}
	
}
