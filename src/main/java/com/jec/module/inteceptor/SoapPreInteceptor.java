package com.jec.module.inteceptor;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;

/**
 * Created by jeremyliu on 5/24/16.
 */
public class SoapPreInteceptor extends AbstractPhaseInterceptor<SoapMessage> {
    private Logger log = LoggerFactory.getLogger(SoapPreInteceptor.class);

    public SoapPreInteceptor(){
        super(Phase.PRE_PROTOCOL);
    }

    @Override
    public void handleMessage(SoapMessage soapMessage) throws Fault {
       log.info(soapMessage.getContent(String.class));
    }
}
