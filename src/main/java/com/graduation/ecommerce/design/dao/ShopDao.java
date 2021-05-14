package com.graduation.ecommerce.design.dao;

import com.graduation.ecommerce.design.entity.Shop;
import com.graduation.ecommerce.design.mapper.BusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShopDao {
    @Autowired
    private BusinessMapper businessMapper;

    public int addShop(Shop shop){
        return businessMapper.addShop(shop);
    }

    public List<Shop> shopList(String id){
        return businessMapper.shoplist(id);
    }

    public int shopState(int businessid){
        return businessMapper.shopState(businessid);
    }

    public Shop findShopById(int id){
        return businessMapper.findShopById(id);
    }
}
