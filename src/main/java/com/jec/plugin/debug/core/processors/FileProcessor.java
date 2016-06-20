package com.jec.plugin.debug.core.processors;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

import com.jec.plugin.debug.Debugger.Level;
import com.jec.plugin.debug.core.DebugEntry;
import com.jec.plugin.debug.core.DebugProcessor;

public class FileProcessor implements DebugProcessor{
	
	static final String FileName = "debug.txt";
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //  @jve:decl-index=0:

	FileOutputStream fos = null;
	PrintStream ps = null;

	public FileProcessor() throws Exception {
		super();
		fos = new FileOutputStream(FileName);
		ps = new PrintStream(fos, true);
		ps.println("================================================");
		ps.println("File Log V1.0 By Olins");
		ps.println("Start at " + dateFormat.format(System.currentTimeMillis()));
		ps.println("================================================");
	}

	@Override
	public void onEntry(DebugEntry entry) {

		StringBuilder sb = new StringBuilder();
		sb.append("【");
		sb.append(dateFormat.format(entry.getTime()));
		sb.append("】【");
		sb.append(levelToText(entry.getLevel()));
		sb.append("】【");
		sb.append(makeLength(entry.getModule(), 16));
		sb.append("】 ");
		sb.append(entry.getContent());
		ps.println(sb.toString());	
		
		/* 
		// line return support
		String[] contents = entry.getContent().split("\r\n");

		StringBuilder sb = new StringBuilder();
		sb.append("【");
		sb.append(dateFormat.format(entry.getTime()));
		sb.append("】【");
		sb.append(levelToText(entry.getLevel()));
		sb.append("】【");
		sb.append(makeLength(entry.getModule(), 16));
		sb.append("】 ");
		//sb.append(entry.getContent());
		for(String content : contents) {
			ps.println(sb.toString() + content);		
		}
		 
		 */
	}
	
	public String levelToText(Level level) {
		switch(level) {
		case FATAL: return "致命";
		case ERROR: return "错误";
		case WARNING: return "警告";
		case MESSAGE: return "信息";
		case DEBUG: return "调试";
		default: return "未知";
		}
	}
	
	public static String makeLength(String source, int len) {
		
		if(len <= 0) {
			return "";
		}
		
		if(source == null) {
			source = "";
		}
		
		if(source.length() > len) {
			return source.substring(0, len);
		}
		
		String result = source;
		
		while(result.length() < len) {
			result += " ";
		}
		
		return result;
		
	}
	
}
