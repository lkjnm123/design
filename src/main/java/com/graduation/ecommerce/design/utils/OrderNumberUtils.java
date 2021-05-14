package com.graduation.ecommerce.design.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderNumberUtils {
    public static String generateOrderNumber(){
        //时间精确到秒级别
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowtime = sdf.format(new Date());
        Random random = new Random();
        //末尾添加三位随机数
        for(int i=0;i<3;i++) {
            nowtime+=random.nextInt(10);
        }
        return nowtime;
    }
}
