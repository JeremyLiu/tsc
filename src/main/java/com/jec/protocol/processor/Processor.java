package com.jec.protocol.processor;

import com.jec.protocol.pdu.PDU;

/**
 * Created by jeremyliu on 5/9/16.
 */
public interface Processor {
    boolean process(PDU pdu);

}
