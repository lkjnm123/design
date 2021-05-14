package com.graduation.ecommerce.design.entity;

import java.math.BigDecimal;

public class GoodView {
    //此类用于搜索商品展示页面以及购物车页面 展示商品详细信息
    private int goodid;
    private String goodimage;
    private BigDecimal goodprice;
    private String goodname;
    private int goodnumber;
    private int shopid;
    private String shopname;
    private int status; //1可添加   0已在购物车中不可添加

    public GoodView(int goodid, String goodimage, BigDecimal goodprice, String goodname, int goodnumber, int shopid, String shopname, int status) {
        this.goodid = goodid;
        this.goodimage = goodimage;
        this.goodprice = goodprice;
        this.goodname = goodname;
        this.goodnumber = goodnumber;
        this.shopid = shopid;
        this.shopname = shopname;
        this.status = status;
    }

    public int getGoodid() {
        return goodid;
    }

    public void setGoodid(int goodid) {
        this.goodid = goodid;
    }

    public String getGoodimage() {
        return goodimage;
    }

    public void setGoodimage(String goodimage) {
        this.goodimage = goodimage;
    }

    public BigDecimal getGoodprice() {
        return goodprice;
    }

    public void setGoodprice(BigDecimal goodprice) {
        this.goodprice = goodprice;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGoodnumber() {
        return goodnumber;
    }

    public void setGoodnumber(int goodnumber) {
        this.goodnumber = goodnumber;
    }

    @Override
    public String toString() {
        return "GoodView{" +
                "goodid=" + goodid +
                ", goodimage='" + goodimage + '\'' +
                ", goodprice=" + goodprice +
                ", goodname='" + goodname + '\'' +
                ", goodnumber=" + goodnumber +
                ", shopid=" + shopid +
                ", shopname='" + shopname + '\'' +
                ", status=" + status +
                '}';
    }
}
