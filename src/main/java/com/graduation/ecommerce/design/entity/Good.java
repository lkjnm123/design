package com.graduation.ecommerce.design.entity;

import java.math.BigDecimal;

public class Good {
    private int id;
    private int goodid;
    //goodid auto_increment
    private String goodname;
    private String brand;
    private String introduce;
    private BigDecimal price;
    private int goodnumber;
    private String weight;
    private String address;
    private String image;
    private int status;
    private int admin_status;
    private int state;

    public Good(int id, int goodid, String goodname, String brand, String introduce, BigDecimal price, int goodnumber, String weight, String address, String image, int status, int admin_status, int state) {
        this.id = id;
        this.goodid = goodid;
        this.goodname = goodname;
        this.brand = brand;
        this.introduce = introduce;
        this.price = price;
        this.goodnumber = goodnumber;
        this.weight = weight;
        this.address = address;
        this.image = image;
        this.status = status;
        this.admin_status = admin_status;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getGoodnumber() {
        return goodnumber;
    }

    public void setGoodnumber(int goodnumber) {
        this.goodnumber = goodnumber;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAdmin_status() {
        return admin_status;
    }

    public void setAdmin_status(int admin_status) {
        this.admin_status = admin_status;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Good{" +
                "id=" + id +
                ", goodid=" + goodid +
                ", goodname='" + goodname + '\'' +
                ", brand='" + brand + '\'' +
                ", introduce='" + introduce + '\'' +
                ", price=" + price +
                ", goodnumber=" + goodnumber +
                ", weight='" + weight + '\'' +
                ", address='" + address + '\'' +
                ", image='" + image + '\'' +
                ", status=" + status +
                ", admin_status=" + admin_status +
                ", state=" + state +
                '}';
    }
}
