package com.graduation.ecommerce.design.service;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AlipayServiceImpl implements AlipayService{
    @Value("${alipay.returnUrl}")
    private String reutrnUrl;
    @Value("${alipay.errorUrl")
    private String errorUrl;

    @Override
    public String page(String subject,String ordernumber, String total) {
        try{
            AlipayTradePagePayResponse response = Factory.Payment.Page().pay(subject,ordernumber,total,reutrnUrl);
            return response.body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String refund(String outTradeNo, String refundAmount) {
        AlipayTradeRefundResponse response = null;
        try {
            response = Factory.Payment
                    .Common()
                    // 调用交易退款(商家订单号, 退款金额)
                    .refund(outTradeNo, refundAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response.getMsg().equals("Success")) {return "退款成功";}
        return "退款失败";
    }
}
