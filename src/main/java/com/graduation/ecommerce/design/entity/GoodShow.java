package com.graduation.ecommerce.design.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class GoodShow {
    //此类专用于前台页面展示  待收货商品展示
    private Date date;
    private String ordernumber;
    private int orderitemid;
    private int ordercustomerid;
    private int goodid;
    private String goodname;
    private String goodimage;
    private int buynumber;
    private String consignee;
    private BigDecimal paidprice;
    private int shopid;
    private String shopname;
    private int goodstatus;

    public GoodShow(Date date, String ordernumber, int orderitemid, int ordercustomerid, int goodid, String goodname, String goodimage, int buynumber, String consignee, BigDecimal paidprice, int shopid, String shopname, int goodstatus) {
        this.date = date;
        this.ordernumber = ordernumber;
        this.orderitemid = orderitemid;
        this.ordercustomerid = ordercustomerid;
        this.goodid = goodid;
        this.goodname = goodname;
        this.goodimage = goodimage;
        this.buynumber = buynumber;
        this.consignee = consignee;
        this.paidprice = paidprice;
        this.shopid = shopid;
        this.shopname = shopname;
        this.goodstatus = goodstatus;
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

    public int getOrderitemid() {
        return orderitemid;
    }

    public void setOrderitemid(int orderitemid) {
        this.orderitemid = orderitemid;
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

    public String getGoodimage() {
        return goodimage;
    }

    public void setGoodimage(String goodimage) {
        this.goodimage = goodimage;
    }

    public int getBuynumber() {
        return buynumber;
    }

    public void setBuynumber(int buynumber) {
        this.buynumber = buynumber;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public BigDecimal getPaidprice() {
        return paidprice;
    }

    public void setPaidprice(BigDecimal paidprice) {
        this.paidprice = paidprice;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public int getGoodstatus() {
        return goodstatus;
    }

    public void setGoodstatus(int goodstatus) {
        this.goodstatus = goodstatus;
    }

    public int getOrdercustomerid() {
        return ordercustomerid;
    }

    public void setOrdercustomerid(int ordercustomerid) {
        this.ordercustomerid = ordercustomerid;
    }

    @Override
    public String toString() {
        return "GoodShow{" +
                "date=" + date +
                ", ordernumber='" + ordernumber + '\'' +
                ", orderitemid=" + orderitemid +
                ", ordercustomerid=" + ordercustomerid +
                ", goodid=" + goodid +
                ", goodname='" + goodname + '\'' +
                ", goodimage='" + goodimage + '\'' +
                ", buynumber=" + buynumber +
                ", consignee='" + consignee + '\'' +
                ", paidprice=" + paidprice +
                ", shopid=" + shopid +
                ", shopname='" + shopname + '\'' +
                ", goodstatus=" + goodstatus +
                '}';
    }
}
