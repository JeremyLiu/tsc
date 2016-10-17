package com.jec.module.sysconfig.service;

import com.jec.utils.Response;

/**
 * Created by jeremyliu on 09/10/2016.
 */
public abstract class DeviceDownloadService extends DownloadService{

   public abstract Response download(String number);
}
