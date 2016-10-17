package com.jec.module.sysconfig.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jec.protocol.unit.BCD;
import com.jec.utils.DateTimeUtils;
import com.jec.utils.Response;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeremyliu on 02/10/2016.
 */
@Entity
@Table(name="zhwg_config_number_entry")
public class NumberEntry implements Serializable, NetUnitConfig{

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy="increment")
    private int id = 0;

    private int netunit;

    private int slot;

    private int ts;

    private String number = "";

    @Column(name="call_right")
    private int callRight;

    @Column(name="term_type")
    private int termType;

    @Column(name="user_type")
    private int userType;

    @Column(name="user_level")
    private int userLevel;

    @Column(name="record_right")
    private boolean recordRight;

    @Column(name="record_enable")
    private boolean recordEnable;

    @Column(name="hotline_right")
    private boolean hotlineRight;

    @Column(name="hotline_enable")
    private boolean hotlineEnable;

    @Column(name="hotline_number")
    private String hotlineNumber="";

    @Column(name="busing_cf_right")
    private boolean busingcfRight;

    @Column(name="busing_cf_enable")
    private boolean busingcfEnable;

    @Column(name="busing_cf_number")
    private String busingcfNumber="";

    @Column(name="follow_cf_right")
    private boolean followcfRight;

    @Column(name="follow_cf_enable")
    private boolean followcfEnable;

    @Column(name="follow_cf_number")
    private String followcfNumber="";

    @Column(name="nohinder_right")
    private boolean nohinderRight;

    @Column(name="nohinder_enable")
    private boolean nohinderEnable;

    @Column(name="phone_station_right")
    private boolean phonestationRight;

    @Column(name="phone_station_enable")
    private boolean phonestationEnable;

    @Column(name="reminder_right")
    private boolean reminderRight;

    @Column(name="reminder_enable")
    private boolean reminderEnable;

    @Column(name="reminder_time")
    private String reminderTime="";

    @Column(name="volatile_meet_right")
    private boolean volatileMeetRight;

    @Column(name="digit_relay_right")
    private boolean digitRelayRight;

    @Column(name="simulate_relay_right")
    private boolean simulateRelayRight;

    @Column(name="update_date")
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNetunit() {
        return netunit;
    }

    @Override
    public Response validate() {
        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(netunit<=0)
            return response.message("网元格式错误");
        if(slot<=0)
            return response.message("槽位格式错误");
        if(ts<0)
            return response.message("时隙格式错误");
        if(number.equals(""))
            return response.message("用户号码不能为空");
        if(BCD.fromString(number) == null)
            return response.message("用户号码格式错误");
        if(callRight<0 || callRight>6)
            return response.message("呼叫权限设置非法");
        if(termType<0 || termType>3)
            return response.message("终端类型设置非法");
        if(userType<0 || userType>1)
            return response.message("用户类型设置非法");
        if(userLevel<1 || userLevel>19)
            return response.message("用户级别设置非法");
        if(hotlineRight && hotlineEnable && hotlineNumber.equals(""))
            return response.message("热线号码不能为空");
        if(followcfRight && followcfEnable && followcfNumber.equals(""))
            return response.message("跟随转移号码不能为空");
        if(busingcfRight && busingcfEnable && busingcfNumber.equals(""))
            return response.message("遇忙转移号码不能为空");
        if(reminderRight && reminderEnable && reminderTime.equals(""))
            return response.message("提醒时间不能为空");

        if(!hotlineNumber.equals("") && BCD.fromString(hotlineNumber) == null)
            return response.message("热线号码格式错误");
        if(!busingcfNumber.equals("") && BCD.fromString(busingcfNumber) == null)
            return response.message("遇忙转移号码格式错误");
        if(!followcfNumber.equals("") && BCD.fromString(followcfNumber) == null)
            return response.message("跟随转移号码格式错误");
        if(!reminderTime.equals("") && !DateTimeUtils.isValidTime(reminderTime))
            return response.message("提醒时间格式错误");
        return response.status(Response.STATUS_SUCCESS);
    }

    @Override
    public void setUpdateDate() {
        this.updateDate = new Date();
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

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCallRight() {
        return callRight;
    }

    public void setCallRight(int callRight) {
        this.callRight = callRight;
    }

    public int getTermType() {
        return termType;
    }

    public void setTermType(int termType) {
        this.termType = termType;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public boolean isRecordRight() {
        return recordRight;
    }

    public void setRecordRight(boolean recordRight) {
        this.recordRight = recordRight;
    }

    public boolean isRecordEnable() {
        return recordEnable;
    }

    public void setRecordEnable(boolean recordEnable) {
        this.recordEnable = recordEnable;
    }

    public boolean isHotlineRight() {
        return hotlineRight;
    }

    public void setHotlineRight(boolean hotlineRight) {
        this.hotlineRight = hotlineRight;
    }

    public boolean isHotlineEnable() {
        return hotlineEnable;
    }

    public void setHotlineEnable(boolean hotlineEnable) {
        this.hotlineEnable = hotlineEnable;
    }

    public String getHotlineNumber() {
        return hotlineNumber;
    }

    public void setHotlineNumber(String hotlineNumber) {
        this.hotlineNumber = hotlineNumber;
    }

    public boolean isBusingcfRight() {
        return busingcfRight;
    }

    public void setBusingcfRight(boolean busingcfRight) {
        this.busingcfRight = busingcfRight;
    }

    public boolean isBusingcfEnable() {
        return busingcfEnable;
    }

    public void setBusingcfEnable(boolean busingcfEnable) {
        this.busingcfEnable = busingcfEnable;
    }

    public String getBusingcfNumber() {
        return busingcfNumber;
    }

    public void setBusingcfNumber(String busingcfNumber) {
        this.busingcfNumber = busingcfNumber;
    }

    public boolean isFollowcfRight() {
        return followcfRight;
    }

    public void setFollowcfRight(boolean followcfRight) {
        this.followcfRight = followcfRight;
    }

    public boolean isFollowcfEnable() {
        return followcfEnable;
    }

    public void setFollowcfEnable(boolean followcfEnable) {
        this.followcfEnable = followcfEnable;
    }

    public String getFollowcfNumber() {
        return followcfNumber;
    }

    public void setFollowcfNumber(String followcfNumber) {
        this.followcfNumber = followcfNumber;
    }

    public boolean isNohinderEnable() {
        return nohinderEnable;
    }

    public void setNohinderEnable(boolean nohinderEnable) {
        this.nohinderEnable = nohinderEnable;
    }

    public boolean isNohinderRight() {
        return nohinderRight;
    }

    public void setNohinderRight(boolean nohinderRight) {
        this.nohinderRight = nohinderRight;
    }

    public boolean isPhonestationRight() {
        return phonestationRight;
    }

    public void setPhonestationRight(boolean phonestationRight) {
        this.phonestationRight = phonestationRight;
    }

    public boolean isPhonestationEnable() {
        return phonestationEnable;
    }

    public void setPhonestationEnable(boolean phonestationEnable) {
        this.phonestationEnable = phonestationEnable;
    }

    public boolean isReminderRight() {
        return reminderRight;
    }

    public void setReminderRight(boolean reminderRight) {
        this.reminderRight = reminderRight;
    }

    public boolean isReminderEnable() {
        return reminderEnable;
    }

    public void setReminderEnable(boolean reminderEnable) {
        this.reminderEnable = reminderEnable;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean isVolatileMeetRight() {
        return volatileMeetRight;
    }

    public void setVolatileMeetRight(boolean volatileMeetRight) {
        this.volatileMeetRight = volatileMeetRight;
    }

    public boolean isDigitRelayRight() {
        return digitRelayRight;
    }

    public void setDigitRelayRight(boolean digitRelayRight) {
        this.digitRelayRight = digitRelayRight;
    }

    public boolean isSimulateRelayRight() {
        return simulateRelayRight;
    }

    public void setSimulateRelayRight(boolean simulateRelayRight) {
        this.simulateRelayRight = simulateRelayRight;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}

