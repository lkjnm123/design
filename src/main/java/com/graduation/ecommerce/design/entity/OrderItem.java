package com.graduation.ecommerce.design.entity;

public class OrderItem {
    private int order_item_id;
    private int order_id;
    private int goodid;
    private int goodnumber;
    private int item_status;

    public OrderItem(int order_item_id, int order_id, int goodid, int goodnumber, int item_status) {
        this.order_item_id = order_item_id;
        this.order_id = order_id;
        this.goodid = goodid;
        this.goodnumber = goodnumber;
        this.item_status = item_status;
    }

    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getGoodid() {
        return goodid;
    }

    public void setGoodid(int goodid) {
        this.goodid = goodid;
    }

    public int getGoodnumber() {
        return goodnumber;
    }

    public void setGoodnumber(int goodnumber) {
        this.goodnumber = goodnumber;
    }

    public int getItem_status() {
        return item_status;
    }

    public void setItem_status(int item_status) {
        this.item_status = item_status;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "order_item_id=" + order_item_id +
                ", order_id=" + order_id +
                ", goodid=" + goodid +
                ", goodnumber=" + goodnumber +
                ", item_status=" + item_status +
                '}';
    }
}
