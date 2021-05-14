package com.graduation.ecommerce.design.config;

import com.graduation.ecommerce.design.component.LoginHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/index").setViewName("/index");
        registry.addViewController("/index.html").setViewName("/index");
        registry.addViewController("/search").setViewName("/search");
        registry.addViewController("/search.html").setViewName("/search");

        registry.addViewController("/client/profile.html").setViewName("/client/profile");
        registry.addViewController("/client/profile").setViewName("/client/profile");
        registry.addViewController("/client/alert/alert_address.html").setViewName("/client/alert/alert_address");
        registry.addViewController("/client/alert/alert_address").setViewName("/client/alert/alert_address");
        registry.addViewController("/client/alert/alert_alladdress.html").setViewName("/client/alert/alert_alladdress");
        registry.addViewController("/client/alert/alert_alladdress").setViewName("/client/alert/alert_alladdress");
        registry.addViewController("/client/alert/alert_cart").setViewName("/client/alert/alert_cart");
        registry.addViewController("/client/alert/alert_cart.html").setViewName("/client/alert/alert_cart");
        registry.addViewController("/client/alert/alert_goodinfo").setViewName("/client/alert/alert_goodinfo");
        registry.addViewController("/client/alert/alert_goodinfo.html").setViewName("/client/alert/alert_goodinfo");
        registry.addViewController("/client/alert/alert_settlement").setViewName("/client/alert/alert_settlement");
        registry.addViewController("/client/alert/alert_settlement.html").setViewName("/client/alert/alert_settlement");
        registry.addViewController("/client/alert/alert_tobereceived").setViewName("/client/alert/alert_tobereceived");
        registry.addViewController("/client/alert/alert_tobereceived.html").setViewName("/client/alert/alert_tobereceived");
        registry.addViewController("/client/alert/alert_notpayorder").setViewName("/client/alert/alert_notpayorder");
        registry.addViewController("/client/alert/alert_notpayorder.html").setViewName("/client/alert/alert_notpayorder");
        registry.addViewController("/client/alert/alert_orderinfo").setViewName("/client/alert/alert_orderinfo");
        registry.addViewController("/client/alert/alert_orderinfo.html").setViewName("/client/alert/alert_orderinfo");
        registry.addViewController("/client/alert/alert_orderhistory").setViewName("/client/alert/alert_orderhistory");
        registry.addViewController("/client/alert/alert_orderhistory.html").setViewName("/client/alert/alert_orderhistory");
        registry.addViewController("/client/alert/alert_tobereview.html").setViewName("/client/alert/alert_tobereview");
        registry.addViewController("/client/alert/alert_tobereview").setViewName("/client/alert/alert_tobereview");
        //AliPay
        registry.addViewController("/client/return").setViewName("/client/return");
        registry.addViewController("/client/return.html").setViewName("/client/return");
        registry.addViewController("/client/alipay").setViewName("/client/alipay");
        registry.addViewController("/client/alipay.html").setViewName("/client/alipay");

        registry.addViewController("/shop/business").setViewName("/shop/business");
        registry.addViewController("/shop/business.html").setViewName("/shop/business");
        registry.addViewController("/shop/alert/alert_shop").setViewName("/shop/alert/alert_shop");
        registry.addViewController("/shop/alert/alert_shop.html").setViewName("/shop/alert/alert_shop");
        registry.addViewController("/shop/alert/alert_allshop.html").setViewName("/shop/alert/alert_allshop");
        registry.addViewController("/shop/alert/alert_allshop").setViewName("/shop/alert/alert_allshop");
        registry.addViewController("/shop/alert/alert_good").setViewName("/shop/alert/alert_good");
        registry.addViewController("/shop/alert/alert_good.html").setViewName("/shop/alert/alert_good");
        registry.addViewController("/shop/alert/alert_allgood.html").setViewName("/shop/alert/alert_allgood");
        registry.addViewController("/shop/alert/alert_allgood").setViewName("/shop/alert/alert_allgood");
        registry.addViewController("/shop/alert/alert_delivergood").setViewName("/shop/alert/alert_delivergood");
        registry.addViewController("/shop/alert/alert_delivergood.html").setViewName("/shop/alert/alert_delivergood");

        registry.addViewController("/about").setViewName("/about");
        registry.addViewController("about.html").setViewName("/about");
        registry.addViewController("/register").setViewName("/register");
        registry.addViewController("/register.html").setViewName("/register");
        registry.addViewController("/signin").setViewName("/signin");
        registry.addViewController("/signin.html").setViewName("/signin");
        registry.addViewController("/reset.html").setViewName("/reset");
        registry.addViewController("/reset").setViewName("/reset");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/",
                "/js/**","/css/**","/images/**","/index.html","/index","/client/customer/randomgood",
                "/signin.html","/signin","/signinCheck","/register.html","/register",
                "/registerUsernameCheck","/getKey","/about.html","/about","/client/notify",
                "/getPhoneNumber","/sendCode","/resetPassword","/reset","reset.html","/submitreset","/error");

    }

}
