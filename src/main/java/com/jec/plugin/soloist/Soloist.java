package com.jec.plugin.soloist;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import com.jec.plugin.framework.Plugin;

public class Soloist implements Plugin {
	
	int port = 8120;

	ServerSocket socket = null;
	
	public Soloist(int port) {
		this.port = port;
	}
	
	
	
	@Override
	public void start() throws Exception {
		
		try {
			
			socket = new ServerSocket(port);
			
		} catch (BindException e){
			
			throw(new RuntimeException("应用程序已经运行，应用程序同时只能运行一个实例", e));
			
		} catch (IOException e) {
			
			throw(new RuntimeException("Solist启动失败！", e));		
		}		
	}

	@Override
	public void stop() {
		try {
			if(socket != null) {
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "lingdm";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "保证应用程序只有一个运行实例";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Solist";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0";
	}

}
