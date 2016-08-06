package com.jec.module.sysmonitor.service;

import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.protocol.pdu.implement.BufferedPdu;
import com.jec.protocol.processor.Processor;
import com.jec.protocol.processor.RawProcessor;
import com.jec.utils.NetWorkUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by jeremyliu on 5/11/16.
 */
@Service
@Scope
public class NetWorkListenerService{

    private boolean exit = false;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();

    private Map<Integer, List<Processor>> processors = new HashMap<Integer, List<Processor>>();

    private List<RawProcessor> listeners = new ArrayList<>();


    public void addProcessor(int type, Processor processor) {
        if (processor == null)
            return;
        if (processors.containsKey(type)) {
            List<Processor> list = processors.get(type);
            list.add(processor);
        } else {
            List<Processor> list = new ArrayList<>();
            list.add(processor);
            processors.put(type, list);
        }
    }

    public void addListener(RawProcessor rawProcessor){
        listeners.add(rawProcessor);
    }

    public void removeListner(RawProcessor rawProcessor){
        listeners.remove(rawProcessor);
    }

    public void removeProcessor(Processor processor) {
        for (List<Processor> list : processors.values())
            if (list.remove(processor))
                break;
    }

    public synchronized void stopListen() {
        exit = true;
    }

    public synchronized boolean isExit(){
        return exit;
    }

    private void parsePdu(PDU pdu) throws Exception {

        int length = pdu.length();

        // check pdu
        if(PduConstants.ID_LOCAL != ProtocolUtils.getDestId(pdu)) {
            System.out.println("目的站号错误," + pdu.toString());
        }

        if(PduConstants.PROTOCOL_TYPE != ProtocolUtils.getProtocolType(pdu)) {
            System.out.println("协议类型错误," + pdu.toString());
        }

        if((length - 6) != ProtocolUtils.getBodySize(pdu)) {
            System.out.println("报文长度错误," + pdu.toString());
        }

        int cmdType = ProtocolUtils.getCmdType(pdu);

        List<Processor> list = processors.get(cmdType);
        if(list != null){
            for(Processor processor: list)
                processor.process(pdu);
        }
    }

    @PostConstruct
    public void startListen() {
        exit = false;
        ListenerThread worker = new ListenerThread();
        worker.start();
    }

    private class ListenerThread extends Thread {

        private DatagramSocket socket;

        @Override
        public void run() {

            while (socket == null && !exit) {

                try {

                    socket = new DatagramSocket(
                            NetWorkUtil.listenPort,
                            InetAddress.getByName(NetWorkUtil.getLocalHost()));

                    socket.setSoTimeout(5000);

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



//            BufferedPdu pdu = new BufferedPdu();
            boolean done = false;
            TransactionSynchronizationManager.initSynchronization();
            while (!exit) {

                try {
                    byte[] buffer = new byte[1024 * 10];

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    for (RawProcessor listener : listeners) {
                        if (listener.process(packet)) {
                            done = true;
                            break;
                        }
                    }
                    if (done)
                        continue;

                    byte[] data = packet.getData();
                    int offset = packet.getOffset();
                    int length = packet.getLength();
                    BufferedPdu pdu = new BufferedPdu();
                    pdu.setData(data, offset, length);

                    // 解析
                    parsePdu(pdu);
//                    executor.submit(new Worker(packet));
                } catch (SocketTimeoutException e) {

                    e.printStackTrace();

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

    private class Worker implements Runnable{

        private DatagramPacket packet;

        public Worker(DatagramPacket packet){
            this.packet = packet;
        }

        @Override
        public void run(){
            boolean done = false;
            TransactionSynchronizationManager.initSynchronization();
            try {
                for (RawProcessor listener : listeners) {
                    if (listener.process(packet)) {
                        done = true;
                        break;
                    }
                }
                if (done)
                    return;

                byte[] data = packet.getData();
                int offset = packet.getOffset();
                int length = packet.getLength();
                BufferedPdu pdu = new BufferedPdu();
                pdu.setData(data, offset, length);

                // 解析
                parsePdu(pdu);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
