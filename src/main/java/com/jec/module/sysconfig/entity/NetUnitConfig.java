package com.jec.module.sysconfig.entity;

import com.jec.utils.Response;

/**
 * Created by jeremyliu on 8/6/16.
 */
public interface NetUnitConfig {
    public int getNetunit();

    public Response validate();

    public void setUpdateDate();
}
