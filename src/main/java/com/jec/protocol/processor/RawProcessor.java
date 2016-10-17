package com.jec.protocol.processor;

import java.net.DatagramPacket;

/**
 * Created by jeremyliu on 6/18/16.
 */
public interface RawProcessor {

    boolean process(DatagramPacket packet);
}
