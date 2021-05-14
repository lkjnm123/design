package com.graduation.ecommerce.design.service;

import com.graduation.ecommerce.design.dao.BusinessDao;
import com.graduation.ecommerce.design.dao.GoodDao;
import com.graduation.ecommerce.design.dao.OrderDao;
import com.graduation.ecommerce.design.dao.ShopDao;
import com.graduation.ecommerce.design.entity.Business;
import com.graduation.ecommerce.design.entity.Good;
import com.graduation.ecommerce.design.entity.OrderBusiness;
import com.graduation.ecommerce.design.entity.Shop;
import org.omg.CORBA.ORB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class BusinessServiceImpl implements BusinessService {
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private GoodDao goodDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public int registerByPhoneNumber(String username, String pwdsalt, String pwdhash, String phonenumber) {
        return businessDao.registerByPhoneNumber(username,pwdsalt,pwdhash,phonenumber);
    }

    @Override
    public int registerByMailAddress(String username, String pwdsalt, String pwdhash, String mailaddress) {
        return businessDao.registerByMailAddress(username,pwdsalt,pwdhash,mailaddress);
    }

    @Override
    public Business findUserByUsername(String username) {
        return businessDao.findBusinessByUsername(username);
    }

    @Override
    public String findSaltByUsername(String username) {
        return businessDao.findSaltByUsername(username);
    }

    @Override
    public Business login(String username, String pwdhash) {
        return businessDao.login(username,pwdhash);
    }

    @Override
    public int addShop(Shop shop) {
        return shopDao.addShop(shop);
    }

    @Override
    public List<Shop> shoplist(String id) {
        return shopDao.shopList(id);
    }

    @Override
    public Shop findShopById(int id) {
        return shopDao.findShopById(id);
    }

    @Override
    public int addGood(Good good) {
        return goodDao.addGood(good);
    }

    @Override
    public List<Good> goodlist(String id) {
        List<Good> list = goodDao.goodList(id);
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }
        return goodDao.goodList(id);
    }

    @Override
    public int deleteGood(int goodid) {
        return goodDao.deleteGood(goodid);
    }

    @Override
    public int shopState(int businessid) {
        return shopDao.shopState(businessid);
    }

    @Override
    public int insertGoodItemToOrderBusiness(OrderBusiness orderBusiness) {
        return orderDao.insertGoodItemToOrderBusiness(orderBusiness);
    }

    @Override
    public List<OrderBusiness> queryToBeDeliveredGoodItemFromOrderBusiness(int shopid) {
        return orderDao.queryToBeDeliveredGoodItemFromOrderBusiness(shopid);
    }

    @Override
    public Date queryOrderCreateTimeByOrderNumber(String ordernumber) {
        return orderDao.queryOrderCreateTimeByOrderNumber(ordernumber);
    }

    @Override
    public Good findGoodByGoodId(int goodid) {
        return orderDao.findGoodByGoodId(goodid);
    }

    @Override
    public String findCustomerNameByCustomerId(int customerid) {
        return orderDao.findCustomerNameByCustomerId(customerid);
    }

    @Override
    public int updateOrderBandCToDeliveredByOrderItemId(int orderitemid) {
        return orderDao.updateOrderBandCToDeliveredByOrderItemId(orderitemid);
    }

    @Override
    public int updateOrderBandCInDeliveredByOrderItemId(int orderitemid) {
        return orderDao.updateOrderBandCInDeliveredByOrderItemId(orderitemid);
    }

    @Override
    public Business findBusinessByUsernameAndPhonenumber(String username, String phonenumber) {
        return businessDao.findBusinessByUsernameAndPhonenumber(username,phonenumber);
    }

    @Override
    public int resetPasswordByUsername(String pwdhash, String username) {
        return businessDao.resetPasswordByUsername(pwdhash,username);
    }

    @Override
    public String findPwdhashFromBusinessById(int id) {
        return businessDao.findPwdhashFromBusinessById(id);
    }


}
