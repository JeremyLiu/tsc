package com.jec.base.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jeremyliu on 7/24/16.
 */
public class PageList<T> implements Serializable {

    private int totalPage;

    private int page;

    private List<T> data;

    private long totalCount;

    public PageList(List<T> data, long totalCount, int totalPage, int curPage){
        this.data =  data;
        this.totalPage = totalPage;
        this.page = curPage;
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}