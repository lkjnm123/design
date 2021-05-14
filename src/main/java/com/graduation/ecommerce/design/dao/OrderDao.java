package com.graduation.ecommerce.design.dao;

import com.graduation.ecommerce.design.entity.*;
import com.graduation.ecommerce.design.mapper.BusinessMapper;
import com.graduation.ecommerce.design.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Repository
public class OrderDao {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private BusinessMapper businessMapper;

    public int insertOrder(InitialOrder initialOrder){
        return customerMapper.insertOrder(initialOrder);
    }

    public int insertOrderItem(OrderItem orderItem){
        return customerMapper.insertOrderItem(orderItem);
    }

    public int queryPayOrNotByOrderNumber(String ordernumber){
        return customerMapper.queryPayOrNotByOrderNumber(ordernumber);
    }

    public int overtimeCancelOrder(String ordernumber){
        return customerMapper.overtimeCancelOrder(ordernumber);
    }

    public int updatePayStatusAndPayAmount(BigDecimal payamount,String ordernumber){
        return customerMapper.updatePayStatusAndPayAmount(payamount,ordernumber);
    }

    public int updateOrderItemByOrderId(int orderid){
        return customerMapper.updateOrderItemByOrderId(orderid);
    }

    public int queryOrderIdByOrderNumber(String ordernumber){
        return customerMapper.queryOrderIdByOrderNumber(ordernumber);
    }

    public List<InitialOrder> queryPaidOrderByCustomerId(int customerid){
        return customerMapper.queryPaidOrderByCustomerId(customerid);
    }

    public List<OrderItem> queryToBeReceivedGoodItem(int orderid){
        return customerMapper.queryToBeReceivedGoodItem(orderid);
    }

    public int insertGoodItemToOrderCustomer(OrderCustomer orderCustomer){
        return customerMapper.insertGoodItemToOrderCustomer(orderCustomer);
    }

    public int insertGoodItemToOrderBusiness(OrderBusiness orderBusiness){
        return businessMapper.insertGoodItemToOrderBusiness(orderBusiness);
    }

    public int queryCustomerIdFromInitialOrderByOrderNumber(String ordernumber){
        return customerMapper.queryCustomerIdFromInitialOrderByOrderNumber(ordernumber);
    }

    public int findShopIdByGoodId(int goodid){
        return customerMapper.findShopIdByGoodId(goodid);
    }

    public List<OrderCustomer> queryToBeReceivedGoodItemFromOrderCustomer(int customerid){
        return customerMapper.queryToBeReceivedGoodItemFromOrderCustomer(customerid);
    }

    public List<OrderBusiness> queryToBeDeliveredGoodItemFromOrderBusiness(int shopid){
        return businessMapper.queryToBeDeliveredGoodItemFromOrderBusiness(shopid);
    }

    public int findOrderItemidByOrderCustomerId(int ordercustomerid){
        return customerMapper.findOrderItemidByOrderCustomerId(ordercustomerid);
    }

    public int findOrderIdByOrderItemId(int orderitemid){
        return customerMapper.findOrderIdByOrderItemId(orderitemid);
    }

    public Date findDateByOrderId(int orderid){
        return customerMapper.findDateByOrderId(orderid);
    }

    public List<InitialOrder> queryNotPayOrderByCustomerId(int customerid){
        return customerMapper.queryNotPayOrderByCustomerId(customerid);
    }

    public int cancelOrderManually(int customerid,String ordernumber){
        return customerMapper.cancelOrderManually(customerid,ordernumber);
    }

    public List<InitialOrder> queryAllOrderByCustomerId(int customerid){
        return customerMapper.queryAllOrderByCustomerId(customerid);
    }

    public Date queryOrderCreateTimeByOrderNumber(String ordernumber){
        return businessMapper.queryOrderCreateTimeByOrderNumber(ordernumber);
    }

    public Good findGoodByGoodId(int goodid){
        return businessMapper.findGoodByGoodId(goodid);
    }

    public String findCustomerNameByCustomerId(int customerid){
        return businessMapper.findCustomerNameByCustomerId(customerid);
    }

    public int updateOrderBandCToDeliveredByOrderItemId(int orderitemid){
        //TODO 返回逻辑优化
        customerMapper.updateOrderCustomerToDeliveredByOrderItemId(orderitemid);
        return businessMapper.updateOrderBusinessToDeliveredByOrderItemId(orderitemid);
    }

    public int updateOrderBandCInDeliveredByOrderItemId(int orderitemid){
        //TODO 返回逻辑优化
        customerMapper.updateOrderCustomerInDeliveredByOrderItemId(orderitemid);
        return businessMapper.updateOrderBusinessInDeliveredByOrderItemId(orderitemid);
    }

    public InitialOrder findInitialOrderByOrderNumber(String ordernumber){
        return customerMapper.findInitialOrderByOrderNumber(ordernumber);
    }

    public List<OrderItem> queryOrderItemListByOrderId(int orderid){
        return customerMapper.queryOrderItemListByOrderId(orderid);
    }

    public OrderCustomer getOrderCusotmerByOrderItemId(int orderitemid){
        return customerMapper.getOrderCusotmerByOrderItemId(orderitemid);
    }

    public int confirmBandCReceiptByOrderItemId(int orderitemid,int customerid){
        businessMapper.confirmReceiptByOrderItemId(orderitemid,customerid);
        return customerMapper.confirmReceiptByOrderItemId(orderitemid,customerid);
    }

    public String QueryAddressContextByOrderNumber(String order_number){
        return customerMapper.QueryAddressContextByOrderNumber(order_number);
    }

    public Date findCreateOrderTimeByOrderNumber(String ordernumber){
        return customerMapper.findCreateOrderTimeByOrderNumber(ordernumber);
    }

    public int finishOrder(int ordercustomerid){
        int n1 = businessMapper.finishOrder(ordercustomerid);
        int n2 = customerMapper.finishOrder(ordercustomerid);
        if(n1==n2){
            return 1;
        }else{
            return 0;
        }
    }

    public BigDecimal getPayAmountByOrderNumber(String ordernumber){
        return customerMapper.getPayAmountByOrderNumber(ordernumber);
    }

    public int findGoodIdByOrderCustomerId(int ordercustomerid){
        return customerMapper.findGoodIdByOrderCustomerId(ordercustomerid);
    }

    public int refundOrderByOrderNumber(String ordernumber){
        int n1 = businessMapper.refundOrderByOrderNumber(ordernumber);
        int n2 = customerMapper.refundOrderByOrderNumber(ordernumber);
        if(n1==n2){
            return 1;
        }else{
            return 0;
        }
    }

    public int refundIntialOrderByOrderNumber(String ordernumber){
        return customerMapper.refundIntialOrderByOrderNumber(ordernumber);
    }
}
