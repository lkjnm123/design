package com.graduation.ecommerce.design.entity;

public class CustomerAddress {
    private int addressid;
    private int customerid;
    private String realname;
    private String phonenumber;
    private String address;

    public CustomerAddress(int addressid, int customerid, String realname, String phonenumber, String address) {
        this.addressid = addressid;
        this.customerid = customerid;
        this.realname = realname;
        this.phonenumber = phonenumber;
        this.address = address;
    }

    public int getAddressid() {
        return addressid;
    }

    public void setAddressid(int addressid) {
        this.addressid = addressid;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CustomerAddress{" +
                "addressid=" + addressid +
                ", customerid=" + customerid +
                ", realname='" + realname + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
