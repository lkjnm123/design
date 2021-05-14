package com.graduation.ecommerce.design.entity;

public class GoodInfoCommentShow {
    private String username;
    private String comment;

    public GoodInfoCommentShow(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "GoodInfoCommentShow{" +
                "username='" + username + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
