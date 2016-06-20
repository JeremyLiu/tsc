package com.jec.inteceptor;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * Created by jeremyliu on 5/24/16.
 */
public class SoapPostInteceptor extends AbstractPhaseInterceptor<SoapMessage> {

    public SoapPostInteceptor(){
        super(Phase.PRE_PROTOCOL);
    }

    @Override
    public void handleMessage(SoapMessage soapMessage) throws Fault {

    }
}
