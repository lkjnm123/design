package com.graduation.ecommerce.design.entity;

import java.sql.Date;

public class CartItem {
    private int cartitemid;
    private int customerid;
    private int goodid;
    private int number;
    private int customer_status;
    private int shop_status;
    private int admin_status;
    private int business_status;
    private Date updatetime;

    public CartItem(int cartitemid, int customerid, int goodid, int number, int customer_status, int shop_status, int admin_status, int business_status, Date updatetime) {
        this.cartitemid = cartitemid;
        this.customerid = customerid;
        this.goodid = goodid;
        this.number = number;
        this.customer_status = customer_status;
        this.shop_status = shop_status;
        this.admin_status = admin_status;
        this.business_status = business_status;
        this.updatetime = updatetime;
    }

    public int getCartitemid() {
        return cartitemid;
    }

    public void setCartitemid(int cartitemid) {
        this.cartitemid = cartitemid;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public int getGoodid() {
        return goodid;
    }

    public void setGoodid(int goodid) {
        this.goodid = goodid;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(int customer_status) {
        this.customer_status = customer_status;
    }

    public int getShop_status() {
        return shop_status;
    }

    public void setShop_status(int shop_status) {
        this.shop_status = shop_status;
    }

    public int getAdmin_status() {
        return admin_status;
    }

    public void setAdmin_status(int admin_status) {
        this.admin_status = admin_status;
    }

    public int getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(int business_status) {
        this.business_status = business_status;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartitemid=" + cartitemid +
                ", customerid=" + customerid +
                ", goodid=" + goodid +
                ", number=" + number +
                ", customer_status=" + customer_status +
                ", shop_status=" + shop_status +
                ", admin_status=" + admin_status +
                ", business_status=" + business_status +
                ", updatetime=" + updatetime +
                '}';
    }
}
