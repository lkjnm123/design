package com.graduation.ecommerce.design.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class InitialOrder {
    private int order_id;
    private String order_number;
    private int customer_id;
    private int trade_status;
    private int pay_status;
    private BigDecimal order_amount;
    private BigDecimal pay_amount;
    private Date create_time;
    private String address_context;

    public InitialOrder(int order_id, String order_number, int customer_id, int trade_status, int pay_status, BigDecimal order_amount, BigDecimal pay_amount, Date create_time, String address_context) {
        this.order_id = order_id;
        this.order_number = order_number;
        this.customer_id = customer_id;
        this.trade_status = trade_status;
        this.pay_status = pay_status;
        this.order_amount = order_amount;
        this.pay_amount = pay_amount;
        this.create_time = create_time;
        this.address_context = address_context;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(int trade_status) {
        this.trade_status = trade_status;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public BigDecimal getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(BigDecimal order_amount) {
        this.order_amount = order_amount;
    }

    public BigDecimal getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(BigDecimal pay_amount) {
        this.pay_amount = pay_amount;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getAddress_context() {
        return address_context;
    }

    public void setAddress_context(String address_context) {
        this.address_context = address_context;
    }

    @Override
    public String toString() {
        return "InitialOrder{" +
                "order_id=" + order_id +
                ", order_number='" + order_number + '\'' +
                ", customer_id=" + customer_id +
                ", trade_status=" + trade_status +
                ", pay_status=" + pay_status +
                ", order_amount=" + order_amount +
                ", pay_amount=" + pay_amount +
                ", create_time=" + create_time +
                ", address_context='" + address_context + '\'' +
                '}';
    }
}
