package com.jec.protocol.command;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.jec.protocol.pdu.PDU;

public class PduPostor {

	/**
	 * UDP套接字
	 */
	DatagramSocket socket = null;
	
	/*
	 * 
	 * 
	 * 
	 */
	public PduPostor() {
	}
		
	
	
	//
	//
	//
	//
	//
	//
	//
	public boolean prepare() {
		
		/*
		 * 创建socket
		 */		
		try {
			
			socket = new DatagramSocket();
			
			return true;
			
		} catch (SocketException e) {
			
			return false;
			
		} catch (SecurityException e) {
			
			return false;
			
		} catch (Exception e) {
			
			return false;
			
		} 
		
	}
	
	
	
	public void cleanup() {
		
		if(socket != null) {
		
			socket.close();
			
			socket = null;
		
		}
		
	}
	
	
	public boolean prepared() {
		
		
		/*
		 * 检测socket
		 */
		if(socket == null || socket.isClosed()) {

			return false;
			
		}
		
		return true;
		
	}
	
	
	public boolean post(PDU pdu, String remoteHost, int remotePort) {
		
		if(!prepared()) {
			
			System.out.println("PduPostor post failed for not prepared.");
			
			return false;
			
		}
		
		/*
		 * 检测网络参数
		 */
		InetSocketAddress address = null;
		
		try {
		
			address = new InetSocketAddress(remoteHost, remotePort);
			
		} catch(IllegalArgumentException e) {
			
			e.printStackTrace();
			
			return false;
			
		}
		
		if(address.isUnresolved()) {

			System.out.println("无法解析的主机地址: " + remoteHost);
			
			return false;
			
		}
		
		/*
		 * 创建请求UDP包
		 */
		DatagramPacket requestPacket = 
			new DatagramPacket(pdu.buffer(), pdu.offset(), pdu.length());
		
		requestPacket.setSocketAddress(address);
		
		
		/*
		 * 发送PDU
		 */
		try {
			
			socket.send(requestPacket);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
			return false;
			
		} 
		
		return true;

		
	}

	
	
	
	
	
}
