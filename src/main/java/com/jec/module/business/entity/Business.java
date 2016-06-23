package com.jec.module.business.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeremyliu on 6/18/16.
 */
public class Business implements Serializable {

    private String name;

    private String img;

    private int type;

    private int count;

    public Business(){

    }

    public Business(String name, String img, int type){
        this.name = name;
        this.img = img;
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
