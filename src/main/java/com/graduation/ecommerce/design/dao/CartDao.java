package com.graduation.ecommerce.design.dao;

import com.graduation.ecommerce.design.entity.CartItem;
import com.graduation.ecommerce.design.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartDao {
    @Autowired
    private CustomerMapper customerMapper;

    public int addGoodItem(CartItem cartItem){
        return customerMapper.addCart(cartItem);
    }

    public List<CartItem> findCartItemListByCustomerId(int customerid){
        return customerMapper.findCartItemListByCustomerId(customerid);
    }

    public int deleteCartItem(int customerid,int goodid){
        return customerMapper.deleteCartItem(customerid,goodid);
    }
}
