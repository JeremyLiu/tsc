package com.jec.utils.tool;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

public class PropertiesWrap {

	private Properties properties = new Properties();

	public PropertiesWrap() {
	}
	
	public void load(String file) throws IOException {
		
		FileReader fr = new FileReader(file);
		
		try {
			
			properties.load(fr);
			
		} finally {
			
			if(fr != null) {
				fr.close();
			}
		}
	}
	
	public void save(String file) throws IOException {
		
		FileOutputStream fos = null;
		PrintWriter pw = null;
		
		try {
			
			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			properties.store(pw, "PropertiesWrap by olins");
		
		} finally {
			
			if(pw != null) {
				pw.close();
			}
			
			if(fos != null) {
				fos.close();
			}
		}
	}
	
	public String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	public void set(String key, String value) {
		properties.setProperty(key, value);
	}	
	
	public int get(String key, int defaultValue) {
		String value = properties.getProperty(key, Integer.toString(defaultValue));
		return Integer.parseInt(value);
	}
	
	public void set(String key, int value) {
		properties.setProperty(key, Integer.toString(value));
	}	
	
	/*
	public static void main(String arg[]) throws IOException {
		PropertiesWrap pw = new PropertiesWrap();
		pw.set("host", "192.23.0.1");
		pw.set("port", 3599);
		pw.set("text", "我是中国人！");
		pw.save("a.properties");
		
		PropertiesWrap pw2 = new PropertiesWrap();
		pw2.load("a.properties");
		String host = pw2.get("host", "192.23.0.2");
		int port = pw2.get("port", 35929);
		String text = pw2.get("text", "");

		System.out.println(host + " " + port + " " + text);

	}
	*/
}
