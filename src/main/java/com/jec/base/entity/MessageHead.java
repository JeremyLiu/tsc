package com.jec.base.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jeremyliu on 5/23/16.
 */
public class MessageHead {

    public static final String MSG_TYPE_HB = "1";
    public static final String MSG_TYPE_TPQ = "2";
    public static final String MSG_TYPE_TPT = "3";
    public static final String MSG_TYPE_ERROR = "4";
    public static final String MSG_TYPE_HSL = "5";
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
    private static Integer count = 1;

    @XStreamAsAttribute
    @XStreamAlias("ProtocolType")
    private String protocolType="0x01";
    @XStreamAsAttribute
    @XStreamAlias("MessageType")
    private String messageType="";
    @XStreamAsAttribute
    @XStreamAlias("MsgID")
    private String msgId = String.valueOf(count);
    @XStreamAsAttribute
    @XStreamAlias("OperateType")
    private String operateType="0xFF";
    @XStreamAsAttribute
    @XStreamAlias("SendType")
    private String sendType="0x00";
    @XStreamAsAttribute
    @XStreamAlias("Priority")
    private String priority="0x00";
    @XStreamAsAttribute
    @XStreamAlias("DayTime")
    private String dayTime = formatter.format(new Date());

    public MessageHead(){
        synchronized (count) {
            count++;
        }
    }

    public MessageHead(String messageType){
        this();
        this.messageType = messageType;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }
}
