package com.graduation.ecommerce.design.dao;

import com.graduation.ecommerce.design.entity.Good;
import com.graduation.ecommerce.design.entity.GoodReviews;
import com.graduation.ecommerce.design.entity.Shop;
import com.graduation.ecommerce.design.mapper.BusinessMapper;
import com.graduation.ecommerce.design.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GoodDao {
    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private CustomerMapper customerMapper;

    public int addGood(Good good){
        return businessMapper.addGood(good);
    }

    public List<Good> goodList(String id){
        return businessMapper.goodlist(id);
    }

    public int deleteGood(int id){
        return businessMapper.deleteGood(id);
    }

    public List<Good> searchGoodList(String keyword){
        return customerMapper.findGoodByKeyword(keyword);
    }

    public Shop findShopByShopId(int shopid){
        return customerMapper.findShopByShopId(shopid);
    }

    public int findShopIdByGoodId(int goodid){
        return customerMapper.findShopIdByGoodId(goodid);
    }

    public Good findGoodByGoodId(int goodid){
        return customerMapper.findGoodByGoodId(goodid);
    }

    public List<Good> takeRandomGood(){
        return customerMapper.takeRandomGood();
    }

    public List<Good> findAllGoodByShopId(int shopid){
        return customerMapper.findAllGoodByShopId(shopid);
    }

    public int queryGoodNumberByGoodId(int goodid){
        return customerMapper.queryGoodNumberByGoodId(goodid);
    }

    public List<GoodReviews> queryGoodCommentByGoodId(int goodid){
        return customerMapper.queryGoodCommentByGoodId(goodid);
    }
}
