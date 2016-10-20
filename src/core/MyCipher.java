package core;

import java.nio.ByteBuffer;
import java.util.Random;

import utils.BitMatrix;
import utils.BitUtils;

public class MyCipher {
	private int[][] sbox;
	
	public MyCipher() {
		createSBox(5286);
	}
	
	public MyCipher(int seed) {
		createSBox(seed);
	}
	public byte[] encrypt(byte[] text, byte[] key) {
		byte[] newText = completeBlock(text);
		byte[] newKey = completeKey(key);
		byte[][] blocks = bytesToBlocks(text,16);
		ByteBuffer buffer = ByteBuffer.allocate(newText.length);
		for(int i = 0; i < blocks.length; i++) {
			byte[] encBlock = encryptBlock(blocks[i], newKey);
			buffer.put(encBlock);
		}
		return buffer.array();
		
	}
	
	public byte[] decrypt(byte[] text, byte[] key) {
		byte[] newKey = completeKey(key);
		byte[] newText = completeBlock(text);
		byte[][] blocks = bytesToBlocks(text,16);
		ByteBuffer buffer = ByteBuffer.allocate(newText.length);
		for(int i = 0; i < blocks.length; i++) {
			byte[] decBlock = decryptBlock(blocks[i], newKey);
			buffer.put(decBlock);
		}
		return buffer.array();
	}
	
	public byte[] encryptBlock(byte[] text, byte[] key) {
		String skey = BitUtils.getBits(key);
		String stext = BitUtils.getBits(text);
		int round = numRound(key);
		
		BitMatrix bm = new BitMatrix();
		
		long left = splitBits(stext,1);
		long right = splitBits(stext,2);
		
		for(int i = 0; i < round-1; i++) {
			long rightFlip = bm.getFlipLong(right);
			skey = rotateLeft(skey);
			long keyComp = compressKey(skey);
			long temp = rightFlip ^ keyComp;
			temp = temp ^ left;
			left = right;
			right = temp;
		}
		//last round
		long rightFlip = bm.getFlipLong(right);
		skey = rotateLeft(skey);
		long keyComp = compressKey(skey);
		long temp = rightFlip ^ keyComp;
		temp = temp ^ left;
		left = temp;
		
		byte[] leftBytes = BitUtils.longToBytes(left);
		byte[] rightBytes = BitUtils.longToBytes(right);
		return BitUtils.concatBytes(leftBytes, rightBytes);
	}
	
	public byte[] decryptBlock(byte[] text, byte[] key) {
		String skey = getLastKey(key);
		String stext = BitUtils.getBits(text);
		int round = numRound(key);
		BitMatrix bm = new BitMatrix();
		
		long left = splitBits(stext,1);
		long right = splitBits(stext,2);
		
		//first round
		long rightFlip = bm.getFlipLong(right);
		long keyComp = compressKey(skey);
		long temp = rightFlip ^ keyComp;
		temp = temp ^ left;
		left = temp;
		
		for(int i = 0; i < round-1; i++) {
			skey = rotateRight(skey);
			temp = left;
			rightFlip = bm.getFlipLong(left);
			keyComp = compressKey(skey);
			left = rightFlip ^ keyComp;
			left = left ^ right;
			right = temp;
		}

		byte[] leftBytes = BitUtils.longToBytes(left);
		byte[] rightBytes = BitUtils.longToBytes(right);
		return BitUtils.concatBytes(leftBytes, rightBytes);
	}
	
	private int numRound(byte[] key) {
		int res = 0;
		for(int i = 0; i < key.length; i++) {
			res += key[i] & 0xFF;
		}
		res = res % 16;
		res += 10;
		return res;
	}
	
	public void createSBox(long seed) {
		Random rand = new Random(seed);
		sbox = new int[4][4];
		for(int i = 0; i < sbox.length; i++) {
			for(int j = 0; j < sbox.length; j++) {
				int val = rand.nextInt() % 4;
				if(val < 0) {
					val = 4 - val;
				}
				sbox[i][j] = val;
			}
		}
	}
	
	public void createSBox(byte[] key) {
		long seed = compressKey(BitUtils.getBits(key));
		createSBox(seed);
	}
	
	private long splitBits(String bits, int pos) {
		if(bits.length() == 128 && (pos == 1 || pos == 2)) {
			int start = (pos - 1) * 64;
			return BitUtils.bitsToLong(bits.substring(start, start+64));
		} else {
			return 0;
		}
	}
	
	private String getLastKey(byte[] key) {
		String skey = BitUtils.getBits(key);
		int round = numRound(key);
		String res;
		String s = skey.substring(0, round);
		res = skey.substring(round);
		res += s;
		return res;
	} 
	
	private String rotateLeft(String key) {
		String res;
		char c = key.charAt(0);
		res = key.substring(1);
		res += c;
		return res;
	}
	
	private String rotateRight(String key) {
		String res;
		char c = key.charAt(key.length()-1);
		res = key.substring(0, key.length()-1);
		res = c + res;
		return res;
	}
	
	private long compressKey(String key) {
		String res = "";
		
		//split string by 4 char
		String text = key;
		int index = 0;
		while (index < text.length()) {
		    String bits = text.substring(index, Math.min(index + 4,text.length()));
		    String srow = "" + bits.charAt(0) + bits.charAt(3);
		    String scol = "" + bits.charAt(1) + bits.charAt(2);
		    int row = Integer.parseInt(srow, 2);
		    int col = Integer.parseInt(scol, 2);
		    res += BitUtils.get2Bits(sbox[row][col]);
		    index += 4;
		}
		return BitUtils.bitsToLong(res);
		
	}
	
	private byte[] completeBlock(byte[] text) {
		if(text.length % 16 == 0) {
			return text;
		} else {
			int length = (text.length/16 + 1) * 16;
			byte[] newText = new byte[length];
			int i;
			for(i = 0; i < text.length; i++) {
				newText[i] = text[i];
			}
			while(i < newText.length) {
				newText[i] = 0;
				i++;
			}
			return newText;
		}
	}
	
	private byte[] completeKey(byte[] key) {
		if(key.length > 16) {
			byte[] newKey = new byte[16];
			for(int i = 0; i < newKey.length; i++) {
				newKey[i] = key[i];
			}
			return newKey;
		} else {
			return completeBlock(key);
		}
	}
	
	private byte[][] bytesToBlocks(byte[] bytes, int size) {
		int numBlock = bytes.length / size;
		byte[][] blocks = new byte[numBlock][size];
		for(int i = 0; i < blocks.length; i++) {
			System.arraycopy(bytes, i*size, blocks[i], 0, size);
		}
		return blocks;
	}
}
