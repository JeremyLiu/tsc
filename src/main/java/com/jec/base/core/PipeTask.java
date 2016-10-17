package com.jec.base.core;

/**
 * Created by jeremyliu on 6/19/16.
 */
abstract public class PipeTask<T> {

    abstract public boolean prepare();

    abstract public boolean work(T t);

    abstract public boolean done();
}
