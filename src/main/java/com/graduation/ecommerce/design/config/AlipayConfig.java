package com.graduation.ecommerce.design.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig implements ApplicationRunner {
    @Value("${alipay.appId}")
    private String appId;
    @Value("${alipay.privateKey}")
    private String privateKey;
    @Value("${alipay.publicKey}")
    private String publicKey;
    @Value("${alipay.gateway}")
    private String gateway;
    @Value("${alipay.notifyUrl}")
    private String notifyUrl;

    private Config getOptions(){
        Config config = new Config();
        config.protocol="https";
        config.gatewayHost=this.gateway;
        config.signType="RSA2";
        config.appId=this.appId;
        config.merchantPrivateKey=this.privateKey;
        config.alipayPublicKey=this.publicKey;
        config.notifyUrl=this.notifyUrl;
        return config;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Factory.setOptions(getOptions());
        System.out.println("====支付宝SDK初始化完成====");
    }
}
