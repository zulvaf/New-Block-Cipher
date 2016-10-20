package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IOFile {
	
	private int mode;
	
	public IOFile() {
		setMode(0);
	}
	
	public IOFile(int mode) {
		this.setMode(mode);
	}
	
	public String readText(String FileName) {
		BufferedReader br = null;
		String line = "";
		String res = "";
		try {
	
			br = new BufferedReader(new FileReader(FileName));
			while ((line = br.readLine()) != null) {
			    // use comma as separator
				res += line;
			}
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}
	
	public void writeText(String name, String content) {
		try {
			File file = new File(name);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Getter and setters
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
}
