package com.graduation.ecommerce.design.service;

import com.graduation.ecommerce.design.entity.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public interface CustomerService {
    public int registerByPhoneNumber(String username,String pwdsalt,String pwdhash,String phonenumber);
    public int registerByMailAddress(String username,String pwdsalt,String pwdhash,String mailaddress); //待开发
    public Customer findUserByUsername(String username);
    public String findSaltByUsername(String username);
    public Customer login(String username,String pwdhash);
    public List<Good> findGoodByKeyword(String keyword);
    public int addAddress(CustomerAddress customerAddress);
    public List<CustomerAddress> addressList(int customerid);
    public int addCart(CartItem cartItem);
    public Shop findShopByShopId(int shopid);
    public List<CartItem> findCartItemListByCustomerId(int customerid);
    public Good findGoodByGoodId(int goodid);
    public List<Good> takeRandomGood();
    public List<Good> findAllGoodByShopId(int shopid);
    public int deleteCartItem(int customerid,int goodid);
    public int insertOrder(InitialOrder initialOrder);
    public int insertOrderItem(OrderItem orderItem);
    public int queryPayOrNotByOrderNumber(String ordernumber);
    public int overtimeCancelOrder(String ordernumber);
    public int updatePayStatusAndPayAmount(BigDecimal payamount,String ordernumber);
    public int queryGoodNumberByGoodId(int goodid);
    public int updateOrderItemByOrderId(int orderid);
    public int queryOrderIdByOrderNumber(String ordernumber);
    public List<InitialOrder> queryPaidOrderByCustomerId(int customerid);
    public List<OrderItem> queryToBeReceivedGoodItem(int orderid);
    public int insertGoodItemToOrderCustomer(OrderCustomer orderCustomer);
    public int queryCustomerIdFromInitialOrderByOrderNumber(String ordernumber);
    public int findShopIdByGoodId(int goodid);
    public List<OrderCustomer> queryToBeReceivedGoodItemFromOrderCustomer(int customerid);
    public int findOrderItemidByOrderCustomerId(int ordercustomerid);
    public int findOrderIdByOrderItemId(int orderitemid);
    public Date findDateByOrderId(int orderid);
    public List<InitialOrder> queryNotPayOrderByCustomerId(int customerid);
    public int cancelOrderManually(int customerid,String ordernumber);
    public List<InitialOrder> queryAllOrderByCustomerId(int customerid);
    public InitialOrder findInitialOrderByOrderNumber(String ordernumber);
    public List<OrderItem> queryOrderItemListByOrderId(int orderid);
    public OrderCustomer getOrderCusotmerByOrderItemId(int orderitemid);
    public int confirmBandCReceiptByOrderItemId(int orderitemid,int customerid);
    public CustomerAddress findAddressById(int addressid);
    public String QueryAddressContextByOrderNumber(String order_number);
    public Customer findCustomerByUsernameAndPhonenumber(String username,String phonenumber);
    public int resetPasswordByUsername(String pwdhash,String username);
    public String findPwdhashFromCustomerById(int id);
    public int addGoodComment(GoodReviews goodReviews);
    public List<OrderCustomer> queryToBeEvaluateGoodByCustomerId(int customerid);
    public Date findCreateOrderTimeByOrderNumber(String ordernumber);
    public int finishOrder(int ordercustomerid);
    public BigDecimal getPayAmountByOrderNumber(String ordernumber);
    public List<GoodReviews> queryGoodCommentByGoodId(int goodid);
    public String findUserNameById(int id);
    public int findGoodIdByOrderCustomerId(int ordercustomerid);
    public int refundOrderByOrderNumber(String ordernumber);
    public int refundIntialOrderByOrderNumber(String ordernumber);
}
