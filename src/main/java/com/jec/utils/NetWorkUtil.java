package com.jec.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by jeremyliu on 5/10/16.
 */
public class NetWorkUtil {

    public static int listenPort = 3600;

    public static int recordListenPort = 6001;

    public static String getLocalHost() {

        try {
            Enumeration<NetworkInterface> enitf = NetworkInterface.getNetworkInterfaces();
            while(enitf.hasMoreElements()) {
                NetworkInterface nitf = enitf.nextElement();
                Enumeration<InetAddress> eia = nitf.getInetAddresses();
                while(eia.hasMoreElements()) {
                    InetAddress ia = eia.nextElement();

                    if(ia instanceof Inet4Address) {
                        String address = ia.getHostAddress();
                        return address;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }
}
