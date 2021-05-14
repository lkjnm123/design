package com.graduation.ecommerce.design.service;

import com.graduation.ecommerce.design.entity.Business;
import com.graduation.ecommerce.design.entity.Good;
import com.graduation.ecommerce.design.entity.OrderBusiness;
import com.graduation.ecommerce.design.entity.Shop;

import java.sql.Date;
import java.util.List;

public interface BusinessService {
    public int registerByPhoneNumber(String username,String pwdsalt,String pwdhash,String phonenumber);
    public int registerByMailAddress(String username,String pwdsalt,String pwdhash,String mailaddress);
    public Business findUserByUsername(String username);
    public String findSaltByUsername(String username);
    public Business login(String username, String pwdhash);
    public int addShop(Shop shop);
    public List<Shop> shoplist(String id);
    public Shop findShopById(int id);
    public int addGood(Good good);
    public List<Good> goodlist(String id);
    public int deleteGood(int goodid);
    public int shopState(int businessid);
    public int insertGoodItemToOrderBusiness(OrderBusiness orderBusiness);
    public List<OrderBusiness> queryToBeDeliveredGoodItemFromOrderBusiness(int shopid);
    public Date queryOrderCreateTimeByOrderNumber(String ordernumber);
    public Good findGoodByGoodId(int goodid);
    public String findCustomerNameByCustomerId(int customerid);
    public int updateOrderBandCToDeliveredByOrderItemId(int orderitemid);
    public int updateOrderBandCInDeliveredByOrderItemId(int orderitemid);
    public Business findBusinessByUsernameAndPhonenumber(String username,String phonenumber);
    public int resetPasswordByUsername(String pwdhash,String username);
    public String findPwdhashFromBusinessById(int id);

}
