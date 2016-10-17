package com.jec.module.business.service;

import com.jec.protocol.pdu.implement.BufferedPdu;
import com.jec.utils.NetWorkUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.net.*;

/**
 * Created by jeremyliu on 6/18/16.
 */
@Service
@Scope
public class RecordListenerService {

    @Resource
    private RecordService recordService;

    private boolean exit = false;

    public synchronized void stopListen() {
        exit = true;
    }


    @PostConstruct
    public void startListener(){
        exit =false;
        new ListenerThread().start();
    }

    private class ListenerThread extends Thread {

        private DatagramSocket socket;

        @Override
        public void run() {

            while (socket == null && !exit) {

                try {

                    socket = new DatagramSocket(
                            NetWorkUtil.recordListenPort,
                            InetAddress.getByName(NetWorkUtil.getLocalHost()));

                    socket.setSoTimeout(2000);

                } catch (SocketException e) {

                    e.printStackTrace();
                    exit = true;
                    socket = null;
                    return;

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                if (socket == null) {

                    try {

                        Thread.sleep(5000);

                    } catch (InterruptedException e) {

                        exit = true;

                        return;

                    }

                }

            }

            //debug.debug("创建监听套接字成功，地址：" + MonitorAddress.getMonitorHost() + " 端口：" + MonitorAddress.getMonitorPort());

            byte[] buffer = new byte[1024 * 10];

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            BufferedPdu pdu = new BufferedPdu();

            while (!exit) {

                try {

                    socket.receive(packet);
                    recordService.process(packet);

                } catch (SocketTimeoutException e) {

//                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                    stopListen();
                    break;

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }
        }

    }

}
