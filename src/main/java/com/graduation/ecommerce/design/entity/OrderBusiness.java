package com.graduation.ecommerce.design.entity;

public class OrderBusiness {
    private int order_business_id; //自增主键
    private int order_item_id; //外键
    private int shop_id; //外键
    private int customer_id; //外键
    private int good_id; //外键
    private int good_number;
    private String address_context;
    private String order_number; //外键
    private int good_status;
    private String express_number;

    public OrderBusiness(int order_business_id, int order_item_id, int shop_id, int customer_id, int good_id, int good_number, String address_context, String order_number, int good_status, String express_number) {
        this.order_business_id = order_business_id;
        this.order_item_id = order_item_id;
        this.shop_id = shop_id;
        this.customer_id = customer_id;
        this.good_id = good_id;
        this.good_number = good_number;
        this.address_context = address_context;
        this.order_number = order_number;
        this.good_status = good_status;
        this.express_number = express_number;
    }

    public int getOrder_business_id() {
        return order_business_id;
    }

    public void setOrder_business_id(int order_business_id) {
        this.order_business_id = order_business_id;
    }

    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }

    public int getGood_number() {
        return good_number;
    }

    public void setGood_number(int good_number) {
        this.good_number = good_number;
    }

    public String getAddress_context() {
        return address_context;
    }

    public void setAddress_context(String address_context) {
        this.address_context = address_context;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public int getGood_status() {
        return good_status;
    }

    public void setGood_status(int good_status) {
        this.good_status = good_status;
    }

    public String getExpress_number() {
        return express_number;
    }

    public void setExpress_number(String express_number) {
        this.express_number = express_number;
    }

    @Override
    public String toString() {
        return "OrderBusiness{" +
                "order_business_id=" + order_business_id +
                ", order_item_id=" + order_item_id +
                ", shop_id=" + shop_id +
                ", customer_id=" + customer_id +
                ", good_id=" + good_id +
                ", good_number=" + good_number +
                ", address_context='" + address_context + '\'' +
                ", order_number='" + order_number + '\'' +
                ", good_status=" + good_status +
                ", express_number='" + express_number + '\'' +
                '}';
    }
}
