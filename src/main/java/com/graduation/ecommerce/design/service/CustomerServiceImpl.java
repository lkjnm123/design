package com.graduation.ecommerce.design.service;

import com.graduation.ecommerce.design.dao.*;
import com.graduation.ecommerce.design.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private GoodDao goodDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private CartDao cartDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public int registerByPhoneNumber(String username, String pwdsalt, String pwdhash, String phonenumber) {
        return customerDao.registerByPhoneNumber(username,pwdsalt,pwdhash,phonenumber);
    }

    @Override
    public int registerByMailAddress(String username, String pwdsalt, String pwdhash, String mailaddress) {
        return 0;
    }

    @Override
    public Customer findUserByUsername(String username) {
        return customerDao.findConsumerByUsername(username);
    }

    @Override
    public String findSaltByUsername(String username) {
        return customerDao.findSaltByUsername(username);
    }

    @Override
    public Customer login(String username, String pwdhash) {
        return customerDao.login(username,pwdhash);
    }

    @Override
    public List<Good> findGoodByKeyword(String keyword) {
        return goodDao.searchGoodList(keyword);
    }

    @Override
    public int addAddress(CustomerAddress customerAddress) {
        return addressDao.addAddress(customerAddress);
    }

    @Override
    public List<CustomerAddress> addressList(int customerid) {
        return addressDao.addressList(customerid);
    }

    @Override
    public int addCart(CartItem cartItem) {
        return cartDao.addGoodItem(cartItem);
    }

    @Override
    public Shop findShopByShopId(int shopid) {
        return goodDao.findShopByShopId(shopid);
    }

    @Override
    public List<CartItem> findCartItemListByCustomerId(int customerid) {
        return cartDao.findCartItemListByCustomerId(customerid);
    }

    @Override
    public Good findGoodByGoodId(int goodid) {
        return goodDao.findGoodByGoodId(goodid);
    }


    @Override
    public List<Good> takeRandomGood() {
        return goodDao.takeRandomGood();
    }

    @Override
    public List<Good> findAllGoodByShopId(int shopid) {
        return goodDao.findAllGoodByShopId(shopid);
    }

    @Override
    public int deleteCartItem(int customerid, int goodid) {
        return cartDao.deleteCartItem(customerid,goodid);
    }

    @Override
    public int insertOrder(InitialOrder initialOrder) {
        return orderDao.insertOrder(initialOrder);
    }

    @Override
    public int insertOrderItem(OrderItem orderItem) {
        return orderDao.insertOrderItem(orderItem);
    }

    @Override
    public int queryPayOrNotByOrderNumber(String ordernumber) {
        return orderDao.queryPayOrNotByOrderNumber(ordernumber);
    }

    @Override
    public int overtimeCancelOrder(String ordernumber) {
        return orderDao.overtimeCancelOrder(ordernumber);
    }

    @Override
    public int updatePayStatusAndPayAmount(BigDecimal payamount,String ordernumber) {
        return orderDao.updatePayStatusAndPayAmount(payamount,ordernumber);
    }

    @Override
    public int queryGoodNumberByGoodId(int goodid) {
        return goodDao.queryGoodNumberByGoodId(goodid);
    }

    @Override
    public int updateOrderItemByOrderId(int orderid) {
        return orderDao.updateOrderItemByOrderId(orderid);
    }

    @Override
    public int queryOrderIdByOrderNumber(String ordernumber) {
        return orderDao.queryOrderIdByOrderNumber(ordernumber);
    }

    @Override
    public List<InitialOrder> queryPaidOrderByCustomerId(int customerid) {
        return orderDao.queryPaidOrderByCustomerId(customerid);
    }

    @Override
    public List<OrderItem> queryToBeReceivedGoodItem(int orderid) {
        return orderDao.queryToBeReceivedGoodItem(orderid);
    }

    @Override
    public int insertGoodItemToOrderCustomer(OrderCustomer orderCustomer) {
        return orderDao.insertGoodItemToOrderCustomer(orderCustomer);
    }



    @Override
    public int queryCustomerIdFromInitialOrderByOrderNumber(String ordernumber) {
        return orderDao.queryCustomerIdFromInitialOrderByOrderNumber(ordernumber);
    }

    @Override
    public int findShopIdByGoodId(int goodid) {
        return orderDao.findShopIdByGoodId(goodid);
    }

    @Override
    public List<OrderCustomer> queryToBeReceivedGoodItemFromOrderCustomer(int customerid) {
        return orderDao.queryToBeReceivedGoodItemFromOrderCustomer(customerid);
    }

    @Override
    public int findOrderItemidByOrderCustomerId(int ordercustomerid) {
        return orderDao.findOrderItemidByOrderCustomerId(ordercustomerid);
    }

    @Override
    public int findOrderIdByOrderItemId(int orderitemid) {
        return orderDao.findOrderIdByOrderItemId(orderitemid);
    }

    @Override
    public Date findDateByOrderId(int orderid) {
        return orderDao.findDateByOrderId(orderid);
    }

    @Override
    public List<InitialOrder> queryNotPayOrderByCustomerId(int customerid) {
        return orderDao.queryNotPayOrderByCustomerId(customerid);
    }

    @Override
    public int cancelOrderManually(int customerid, String ordernumber) {
        return orderDao.cancelOrderManually(customerid,ordernumber);
    }

    @Override
    public List<InitialOrder> queryAllOrderByCustomerId(int customerid) {
        return orderDao.queryAllOrderByCustomerId(customerid);
    }

    @Override
    public InitialOrder findInitialOrderByOrderNumber(String ordernumber) {
        return orderDao.findInitialOrderByOrderNumber(ordernumber);
    }

    @Override
    public List<OrderItem> queryOrderItemListByOrderId(int orderid) {
        return orderDao.queryOrderItemListByOrderId(orderid);
    }

    @Override
    public OrderCustomer getOrderCusotmerByOrderItemId(int orderitemid) {
        return orderDao.getOrderCusotmerByOrderItemId(orderitemid);
    }

    @Override
    public int confirmBandCReceiptByOrderItemId(int orderitemid, int customerid) {
        return orderDao.confirmBandCReceiptByOrderItemId(orderitemid,customerid);
    }

    @Override
    public CustomerAddress findAddressById(int addressid) {
        return addressDao.findAddressById(addressid);
    }

    @Override
    public String QueryAddressContextByOrderNumber(String order_number) {
        return orderDao.QueryAddressContextByOrderNumber(order_number);
    }

    @Override
    public Customer findCustomerByUsernameAndPhonenumber(String username, String phonenumber) {
        return customerDao.findCustomerByUsernameAndPhonenumber(username,phonenumber);
    }

    @Override
    public int resetPasswordByUsername(String pwdhash, String username) {
        return customerDao.resetPasswordByUsername(pwdhash,username);
    }

    @Override
    public String findPwdhashFromCustomerById(int id) {
        return customerDao.findPwdhashFromCustomerById(id);
    }

    @Override
    public int addGoodComment(GoodReviews goodReviews) {
        return customerDao.addGoodComment(goodReviews);
    }

    @Override
    public List<OrderCustomer> queryToBeEvaluateGoodByCustomerId(int customerid) {
        return customerDao.queryToBeEvaluateGoodByCustomerId(customerid);
    }

    @Override
    public Date findCreateOrderTimeByOrderNumber(String ordernumber) {
        return orderDao.findCreateOrderTimeByOrderNumber(ordernumber);
    }

    @Override
    public int finishOrder(int ordercustomerid) {
        return orderDao.finishOrder(ordercustomerid);
    }

    @Override
    public BigDecimal getPayAmountByOrderNumber(String ordernumber) {
        return orderDao.getPayAmountByOrderNumber(ordernumber);
    }

    @Override
    public List<GoodReviews> queryGoodCommentByGoodId(int goodid) {
        return goodDao.queryGoodCommentByGoodId(goodid);
    }

    @Override
    public String findUserNameById(int id) {
        return customerDao.findUserNameById(id);
    }

    @Override
    public int findGoodIdByOrderCustomerId(int ordercustomerid) {
        return orderDao.findGoodIdByOrderCustomerId(ordercustomerid);
    }

    @Override
    public int refundOrderByOrderNumber(String ordernumber) {
        return orderDao.refundOrderByOrderNumber(ordernumber);
    }

    @Override
    public int refundIntialOrderByOrderNumber(String ordernumber) {
        return orderDao.refundIntialOrderByOrderNumber(ordernumber);
    }


}
