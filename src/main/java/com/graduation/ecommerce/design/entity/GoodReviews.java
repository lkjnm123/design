package com.graduation.ecommerce.design.entity;

public class GoodReviews {
    private int reviews_id;
    private int ordercustomer_id;
    private int customer_id;
    private String comment;
    private int goodid;

    public GoodReviews(int reviews_id, int ordercustomer_id, int customer_id, String comment, int goodid) {
        this.reviews_id = reviews_id;
        this.ordercustomer_id = ordercustomer_id;
        this.customer_id = customer_id;
        this.comment = comment;
        this.goodid = goodid;
    }

    public int getReviews_id() {
        return reviews_id;
    }

    public void setReviews_id(int reviews_id) {
        this.reviews_id = reviews_id;
    }

    public int getOrdercustomer_id() {
        return ordercustomer_id;
    }

    public void setOrdercustomer_id(int ordercustomer_id) {
        this.ordercustomer_id = ordercustomer_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGoodid() {
        return goodid;
    }

    public void setGoodid(int goodid) {
        this.goodid = goodid;
    }

    @Override
    public String toString() {
        return "GoodReviews{" +
                "reviews_id=" + reviews_id +
                ", ordercustomer_id=" + ordercustomer_id +
                ", customer_id=" + customer_id +
                ", comment='" + comment + '\'' +
                ", goodid=" + goodid +
                '}';
    }
}
