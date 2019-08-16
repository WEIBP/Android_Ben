package com.ben.common.net.hikvision;

public class HikBean {


    /**
     * pageNo : 1
     * pageSize : 20
     * treeCode : 0
     */

    private int pageNo;
    private int pageSize;
    private String treeCode;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getTreeCode() {
        return treeCode;
    }

    public void setTreeCode(String treeCode) {
        this.treeCode = treeCode;
    }
}
