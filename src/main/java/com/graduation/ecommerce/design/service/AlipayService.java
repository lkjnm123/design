package com.graduation.ecommerce.design.service;

public interface AlipayService {
    public String page(String subject,String ordernumber,String total);
    public String refund(String outTradeNo, String refundAmount);
}
