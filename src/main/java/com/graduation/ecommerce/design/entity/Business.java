package com.graduation.ecommerce.design.entity;

public class Business {
    private int id;
    private String username;
    private String pwdsalt;
    private String pwdhash;
    private String phonenumber;
    private String mailaddress;
    private int status;
    public Business(int id, String username, String pwdsalt, String pwdhash,String phonenumber,String mailaddress) {
        this.id = id;
        this.username = username;
        this.pwdsalt = pwdsalt;
        this.pwdhash = pwdhash;
        this.phonenumber = phonenumber;
        this.mailaddress = mailaddress;
        this.status = 1;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPwdsalt() {
        return pwdsalt;
    }

    public String getPwdhash() {
        return pwdhash;
    }

    public int getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPwdsalt(String pwdsalt) {
        this.pwdsalt = pwdsalt;
    }

    public void setPwdhash(String pwdhash) {
        this.pwdhash = pwdhash;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getMailaddress() {
        return mailaddress;
    }

    public void setMailaddress(String mailaddress) {
        this.mailaddress = mailaddress;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Override
    public String toString() {
        return "Business{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", pwdsalt='" + pwdsalt + '\'' +
                ", pwdhash='" + pwdhash + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", mailaddress='" + mailaddress + '\'' +
                ", status=" + status +
                '}';
    }
}
