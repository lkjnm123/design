package com.graduation.ecommerce.design.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {


    @RequestMapping(value="/justtest",method = {RequestMethod.POST},consumes = "application/json")
    public void test(@RequestBody Map<String, String> username){
        /*
        String result = username.get("username");
        System.out.println(result);
         */
    }

    @ResponseBody
    @RequestMapping(value="/JedisTest")
    public Map<String,String> redisTest(){
        Map<String,String> map = new HashMap<>();
        map.put("msg","success");
        return map;
        /*
        String key = "1";
        String value = "1";
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.select(1);
        String n = jedis.setex(key,20,value);
        Map<String,String> map = new HashMap<>();
        map.put("msg",n);
        return map;
         */
    }


}
