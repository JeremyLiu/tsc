package com.jec.module.extern;

import com.jec.inteceptor.SoapPostInteceptor;
import com.jec.inteceptor.SoapPreInteceptor;
import com.jec.module.sysmonitor.entity.ConnectState;
import org.springframework.stereotype.Service;

import javax.jws.WebResult;
import javax.jws.WebService;
import org.apache.cxf.interceptor.InInterceptors;

import java.util.List;

/**
 * Created by jeremyliu on 5/17/16.
 */
@WebService(name = "ZHNMS_Service",
        portName = "NTNMS_EndPoint",
        endpointInterface = "com.jec.module.extern.TestService",
        targetNamespace = "http://ntnms.webservice.com")
@InInterceptors()
public interface NTNMS_Service {

    @WebResult
    String QueryRemoteRequest(String msg);

}
