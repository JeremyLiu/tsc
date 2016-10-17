package com.jec.utils.lang;

import java.io.Closeable;
import java.io.IOException;

public class IoUtils {
	
	public static void close(Closeable obj) {
		if(obj != null) {
			try {
				obj.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
