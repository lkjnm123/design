package com.graduation.ecommerce.design.component;

import com.graduation.ecommerce.design.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    @Autowired
    private CustomerService customerService;

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        String channel = new String(message.getChannel(),StandardCharsets.UTF_8);
        if(channel.startsWith("__keyevent@1__")){
            //根据过期的key 查库是否已经支付
            int result = customerService.queryPayOrNotByOrderNumber(key);
            if(result==1){
                return;
            }else{
                //未支付 取消订单
                int n = customerService.overtimeCancelOrder(key);
                if(n!=0){
                    System.out.println("取消编号为"+key+"的订单成功");
                }
            }
        }
    }

}
