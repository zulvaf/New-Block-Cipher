package main;
import core.MyCipher;
import utils.IOFile;
public class Test {
	public static void main(String[] args) {
		MyCipher mc = new MyCipher();
		IOFile iofile = new IOFile();
		String text = iofile.readText("mars.txt");
		String key = "informatikasteii";
		byte[] btext = text.getBytes();
		byte[] bkey = key.getBytes();
		
		int[] frtext = new int[256];
		int[] frres = new int[256];
		for(int i = 0; i < 256; i++) {
			frtext[i] = 0;
			frres[i] = 0;
		}
		
		for(int i = 0; i < btext.length; i++) {
			int idx = (int) btext[i] + 128;
			frtext[idx] += 1; 
		}
		
		byte[] bres = mc.encrypt(btext, bkey);
		for(int i = 0; i < bres.length; i++) {
			int idx = (int) bres[i] + 128;
			frres[idx] += 1; 
		}
		String resAnalisis = getResult(frtext,frres);
		iofile.writeText("result.csv", resAnalisis);
		
	}
	
	public static String getResult(int[] res, int[] res2) {
		String S = "";
		for(int i = 0; i < res.length; i++) {
			S += Integer.toString(res[i]) + ", " + Integer.toString(res2[i]) + "\n";
		}
		return S;
	}
}
