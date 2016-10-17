package com.jec.module.sysmonitor.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by jeremyliu on 5/24/16.
 */

@XStreamAlias("NmsHeartbeat")
public class NmsHeartbeat {
    @XStreamAlias("NmsStatus")
    private NmsStatus nmsStatus = new NmsStatus();

    public class NmsStatus {

        int status = 0;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}

