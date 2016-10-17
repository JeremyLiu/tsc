package com.jec.module.sysmonitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jec.base.entity.NetState;

import java.io.Serializable;

/**
 * Created by jeremyliu on 5/10/16.
 */
public class NetUnitState extends NetState implements Serializable {

    private int id;

    private CardState[] cardStates;

    public NetUnitState(){

    }

    public NetUnitState(int id){
        this.id = id;
    }

    @JsonIgnore
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CardState[] getCardStates() {
        return cardStates;
    }

    public void setCardStates(CardState[] cardStates) {
        this.cardStates = cardStates;
    }
}
