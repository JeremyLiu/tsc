package com.jec.module.sysconfig.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jec.protocol.unit.BCD;
import com.jec.utils.Response;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeremyliu on 08/10/2016.
 */
@Entity
@Table(name="zhwg_config_phonestation")
public class PhoneStation implements NetUnitConfig,Serializable {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    private int id;

    public final static String splitChar = "\\n|\\r\\n| |\\t";

    @Column(name="element_id")
    private int netunit;

    private String name="";

    private String code="";

    private String members = "";

    @Column(name="update_date")
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String getSplitChar() {
        return splitChar;
    }

    @Override
    public int getNetunit() {
        return netunit;
    }

    @Override
    public Response validate() {
        Response resp = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(name.equals(""))
            return resp.message("业务名不能为空");
        if(name.length()>20)
            return resp.message("业务名长度超过20个字符");
        if(code.equals(""))
            return resp.message("业务代码不能为空");
        if(BCD.fromString(code) == null)
            return resp.message("业务代码格式错误");

        String[] memberList = getMembers();
        if(!bcdListValidate(memberList))
            return resp.message("成员格式错误");
        if(memberList.length>40)
            return resp.message("成员个数不能超过40个");
        return resp.status(Response.STATUS_SUCCESS);
    }

    @Override
    public void setUpdateDate() {
        updateDate = new Date();
    }

    public void setNetunit(int netunit) {
        this.netunit = netunit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getMembers() {
        return members.split(splitChar);
    }

    public void setMembers(String members) {
        this.members = members;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public static boolean bcdListValidate(String[] values){
        try {
            for (String value: values)
                Integer.parseInt(value);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
}
