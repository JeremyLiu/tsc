package com.jec.module.sysconfig.service.convertor;

import com.jec.utils.Response;

/**
 * Created by jeremyliu on 9/22/16.
 */
public interface Convertor<T>{
    Response process(T t);

    Response checkBeforeRemove(int id);
}
