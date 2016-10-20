package main;

import java.nio.ByteBuffer;

import core.MyCipher;
import utils.*;

public class Test {
	public static void main(String[] args) {
		//long a = -4;
		String s = "halo halo";
		byte[] bytes = s.getBytes();
		String s2 = new String(bytes);
		System.out.println(s2);
		//String s = "abcdef";
		//System.out.println(s.substring(0, 6));
		/*
		String slong = Long.toBinaryString(a);
		BitMatrix bm = new BitMatrix(slong);
		long b = BitUtils.bitsToLong(bm.getFlipString());
		String slong2 = Long.toBinaryString(b);
		BitMatrix bm2 = new BitMatrix(slong2);
		long c = BitUtils.bitsToLong(bm2.getFlipString());
		System.out.println(c);
		*/
		byte[] text = new byte[16];
		byte[] key = new byte[16];
		for(int i = 0; i < 16; i++) {
			text[i] = (byte) i;
			key[i] = (byte) (i + 1);
		}
		
		MyCipher mc = new MyCipher(100);
		byte[] b = mc.encrypt(text, key);
		
		for(int i = 0; i < b.length; i++) {
			System.out.print(text[i] + " ");
		}
		System.out.println();
		for(int i = 0; i < b.length; i++) {
			System.out.print(b[i] + " ");
		}
		System.out.println();
		
		byte[] c = mc.decrypt(b, key);
		for(int i = 0; i < b.length; i++) {
			System.out.print(c[i] + " ");
		}
	}
}
