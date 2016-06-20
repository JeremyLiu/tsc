package com.jec.module.business.record;

import com.jec.protocol.unit.BytesWrap;
import com.jec.utils.Constants;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by jeremyliu on 6/19/16.
 */
public class RecordSession {

    private long lastUpdate = 0;
    private long timeout = Constants.RecordTimeOut;

    private Queue<BytesWrap> queue= new LinkedList<BytesWrap>();
}
