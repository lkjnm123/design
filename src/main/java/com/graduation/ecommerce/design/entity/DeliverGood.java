package com.graduation.ecommerce.design.entity;

import java.util.Date;

public class DeliverGood {
    //此类专用于 前台待发货商品展示
    private int order_item_id; //方便修改 确保唯一
    private String createdate;
    private String ordernumber;
    private int goodid;
    private String goodname;
    private int goodnumber;
    private String buyer;
    private String address;
    private int goodstatus;

    public DeliverGood(int order_item_id, String createdate, String ordernumber, int goodid, String goodname, int goodnumber, String buyer, String address, int goodstatus) {
        this.order_item_id = order_item_id;
        this.createdate = createdate;
        this.ordernumber = ordernumber;
        this.goodid = goodid;
        this.goodname = goodname;
        this.goodnumber = goodnumber;
        this.buyer = buyer;
        this.address = address;
        this.goodstatus = goodstatus;
    }

    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public int getGoodid() {
        return goodid;
    }

    public void setGoodid(int goodid) {
        this.goodid = goodid;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public int getGoodnumber() {
        return goodnumber;
    }

    public void setGoodnumber(int goodnumber) {
        this.goodnumber = goodnumber;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getGoodstatus() {
        return goodstatus;
    }

    public void setGoodstatus(int goodstatus) {
        this.goodstatus = goodstatus;
    }

    @Override
    public String toString() {
        return "DeliverGood{" +
                "order_item_id=" + order_item_id +
                ", createdate='" + createdate + '\'' +
                ", ordernumber='" + ordernumber + '\'' +
                ", goodid=" + goodid +
                ", goodname='" + goodname + '\'' +
                ", goodnumber=" + goodnumber +
                ", buyer='" + buyer + '\'' +
                ", address='" + address + '\'' +
                ", goodstatus=" + goodstatus +
                '}';
    }
}
