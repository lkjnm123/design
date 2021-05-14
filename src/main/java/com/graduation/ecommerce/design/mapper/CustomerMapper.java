package com.graduation.ecommerce.design.mapper;

import com.graduation.ecommerce.design.entity.*;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Mapper
public interface CustomerMapper {
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn="id")
    @Insert("insert into customer(username,pwdsalt,pwdhash,phonenumber) values(#{username},#{pwdsalt},#{pwdhash},#{phonenumber})")
    public int registerByPhone(Customer customer);

    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn="id")
    @Insert("insert into customer(username,pwdsalt,pwdhash,mailaddress) values(#{username},#{pwdsalt},#{pwdhash},#{mailaddress})")
    public int registerByMail(Customer customer);

    @Select("select * from customer where username=#{username} and pwdhash=#{pwdhash}")
    public Customer login(String username,String pwdhash);

    @Select("select pwdsalt from customer where username=#{username}")
    public String findSaltByUsername(String username);

    @Select("select * from customer where username=#{username}")
    public Customer findUserByUsername(String username);

    @Select("select * from good where goodname like concat('%',#{keyword},'%')")
    public List<Good> findGoodByKeyword(String keyword);

    @Select("select * from shop where id=#{shopid}")
    public Shop findShopByShopId(int shopid);

    //根据goodid 查询shopId
    @Select("select id from good where goodid=#{goodid}")
    public int findShopIdByGoodId(int goodid);

    @Insert("insert into customeraddress(addressid,customerid,realname,phonenumber,address) values(#{addressid},#{customerid},#{realname},#{phonenumber},#{address}) on duplicate key update realname=#{realname},phonenumber=#{phonenumber},address=#{address}")
    public int addAddress(CustomerAddress customerAddress);

    @Select("select * from customeraddress where customerid=#{customerid}")
    public List<CustomerAddress> addresslist(int customerid);

    @Options(useGeneratedKeys = true,keyProperty = "cartitemid",keyColumn="cartitemid")
    @Insert("insert into cartitem(cartitemid,customerid,goodid,number,customer_status,shop_status,admin_status,business_status,updatetime) values(#{cartitemid},#{customerid},#{goodid},#{number},#{customer_status},#{shop_status},#{admin_status},#{business_status},#{updatetime}) on duplicate key update number=#{number},customer_status=#{customer_status},updatetime=#{updatetime}")
    public int addCart(CartItem cartItem);
    //重复添加 查找返回出问题
    //确保不会重复 为customerid和goodid添加唯一索引，出现重复时不会添加而是更新字段

    @Select("select * from cartitem where customerid=#{customerid} and customer_status=1")
    public List<CartItem> findCartItemListByCustomerId(int customerid);

    @Select("select * from good where goodid=#{goodid}")
    public Good findGoodByGoodId(int goodid);

    //弃用 避免查询不到，使用随机数 获取商品表的商品数量
    @Select("select count(1) from good")
    public int countNumberFromGood();

    //直接从数据库中随机拿取数据
    @Select("select * from good order by rand() limit 5")
    public List<Good> takeRandomGood();

    @Select("select * from good where id=#{shopid}")
    public List<Good> findAllGoodByShopId(int shopid);

    //删除将customer_status字段置0，方便以后再添加
    @Update("update cartitem set customer_status=0 where customerid=#{customerid} and goodid=#{goodid}")
    public int deleteCartItem(int customerid,int goodid);

    //查询good数量
    @Select("select goodnumber from good where goodid=#{goodid}")
    public int queryGoodNumberByGoodId(int goodid);

    @Options(useGeneratedKeys = true,keyProperty = "order_id",keyColumn="order_id")
    @Insert("insert into initial_order(order_number,customer_id,trade_status,pay_status,order_amount,pay_amount,create_time,address_context) values (#{order_number},#{customer_id},#{trade_status},#{pay_status},#{order_amount},#{pay_amount},#{create_time},#{address_context})")
    public int insertOrder(InitialOrder initialOrder);

    @Options(useGeneratedKeys = true,keyProperty = "order_item_id",keyColumn="order_item_id")
    @Insert("insert into order_item(order_id,goodid,goodnumber,item_status) values (#{order_id},#{goodid},#{goodnumber},#{item_status})")
    public int insertOrderItem(OrderItem orderItem);

    //查询是否已支付
    @Select("select pay_status from initial_order where order_number=#{ordernumber}")
    public int queryPayOrNotByOrderNumber(String ordernumber);

    //超时自动取消订单
    @Update("update initial_order set trade_status=2 where order_number=#{ordernumber}")
    public int overtimeCancelOrder(String ordernumber);

    //支付成功修改为已支付状态并添加已支付金额(添加筛选条件 订单状态必须为进行状态）
    @Update("update initial_order set pay_status=1,pay_amount=#{payamount} where order_number=#{ordernumber} and trade_status=0")
    public int updatePayStatusAndPayAmount(BigDecimal payamount, String ordernumber);

    //支付成功 根据orderid激活order_item表中相关的商品条目
    @Update("update order_item set item_status=1 where order_id=#{orderid}")
    public int updateOrderItemByOrderId(int orderid);

    //根据外部的order_number查询order表中的自增主键order_id字段
    @Select("select order_id from initial_order where order_number=#{ordernumber}")
    public int queryOrderIdByOrderNumber(String ordernumber);

    //根据外部order_number 查询initial_order表中所属的顾客id
    @Select("select customer_id from initial_order where order_number=#{ordernumber}")
    public int queryCustomerIdFromInitialOrderByOrderNumber(String ordernumber);

    //查询顾客已支付的订单 //筛选条件 已支付并且交易进行中 //用于向顾客展示待收货的物品
    @Select("select * from initial_order where customer_id=#{customerid} and pay_status=1 and trade_status=0")
    public List<InitialOrder> queryPaidOrderByCustomerId(int customerid);

    //查询顾客所有订单
    @Select("select * from initial_order where customer_id=#{customerid}")
    public List<InitialOrder> queryAllOrderByCustomerId(int customerid);

    //查询OrderItem待收货的商品条目
    @Select("select * from order_item where order_id=#{orderid} and item_status=1")
    public List<OrderItem> queryToBeReceivedGoodItem(int orderid);

    //向order_customer表中添加商品条目
    @Options(useGeneratedKeys = true,keyProperty = "order_customer_id",keyColumn="order_customer_id")
    @Insert("insert into order_customer(order_item_id,customer_id,shop_id,good_id,good_number,address_context,order_number,good_status,express_number) values(#{order_item_id},#{customer_id},#{shop_id},#{good_id},#{good_number},#{address_context},#{order_number},#{good_status},#{express_number})")
    public int insertGoodItemToOrderCustomer(OrderCustomer orderCustomer);
    //TODO 多商家商品订单处理

    //顾客查询待收货的商品条目
    //good_status; 0待发货， 1 运输中，2 配送中，3 待评价 ，4 已完成
    @Select("select * from order_customer where customer_id=#{customerid} and good_status!=4")
    public List<OrderCustomer> queryToBeReceivedGoodItemFromOrderCustomer(int customerid);

    //TODO 频繁查库优化方法
    //根据order_customer中的order_item id 反查order_item表中的 initial_order id
    @Select("select order_item_id from order_customer where order_customer_id=#{ordercustomerid}")
    public int findOrderItemidByOrderCustomerId(int ordercustomerid);

    //根据orderItem id查询order_item表中的order_id
    @Select("select order_id from order_item where order_item_id=#{orderitemid}")
    public int findOrderIdByOrderItemId(int orderitemid);

    //根据order_id 查询initial_order表中的创建时间
    @Select("select create_time from initial_order where order_id=#{orderid}")
    public Date findDateByOrderId(int orderid);

    //查询initial_order中未支付的订单（订单处于进行状态并且未支付）
    @Select("select * from initial_order where trade_status=0 and pay_status=0")
    public List<InitialOrder> queryNotPayOrderByCustomerId(int customerid);

    //手动取消订单
    //当出现关闭系统Redis仍在运行情况时，key过期后不能监听到，即无法自动取消订单 导致订单会一直处于未支付状态
    @Update("update initial_order set trade_status=2 where customer_id=#{customerid} and order_number=#{ordernumber} and pay_status=0")
    public int cancelOrderManually(int customerid,String ordernumber);

    //修改order_customer表中商品条目 good_status, 0待发货 1运输中 2配送中 3已签收 4已完成 9已取消
    @Update("update order_customer set good_status = 1 where order_item_id=#{orderitemid} and good_status=0")
    public int updateOrderCustomerToDeliveredByOrderItemId(int orderitemid);

    //从运输中修改为 配送中
    @Update("update order_customer set good_status = 2 where order_item_id=#{orderitemid} and good_status=1")
    public int updateOrderCustomerInDeliveredByOrderItemId(int orderitemid);

    //查询总订单,根据订单编号 order_number 查询
    @Select("select * from initial_order where order_number=#{ordernumber}")
    public InitialOrder findInitialOrderByOrderNumber(String ordernumber);

    //查询附属于initial_order中的商品条目
    @Select("select * from order_item where order_id=#{orderid}")
    public List<OrderItem> queryOrderItemListByOrderId(int orderid);

    //根据order_item_id 查询order_customer表中商品条目
    @Select("select * from order_customer where order_item_id=#{orderitemid}")
   public OrderCustomer getOrderCusotmerByOrderItemId(int orderitemid);

    //顾客确认收货
    @Update("update order_customer set good_status=3 where good_status=2 and order_item_id=#{orderitemid} and customer_id=#{customerid}")
    public int confirmReceiptByOrderItemId(int orderitemid,int customerid);

    //根据addressid查询customeraddress表中收货地址内容
    @Select("select * from customeraddress where addressid=#{addressid}")
    public CustomerAddress findAddressById(int addressid);
    //根据oder_number 查询initial_order中的收货地址
    @Select("select address_context from initial_order where order_number=#{order_number}")
    public String QueryAddressContextByOrderNumber(String order_number);

    //重置密码功能 校验用户名和手机号是否存在
    @Select("select * from customer where username=#{username} and phonenumber=#{phonenumber}")
    public Customer findCustomerByUsernameAndPhonenumber(String username,String phonenumber);

    //重置密码功能 修改顾客表
    @Update("update customer set pwdhash=#{pwdhash} where username=#{username}")
    public int resetPasswordByUsername(String pwdhash,String username);

    //修改密码功能 获取哈希值进行比对
    @Select("select pwdhash from customer where id=#{id}")
    public String findPwdhashFromCustomerById(int id);

    //向商品评论表中添加内容
    @Options(useGeneratedKeys = true,keyProperty = "reviews_id",keyColumn="reviews_id")
    @Insert("insert into good_reviews(reviews_id,ordercustomer_id,customer_id,comment,goodid) values(#{reviews_id},#{ordercustomer_id},#{customer_id},#{comment},#{goodid}) on duplicate key update comment=#{comment}")
    public int addGoodComment(GoodReviews goodReviews);

    //查询已签收的商品列表
    //good_status, 0待发货 1运输中 2配送中 3已签收 4已完成 9已取消
    @Select("select * from order_customer where customer_id = #{customerid} and good_status=3")
    public List<OrderCustomer> queryToBeEvaluateGoodByCustomerId(int customerid);

    //根据订单编号查询 订单创建修改时间
    @Select("select create_time from initial_order where order_number=#{ordernumber}")
    public Date findCreateOrderTimeByOrderNumber(String ordernumber);

    //用户评价完成 那么订单中此商品状态修改为已完成
    @Update("update order_customer set good_status=4 where order_customer_id=#{ordercustomerid} and good_status=3")
    public int finishOrder(int ordercustomerid);

    //根据订单号查询付款金额，方便退款
    @Select("select pay_amount from initial_order where order_number=#{ordernumber}")
    public BigDecimal getPayAmountByOrderNumber(String ordernumber);

    @Select("select * from good_reviews where goodid=#{goodid}")
    public List<GoodReviews> queryGoodCommentByGoodId(int goodid);

    //根据用户id查询用户名
    @Select("select username from customer where id=#{id}")
    public String findUserNameById(int id);

    //根据ordercustomerid 查询 goodid
    @Select("select good_id from order_customer where order_customer_id=#{ordercustomerid}")
    public int findGoodIdByOrderCustomerId(int ordercustomerid);

    //修改 顾客条目表中的 商品状态为已退款
    @Update("update order_customer set good_status=9 where order_number=#{ordernumber}")
    public int refundOrderByOrderNumber(String ordernumber);

    //修改订单总表中的订单状态为已退款
    @Update("update initial_order set pay_status=2 where order_number=#{ordernumber}")
    public int refundIntialOrderByOrderNumber(String ordernumber);
 }
