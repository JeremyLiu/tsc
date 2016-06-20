package com.jec.module.business.manage;

import com.jec.base.entity.MonitorConstant;
import com.jec.base.entity.NetState;
import com.jec.module.business.entity.Business;
import com.jec.module.business.entity.Meeting;
import com.jec.protocol.pdu.PDU;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jeremyliu on 5/18/16.
 */
abstract public class BusinessManager<T extends NetState> {

    protected List<T> entries = new CopyOnWriteArrayList<T>();

    protected Business business;

    public  void setEntry(T entry){
        if(entry == null) {
            return;
        }

        // find the broadcast
        for(T e : this.entries) {

            if(e.equalWith(entry)) {


                if(entry.getState() == MonitorConstant.BUSINESS_STATE_IDLE) {

                    // if new state is idle

//						e.st entry.state;

                    entries.remove(e);

                    return;

                } else {

                    // if new state is busy
                    e.from(entry);

                    return;

                }

            }

        }

        // if not found and entry's state is not idle, we should create a new entry
        if(entry.getState() != MonitorConstant.BUSINESS_STATE_IDLE) {

            entries.add(entry);

        }

    }

    public List<T> getEntries() {
        return entries;
    }

    public void clear(int netunit){
        for(int i= 0; i< entries.size(); i++){
            if(netunit == entries.get(i).getNetunit()) {
                entries.remove(i);
                i--;
            }
        }
    }

    public void processPdu(PDU pdu){
        T entry = pduToEntry(pdu);
        setEntry(entry);
    }

    abstract protected T pduToEntry(PDU pdu);

    public Business getBrief(){
        return business;
    }
}
