package com.graduation.ecommerce.design.entity;

public class Shop {
    private int id;
    private String shopname;
    private String introduce;
    private int status;
    private int admin_status;

    public Shop(int id, String shopname, String introduce, int status, int admin_status) {
        this.id = id;
        this.shopname = shopname;
        this.introduce = introduce;
        this.status = status;
        this.admin_status = admin_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", shopname='" + shopname + '\'' +
                ", introduce='" + introduce + '\'' +
                ", status=" + status +
                ", admin_status=" + admin_status +
                '}';
    }
}
