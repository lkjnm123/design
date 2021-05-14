package com.graduation.ecommerce.design.entity;

public class OrderShow {
    //此类专用于查看订单详细信息中的商品条目
    private String ordernumber;
    private String createtime;
    private int goodid;
    private String goodname;
    private String goodimage;
    private int buynumber;
    private int goodstatus;
    private int shopid;
    private String shopname;
    private String consignee;

    public OrderShow(String ordernumber, String createtime, int goodid, String goodname, String goodimage, int buynumber, int goodstatus, int shopid, String shopname, String consignee) {
        this.ordernumber = ordernumber;
        this.createtime = createtime;
        this.goodid = goodid;
        this.goodname = goodname;
        this.goodimage = goodimage;
        this.buynumber = buynumber;
        this.goodstatus = goodstatus;
        this.shopid = shopid;
        this.shopname = shopname;
        this.consignee = consignee;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
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

    public int getGoodstatus() {
        return goodstatus;
    }

    public void setGoodstatus(int goodstatus) {
        this.goodstatus = goodstatus;
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

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    @Override
    public String toString() {
        return "OrderShow{" +
                "ordernumber='" + ordernumber + '\'' +
                ", createtime='" + createtime + '\'' +
                ", goodid=" + goodid +
                ", goodname='" + goodname + '\'' +
                ", goodimage='" + goodimage + '\'' +
                ", buynumber=" + buynumber +
                ", goodstatus=" + goodstatus +
                ", shopid=" + shopid +
                ", shopname='" + shopname + '\'' +
                ", consignee='" + consignee + '\'' +
                '}';
    }
}
