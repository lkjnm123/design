package com.graduation.ecommerce.design.mapper;

import com.graduation.ecommerce.design.entity.Business;
import com.graduation.ecommerce.design.entity.Good;
import com.graduation.ecommerce.design.entity.OrderBusiness;
import com.graduation.ecommerce.design.entity.Shop;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;

@Mapper
public interface BusinessMapper {
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn="id")
    @Insert("insert into business(username,pwdsalt,pwdhash,phonenumber) values(#{username},#{pwdsalt},#{pwdhash},#{phonenumber})")
    public int registerByPhone(Business business);

    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn="id")
    @Insert("insert into business(username,pwdsalt,pwdhash,mailaddress) values(#{username},#{pwdsalt},#{pwdhash},#{mailaddress})")
    public int registerByMail(Business customer);

    @Select("select * from business where username=#{username} and pwdhash=#{pwdhash}")
    public Business login(String username,String pwdhash);

    @Select("select pwdsalt from business where username=#{username}")
    public String findSaltByUsername(String username);

    @Select("select * from business where username=#{username}")
    public Business findUserByUsername(String username);

    @Insert("insert into shop(id,shopname,introduce,status,admin_status) values(#{id},#{shopname},#{introduce},#{status},#{admin_status}) on duplicate key update shopname=#{shopname},introduce=#{introduce},status=#{status}")
    public int addShop(Shop shop);

    @Select("select * from shop where id=#{id}")
    public Shop findShopById(int id);

    @Select("select * from shop where id=#{id}")
    public List<Shop> shoplist(String id);

    @Options(useGeneratedKeys = true,keyProperty = "goodid",keyColumn = "goodid")
    @Insert("insert into good(id,goodid,goodname,brand,introduce,price,goodnumber,weight,address,image,status,admin_status,state) values(#{id},#{goodid},#{goodname},#{brand},#{introduce},#{price},#{goodnumber},#{weight},#{address},#{image},#{status},#{admin_status},#{state}) on duplicate key update goodname=#{goodname},brand=#{brand},introduce=#{introduce},price=#{price},goodnumber=#{goodnumber},weight=#{weight},address=#{address},image=#{image},status=#{status},state=#{state}")
    public int addGood(Good good);

    @Select("select * from good where id=#{id}")
    public List<Good> goodlist(String id);

    @Delete("delete from good where goodid=#{goodid}")
    public int deleteGood(int goodid);

    @Select("select admin_status from shop where id=#{businessid}")
    public int shopState(int businessid);

    //向order_business表中添加商品条目
    @Options(useGeneratedKeys = true,keyProperty = "order_business_id",keyColumn="order_business_id")
    @Insert("insert into order_business(order_item_id,shop_id,customer_id,good_id,good_number,address_context,order_number,good_status,express_number) values(#{order_item_id},#{shop_id},#{customer_id},#{good_id},#{good_number},#{address_context},#{order_number},#{good_status},#{express_number})")
    public int insertGoodItemToOrderBusiness(OrderBusiness orderBusiness);

    //商家查询待发货的商品条目
    //good_status; 0待发货， 1 运输中，2 配送中，3 待评价 ，4 已完成
    @Select("select * from order_business where shop_id=#{businessid} and good_status!=4")
    public List<OrderBusiness> queryToBeDeliveredGoodItemFromOrderBusiness(int businessid);

    //根据订单编号 查询 initial_order中的订单创建时间
    @Select("select create_time from initial_order where order_number=#{ordernumber}")
    public Date queryOrderCreateTimeByOrderNumber(String ordernumber);

    @Select("select * from good where goodid=#{goodid}")
    public Good findGoodByGoodId(int goodid);

    @Select("select username from customer where id=#{customerid}")
    public String findCustomerNameByCustomerId(int customerid);

    //修改order_business表中商品条目 good_status, 0待发货 1运输中 2配送中 3已签收 4待评价 9已取消
    @Update("update order_business set good_status = 1 where order_item_id=#{orderitemid} and good_status=0")
    public int updateOrderBusinessToDeliveredByOrderItemId(int orderitemid);

    //从运输中修改为 配送中
    @Update("update order_business set good_status = 2 where order_item_id=#{orderitemid} and good_status=1")
    public int updateOrderBusinessInDeliveredByOrderItemId(int orderitemid);

    //顾客确认收货，从配送中转变为待评价，order_business表中商品条目也跟着变化
    @Update("update order_business set good_status=3 where good_status=2 and order_item_id=#{orderitemid} and customer_id=#{customerid}")
    public int confirmReceiptByOrderItemId(int orderitemid,int customerid);

    //重置密码功能 校验用户名和手机号是否存在
    @Select("select * from business where username=#{username} and phonenumber=#{phonenumber}")
    public Business findBusinessByUsernameAndPhonenumber(String username,String phonenumber);

    //重置密码功能 修改商家表内容
    @Update("update business set pwdhash=#{pwdhash} where username=#{username}")
    public int resetPasswordByUsername(String pwdhash,String username);

    //修改密码功能 查找用户哈希值
    @Select("select pwdhash from business where id=#{id}")
    public  String findPwdhashFromBusinessById(int id);

    //用户评价完成 那么订单中此商品状态修改为已完成
    //order_customer表和order_business表中主键编号是一致的
    @Update("update order_business set good_status=4 where order_business_id=#{ordercustomerid} and good_status=3")
    public int finishOrder(int ordercustomerid);

    //订单退款
    @Update("update order_business set good_status=9 where order_number=#{ordernumber}")
    public int refundOrderByOrderNumber(String ordernumber);
}
