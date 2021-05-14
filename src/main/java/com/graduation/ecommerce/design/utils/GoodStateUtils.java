package com.graduation.ecommerce.design.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodStateUtils {
    public static int finalState(int status,int status_admin,int state){
        Map<Integer,Integer> stateMap = new HashMap<>();
        if(status==status_admin&&status_admin==state&&state==1){
            //仅有全为1 的情况为上架状态
            return 1;
        }else if(status_admin==0){
            return 2; //违规下架
        }else{
            return 0; //自行下架
        }
    }
}
