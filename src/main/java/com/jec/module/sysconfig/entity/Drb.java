package com.jec.module.sysconfig.entity;

import java.io.Serializable;

/**
 * Created by jeremyliu on 7/2/16.
 */
public class Drb implements Serializable{

    private int netunit;

    private int slot;

    private int port;

    private int workMode;

    private int interfaceMode;

    private int distanceMode;

    private int clockMode;

    private int opcA;
    private int opcB;
    private int opcC;

    private int dpcA;
    private int dpcB;
    private int dpcC;

    private int cardType;
/*
	@Attribute public int cicIdx;
	@Attribute public int cicTs;
*/

    public int getOPC() {
        return (opcA << 16) + (opcB << 8) + opcC;
    }

    public int getDPC() {
        return (dpcA << 16) + (dpcB << 8) + dpcC;
    }

    public int getCIC() {
        //return (cicIdx << 5) + cicTs;
        return (port << 5) + 0;
    }

    public int getNetunit() {
        return netunit;
    }

    public void setNetunit(int netunit) {
        this.netunit = netunit;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getDpcC() {
        return dpcC;
    }

    public void setDpcC(int dpcC) {
        this.dpcC = dpcC;
    }

    public int getDpcB() {
        return dpcB;
    }

    public void setDpcB(int dpcB) {
        this.dpcB = dpcB;
    }

    public int getDpcA() {
        return dpcA;
    }

    public void setDpcA(int dpcA) {
        this.dpcA = dpcA;
    }

    public int getOpcC() {
        return opcC;
    }

    public void setOpcC(int opcC) {
        this.opcC = opcC;
    }

    public int getOpcA() {
        return opcA;
    }

    public void setOpcA(int opcA) {
        this.opcA = opcA;
    }

    public int getOpcB() {
        return opcB;
    }

    public void setOpcB(int opcB) {
        this.opcB = opcB;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWorkMode() {
        return workMode;
    }

    public void setWorkMode(int workMode) {
        this.workMode = workMode;
    }

    public int getInterfaceMode() {
        return interfaceMode;
    }

    public void setInterfaceMode(int interfaceMode) {
        this.interfaceMode = interfaceMode;
    }

    public int getDistanceMode() {
        return distanceMode;
    }

    public void setDistanceMode(int distanceMode) {
        this.distanceMode = distanceMode;
    }

    public int getClockMode() {
        return clockMode;
    }

    public void setClockMode(int clockMode) {
        this.clockMode = clockMode;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
}
