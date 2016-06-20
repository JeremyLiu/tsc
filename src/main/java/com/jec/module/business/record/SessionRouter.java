package com.jec.module.business.record;

import com.jec.base.core.PipeTask;
import com.jec.module.business.entity.Record;
import com.jec.protocol.unit.BytesWrap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeremyliu on 6/19/16.
 */
public class SessionRouter  {

    private Map<Integer,RecordSession> sessionMap = new HashMap<>();

    public RecordSession route(int id,String callingNum, String calledNum, BytesWrap data){
        RecordSession session;
        if(sessionMap.containsKey(id))
            session = sessionMap.get(id);
        else
            session = new RecordSession();
    }

}
