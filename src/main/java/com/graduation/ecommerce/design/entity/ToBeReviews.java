package com.graduation.ecommerce.design.entity;

import java.util.Date;

public class ToBeReviews {
    //该类仅用于 向顾客展示待评价的商品
    private Date date;
    private String ordernumber;
    private String goodname;
    private String shopname;
    private int buynumber;
    private int ordercustomerid;

    public ToBeReviews(Date date, String ordernumber, String goodname, String shopname, int buynumber, int ordercustomerid) {
        this.date = date;
        this.ordernumber = ordernumber;
        this.goodname = goodname;
        this.shopname = shopname;
        this.buynumber = buynumber;
        this.ordercustomerid = ordercustomerid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public int getBuynumber() {
        return buynumber;
    }

    public void setBuynumber(int buynumber) {
        this.buynumber = buynumber;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public int getOrdercustomerid() {
        return ordercustomerid;
    }

    public void setOrdercustomerid(int ordercustomerid) {
        this.ordercustomerid = ordercustomerid;
    }

    @Override
    public String toString() {
        return "ToBeReviews{" +
                "date=" + date +
                ", ordernumber='" + ordernumber + '\'' +
                ", goodname='" + goodname + '\'' +
                ", shopname='" + shopname + '\'' +
                ", buynumber=" + buynumber +
                ", ordercustomerid=" + ordercustomerid +
                '}';
    }
}
