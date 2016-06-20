package com.jec.module.sysmonitor.vo;

/**
 * Created by jeremyliu on 5/18/16.
 */
public class State{

    private int id;
    private int slot;
    private int port;
    private int state;
    private int type;

    public final static int NETUNIT = 0;
    public final static int CARD = 1;
    public final static int PORT = 2;
    private final static String pathDelem = ",";

    public State(int id,int state){
        this.id = id;
        this.type = NETUNIT;
        this.state = state;
    }

    public State(int id, int slot, int state){
        this.id = id;
        this.slot = slot;
        this.type = CARD;
        this.state = state;
    }

    public State(int id, int slot, int port,int state){
        this.id = id;
        this.slot = slot;
        this.port = port;
        this.type = PORT;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath(){
        switch (type){
            case NETUNIT: return String.valueOf(id);
            case CARD: return id + pathDelem + slot;
            case PORT: return id + pathDelem + slot + pathDelem + port;
            default: return null;
        }
    }

    public static String buildPath(int id){
        return String.valueOf(id);
    }

    public static String buildPath(int id, int slot){
        return id + pathDelem + slot;
    }

    public static String buildPath(int id, int slot, int port){
        return id + pathDelem + slot + pathDelem + port;
    }
}