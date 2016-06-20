package com.jec.protocol.command;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.implement.DefaultPduBuilder;

public class CommandExecutor {
	
	/**
	 * 如果execute()返回的Result失败，可以检测Result.getCode()
	 * <p>Result.getCode() == ERR_COMMAND表示是Command执行时发生了错误
	 */
	public static final int ERR_COMMAND = -1;
	
	/**
	 * 指令发送的目标主机 
	 */
	private String remoteHost;
	
	/**
	 * 指令发送的目标端口
	 */
	private int remotePort;
	
	/**
	 * 指令超时毫秒数
	 */
	private int timeout = 3000;
	
	/**
	 * UDP套接字
	 */
	DatagramSocket socket = null;
	
	
	/**
	 * 自动打开套接字/关闭套接字
	 */
	private boolean auto = true;
	
		
	public CommandExecutor() {
		super();
	}
	
	public CommandExecutor(boolean auto) {
		super();
		this.auto = auto;
	}


	public String getRemoteHost() {
		return remoteHost;
	}


	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}


	public int getRemotePort() {
		return remotePort;
	}


	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	
	public int getTimeout() {
		return timeout;
	}


	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public void setRemoteAddress(String host,int port) {
		this.setRemoteHost(host);
		this.setRemotePort(port);
	}

	public Result execute(Command command) {
		
		/*
		 * 创建socket
		 */		
		try {
			
			if(auto) {

				socket = new DatagramSocket();
			
			}

			return execute0(command);
			
		} catch (SocketException e) {
			e.printStackTrace();
			
			return Result.newFailure("套接字异常");
			
		} catch (SecurityException e) {
			e.printStackTrace();
			return Result.newFailure("系统安全策略限制，无法创建套接字！");	
			
		} finally {

			if(auto && socket != null) {
			
				socket.close();
				
				socket = null;
			
			}
			
		}
		
	}
	
	public Result open() {
		
		try {
			
			if(!auto) {
			
				close();
				
				socket = new DatagramSocket();
			
			}
			
			return Result.newSuccess();

			
		} catch (SocketException e) {
			
			return Result.newFailure("套接字异常");
			
		} catch (SecurityException e) {
			
			return Result.newFailure("系统安全策略限制，无法创建套接字！");	
			
		} 
		
	}
	
	public void close() {
		
		if(!auto && socket != null) {
			
			if(!socket.isClosed()) {
			
				socket.close();
				
			}
			
			socket = null;
		
		}
	}
	
	
	
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	private Result execute0(Command command) {
		
		/*
		 * 检测command参数
		 */
		if(command == null) {

			return Result.newFailure("command参数为空,无法执行!");
			
		}

		
		/*
		 * 验证参数, 创建请求PDU
		 */
		try {
			command.prepareRequest();
		} catch(Exception e) {
			e.printStackTrace();
			return Result.newFailure(e.getMessage(), ERR_COMMAND);
		}

		
		/*
		 * 检测socket
		 */
		if(socket == null || socket.isClosed()) {

			return Result.newFailure("套接字未准备好,无法执行!");	
			
		}
		
		/*
		 * 检测网络参数
		 */
		InetSocketAddress address = null;
		
		try {
		
			address = new InetSocketAddress(remoteHost, remotePort);
			
		} catch(IllegalArgumentException e) {
			
			return Result.newFailure(e.getLocalizedMessage());	
			
		}
		
		if(address.isUnresolved()) {

			return Result.newFailure("无法解析的主机地址: " + remoteHost);
			
		}
		
		
		/*
		 * 检测超时设置
		 */
		try {
			
			socket.setSoTimeout(Math.max(200, timeout));
			
		} catch (SocketException e) {
			
			return Result.newFailure("套接字超时设置失败!");		
			
		}
		
		/*
		 * 创建请求UDP包
		 */
		DatagramPacket requestPacket = 
			new DatagramPacket(command.request.buffer(), command.request.offset(), command.request.length());
		
		requestPacket.setSocketAddress(address);
		
		/*
		 * 创建回应UDP包
		 */
		DatagramPacket responsePacket = new DatagramPacket(new byte[2048], 2048);
		

		
		/*
		 * 发送请求,接收回复
		 */
		try {
			
			socket.send(requestPacket);
			
			if(command.isNeedResponse()) {
				socket.receive(responsePacket);
			} else {
				return Result.newSuccess("指令执行成功");
			}
			
		} catch (SocketTimeoutException e) {

			return Result.newFailure("等待网络回复超时!");	
			
		} catch (IOException e) {
			
			return Result.newFailure("网络通信异常!");	
			
		} 
		
		/*
		 * 将接收到的回复数据转换为PDU
		 */
		DefaultPduBuilder builder = new DefaultPduBuilder(
				responsePacket.getData(), 
				responsePacket.getOffset(), 
				responsePacket.getLength());
		PDU response = builder.buildPdu();
		if(response == null) {
			
			return Result.newFailure("根据回复数据无法创建协议数据单元!");	
			
		}
		
		/*
		 * 解析回复PDU, 验证回复
		 */
		try {
			command.processResponse(response);
		} catch(Exception e) {
			e.printStackTrace();
			return Result.newFailure(e.getMessage(), ERR_COMMAND);
			
		}
		
		/*
		 * 返回结果
		 */
		return Result.newSuccess("指令执行成功");
		
	}
		
}
