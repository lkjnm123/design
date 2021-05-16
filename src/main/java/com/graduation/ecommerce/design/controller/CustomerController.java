package com.graduation.ecommerce.design.controller;

import com.alipay.easysdk.factory.Factory;
import com.graduation.ecommerce.design.entity.*;
import com.graduation.ecommerce.design.service.AlipayService;
import com.graduation.ecommerce.design.service.BusinessService;
import com.graduation.ecommerce.design.service.CustomerService;
import com.graduation.ecommerce.design.utils.OrderNumberUtils;
import com.graduation.ecommerce.design.utils.PwdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

@Controller
public class CustomerController {
    //TODO 接口防刷
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private AlipayService alipayService;
    /**
     * SpringMVC特性，方法返回值为void。默认解析路径就是请求的路径getPhoneNumber，导致Thymeleaf无法解析报错
     * */

    @RequestMapping(value="/searchgood",method = {RequestMethod.POST,RequestMethod.GET})
    public String searchGood(@RequestParam String keyname,Map<String,Object> map,Model model,HttpSession httpSession){
        String identity = (String)httpSession.getAttribute("identity");
        if(identity.equals("business")){
            map.put("msg","暂未支持商家角色搜索商品");
            return "/search";
        }
        Collection<Good> goodList = customerService.findGoodByKeyword(keyname);
        Collection<Good> goodResult = new ArrayList<>();
        Collection<GoodView> goodViews = new ArrayList<>();
        //筛选出符合条件的商品
        for(Good good:goodList){
            if(good.getStatus()==1&&good.getAdmin_status()==1&&good.getState()==1){
                goodResult.add(good);
            }
        }
        /** GoodView
         *     private int goodid;
         *     private String goodimage;
         *     private String goodname;
         *     private int shopid;
         *     private String shopname;
         */

        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        int customerid = Integer.parseInt(sessionCustomerId);
        Collection<CartItem> cartItemList = customerService.findCartItemListByCustomerId(customerid);
        //个性化展示查询是否已经在购物车中 对collection中的goodid数据进行提取
        List<Integer> extractData = new ArrayList<>();
        for(CartItem cartItem:cartItemList){
            extractData.add(cartItem.getGoodid());
        }
        for(Good good:goodResult){
            //为符合条件的商品查询店铺信息
            Shop shop = customerService.findShopByShopId(good.getId());
            //根据 customerid和goodid查询 cartitem表 ，并且customer_status=1 未在购物车中
            //cartitem 表cartitemid为主键 支持修改 customer_status，查询不需要使用集合接收

            // 搜索到的商品个性化展示 对频繁查库优化
            // 一次性查出所有状态为1即已经在购物车中的商品
            if(extractData.contains(good.getGoodid())){
                //已在购物车中
                if(shop.getAdmin_status()==1){ //店铺未违规关闭
                    GoodView goodView = new GoodView(good.getGoodid(),good.getImage(),good.getPrice(),good.getGoodname(),good.getGoodnumber(),shop.getId(),shop.getShopname(),0);
                    goodViews.add(goodView);
                }
            }else{
                //不在购物车中
                if(shop.getAdmin_status()==1){ //店铺未违规关闭
                    GoodView goodView = new GoodView(good.getGoodid(),good.getImage(),good.getPrice(),good.getGoodname(),good.getGoodnumber(),shop.getId(),shop.getShopname(),1);
                    goodViews.add(goodView);
                }
            }
        }
        //TODO 页面商品列表图片 懒加载
        if(goodResult.size()==0){
            map.put("msg","抱歉，没有找到与"+keyname+"相关的商品");
            return "/search";
        }
        model.addAttribute("goodViews",goodViews);
        return "/search";
    }

    @ResponseBody
    @RequestMapping(value="/client/customer/addaddress",method = {RequestMethod.POST},consumes = "application/json")
    public Map<String,String> addaddress(@RequestBody Map<String,String> content,HttpSession httpSession){
        Map<String,String> map = new HashMap<>();
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        int addressid = Integer.parseInt(content.get("addressid"));
        //TODO 转换异常处理
        int customerid = Integer.parseInt(sessionCustomerId);
        CustomerAddress customerAddress = new CustomerAddress(addressid,customerid,content.get("realname"),content.get("phonenumber"),content.get("address"));
        int n = customerService.addAddress(customerAddress);
        if(n!=0) {
            map.put("msg", "success");
        }else{
            map.put("msg","fail");
        }
        return map;
    }

    @RequestMapping(value="/client/customer/addresslist",method = {RequestMethod.GET})
    public String addresslist(Model model,HttpSession httpSession){
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        int id = Integer.parseInt(sessionCustomerId);
        Collection<CustomerAddress> addressList = customerService.addressList(id);
        model.addAttribute("addresses",addressList);
        return "/client/alert/alert_alladdress";
    }

    @ResponseBody
    @RequestMapping(value="/client/customer/addcart",method={RequestMethod.POST},consumes = "application/json")
    public Map<String,String> addCartItem(@RequestBody Map<String,String> content,HttpSession httpSession){
        Map<String,String> map = new HashMap<>();
        int cartitemid = Integer.parseInt(content.get("cartitemid"));
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        int customerid = Integer.parseInt(sessionCustomerId);
        int goodid = Integer.parseInt(content.get("goodid"));
        int number = 1;
        //添加到购物车 默认商品数量为一个
        int customer_status = 1;
        Date date = new Date(new java.util.Date().getTime());
        //shop_status admin_status business_status 是good表所固有的
        //但已展示的商品 肯定是已上架状态，即店铺开启，管理员准许，商家上架，状态 1 1 1
        CartItem cartItem = new CartItem(cartitemid,customerid,goodid,number,customer_status,1,1,1,date);
        int n = customerService.addCart(cartItem);
        if(n!=0){
            map.put("msg","success");
        }else{
            map.put("msg","fail");
        }
        return map;
    }

    @RequestMapping(value="/client/customer/cartlist",method = {RequestMethod.POST,RequestMethod.GET})
    public String cartList(Model model,HttpSession httpSession){
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        if(sessionCustomerId.equals("")||sessionCustomerId==null){
            //未登录状态
            System.out.println("获取不到session customer id");
        }
        int id = Integer.parseInt(sessionCustomerId);
        //获取购物车商品列表
        Collection<CartItem> cartItems = customerService.findCartItemListByCustomerId(id);
        //根据购物车中的商品信息查询 good表，获取商品详细信息
        //alert_cart.html 中需展示的字段
        /**
         * image
         * goodid goodname
         * shopid shopname
         * goodnumber 来自good表
         * goodprice
         * number 默认为1
         */
        //TODO cartitem购物车表中 数量字段未支持修改 ，默认为1
        Collection<GoodView> goodViews = new ArrayList<>();
        for(CartItem cartItem:cartItems){
            //TODO 频繁查库 优化解决办法
            Good good = customerService.findGoodByGoodId(cartItem.getGoodid());
            Shop shop = customerService.findShopByShopId(good.getId()); //根据good表中的shopid外键字段 查询shop表
            int status = 0; //默认无货状态
            if(shop.getAdmin_status()==0){
                status = 4;
            }else if(shop.getStatus()==0){
                status = 3;
            }else if(good.getState()==0) {
                status = 2;
            }else if(good.getGoodnumber()>0){
                status = 1;
            }else if(good.getGoodnumber()==0){
                status = 0;
            }
            //status 用于表示商品的状态 1 代表有货 0代表无货 2代表卖家自行下架 3代表卖家店铺关闭 4代表管理员关闭
            GoodView goodView = new GoodView(good.getGoodid(),good.getImage(),good.getPrice(),good.getGoodname(),good.getGoodnumber(),shop.getId(),shop.getShopname(),status);
            goodViews.add(goodView);
        }
        model.addAttribute("goodViews",goodViews);
        return "/client/alert/alert_cart";
        //ajax 请求通常使用于页面局部刷新，ajax请求后返回html页面 默认不会进行跳转
        //因此弃用ajax请求跳转，改用表单提交跳转
    }

    //a标签只能是GET请求
    @RequestMapping(value="/goodinfo",method = {RequestMethod.GET})
    public String goodInfo(@RequestParam("goodid") String goodid,Map<String,Object> map,HttpSession httpSession){
        String identity = (String)httpSession.getAttribute("identity");
        Good good = customerService.findGoodByGoodId(Integer.parseInt(goodid));
        Shop shop = customerService.findShopByShopId(good.getId());
        int shopid = shop.getId();
        String shopname = shop.getShopname();
        int gooditemid = good.getGoodid();
        String goodname = good.getGoodname();
        String image = good.getImage();
        BigDecimal price = good.getPrice();
        String introduce = good.getIntroduce();
        String weight = good.getWeight();
        int number = good.getGoodnumber();
        int status = 0; //默认无货
        //能搜到并点进 商品详情页面说明没有下架和关闭店铺
        if(number>0){
            status = 1;
        }
        if(!identity.equals("business")){
            String sessionCustomerId = (String)httpSession.getAttribute("customerId");
            int id = Integer.parseInt(sessionCustomerId);
            Collection<CustomerAddress> addresslist = customerService.addressList(id);
            map.put("addresslist",addresslist);
        }else{
            Collection<CustomerAddress> addresses = new ArrayList<>();
            map.put("addresslist",addresses);
        }
        map.put("goodid",gooditemid);
        map.put("goodname",goodname);
        map.put("image",image);
        map.put("price",price);
        map.put("introduce",introduce);
        map.put("weight",weight);
        map.put("status",status);
        map.put("shopid",shopid);
        map.put("shopname",shopname);

        List<GoodReviews> reviewsList = customerService.queryGoodCommentByGoodId(gooditemid);
        List<GoodInfoCommentShow> commentShows = new ArrayList<>();
        for(GoodReviews goodReviews:reviewsList){
            String customerName = customerService.findUserNameById(goodReviews.getCustomer_id());
            GoodInfoCommentShow goodInfoCommentShow = new GoodInfoCommentShow(customerName,goodReviews.getComment());
            commentShows.add(goodInfoCommentShow);
        }
        map.put("comments",commentShows);
        //传递
        return "/client/alert/alert_goodinfo";
    }

    @RequestMapping(value="/shopinfo",method = {RequestMethod.GET})
    public String shopInfo(@RequestParam("shopid") String shopid,Model model,Map<String,Object> map,HttpSession httpSession){
        int id = Integer.parseInt(shopid);
        Collection<Good> goodList = customerService.findAllGoodByShopId(id);
        Shop shop = customerService.findShopByShopId(id);
        //对结果集进行筛选
        Collection<Good> goodResult = new ArrayList<>();
        for(Good good:goodList){
            if(good.getStatus()==1&&good.getAdmin_status()==1&&good.getState()==1){
                goodResult.add(good);
            }
        }
        if(goodResult.size()==0){
            map.put("msg","该店铺居然是空的");
            return "/client/alert/alert_shopinfo";
        }
        model.addAttribute("goods",goodResult);
        model.addAttribute("shopname",shop.getShopname());
        return "/client/alert/alert_shopinfo";
    }

    @RequestMapping(value="/searchGoodByShopId",method = {RequestMethod.POST,RequestMethod.GET})
    public String searchGoodByShopId(@RequestParam String keyword,@RequestParam String shopid, Map<String,Object> map,Model model,HttpSession httpSession){
        Collection<Good> goodList = customerService.findGoodByKeyword(keyword);
        Collection<Good> goodResult = new ArrayList<>();
        Collection<GoodView> goodViews = new ArrayList<>();
        if(keyword.trim().equals("")||shopid==null){
            map.put("msg","There is something wrong.");
            return "/search";
        }
        for(Good good:goodList){
            if(good.getId()==Integer.parseInt(shopid)){
                //商品符合店铺id
                if(good.getStatus()==1&&good.getAdmin_status()==1&&good.getState()==1){
                    goodResult.add(good);
                }
            }
        }
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        int customerid = Integer.parseInt(sessionCustomerId);
        Collection<CartItem> cartItemList = customerService.findCartItemListByCustomerId(customerid);
        List<Integer> extractData = new ArrayList<>();
        for(CartItem cartItem:cartItemList){
            extractData.add(cartItem.getGoodid());
        }
        for(Good good:goodResult){
            Shop shop = customerService.findShopByShopId(good.getId());

            if(extractData.contains(good.getGoodid())){
                if(shop.getAdmin_status()==1){ //店铺未违规关闭
                    GoodView goodView = new GoodView(good.getGoodid(),good.getImage(),good.getPrice(),good.getGoodname(),good.getGoodnumber(),shop.getId(),shop.getShopname(),0);
                    goodViews.add(goodView);
                }
            }else{
                if(shop.getAdmin_status()==1){ //店铺未违规关闭
                    GoodView goodView = new GoodView(good.getGoodid(),good.getImage(),good.getPrice(),good.getGoodname(),good.getGoodnumber(),shop.getId(),shop.getShopname(),1);
                    goodViews.add(goodView);
                }
            }
        }
        //TODO 页面商品列表图片 懒加载
        if(goodResult.size()==0){
            map.put("msg","抱歉，没有找到与"+keyword+"相关的商品");
            return "/search";
        }
        model.addAttribute("goodViews",goodViews);
        return "/search";
    }

    @ResponseBody
    @RequestMapping(value="/client/customer/randomgood",method={RequestMethod.POST})
    public Map<Integer,Object> showRandomGood(){
        //java生成随机数 如果删除商品 跳过了某个ID 则会查不到商品 出现异常 弃用此方法
        Map<Integer,Object> map = new HashMap<>();
        //直接从mysql数据库中拿取5个随机数据
        //TODO 如果拿取了状态不是1 1 1，即不该展示的商品，则返回给前台的就不是5个商品
        Collection<Good> goodList = customerService.takeRandomGood();
        List<Good> goodResult = new ArrayList<>();
        for(Good good:goodList){
            if(good.getStatus()==1&&good.getAdmin_status()==1&&good.getState()==1){
                goodResult.add(good);
            }
        }
        //生成GoodView对象列表，方便前台展示
        List<GoodView> goodViews = new ArrayList<>();
        for(Good good:goodResult){
            //为符合条件的商品查询店铺信息
            Shop shop = customerService.findShopByShopId(good.getId());
            GoodView goodView = new GoodView(good.getGoodid(),good.getImage(),good.getPrice(),good.getGoodname(),good.getGoodnumber(),shop.getId(),shop.getShopname(),1);
            goodViews.add(goodView);
        }
        for(GoodView goodView:goodViews){
            map.put(goodView.getGoodid(),goodView);
        }
        return map;
    }

    //a标签 /{} 路径传参 弃用
    /*
    @ResponseBody
    @RequestMapping(value="/deleteCartItem/{goodid}",method = {RequestMethod.GET})
    public Map<String,String> deleteCartItem(@PathVariable("goodid") String goodid){
        //TODO session获取customerid
        int itemid = Integer.parseInt(goodid);
        String customerid = "1";
        int id = Integer.parseInt(customerid);
        Map<String,String> map = new HashMap<>();
        int n = customerService.deleteCartItem(id,itemid);
        if(n!=0){
            map.put("msg","success");
        }else{
            map.put("msg","fail");
        }
        return map;
    }
     */
    @ResponseBody
    @RequestMapping(value="/deleteCartItem",method = {RequestMethod.POST})
    public Map<String,String> deleteCartItem(@RequestBody Map<String,String> content,HttpSession httpSession){
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        //TODO 异常处理
        int itemid = Integer.parseInt(content.get("goodid"));
        int id = Integer.parseInt(sessionCustomerId);
        Map<String,String> map = new HashMap<>();
        int n = customerService.deleteCartItem(id,itemid);
        if(n!=0){
            map.put("msg","success");
        }else{
            map.put("msg","fail");
        }
        return map;
    }

    @RequestMapping(value="/goToSettlement",method={RequestMethod.POST})
    public String goToSettlement(@RequestParam Integer[] goodidlist,@RequestParam Integer[] numberlist, Model model,HttpSession httpSession){
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        Collection<GoodView> goodViews = new ArrayList<>();
        for(int i=0;i<goodidlist.length;i++){
            int goodid = goodidlist[i];
            Good good = customerService.findGoodByGoodId(goodid);
            if(numberlist[i]>good.getGoodnumber()){
                //TODO 提醒顾客不要一次性购买大数量商品(超过商品库存），否则会添加不成功
                //超出了商品剩余数量
                continue; //跳过该商品
            }
            Shop shop = customerService.findShopByShopId(good.getId());
            //private int status;  //此时status 作为选中的商品数量
            GoodView goodView = new GoodView(good.getGoodid(),good.getImage(),good.getPrice(),good.getGoodname(),good.getGoodnumber(),good.getId(),shop.getShopname(),numberlist[i]);
            System.out.println(goodView);
            goodViews.add(goodView);
        }

        model.addAttribute("goodViews",goodViews);
        int id = Integer.parseInt(sessionCustomerId);
        Collection<CustomerAddress> addresslist = customerService.addressList(id);
        model.addAttribute("addresses",addresslist);
        model.addAttribute("numbers",goodViews.size());
        //统计价格

        BigDecimal allprice = new BigDecimal(0);
        for(GoodView goodView:goodViews){
            BigDecimal itemprice = new BigDecimal(String.valueOf(goodView.getGoodprice()));
            BigDecimal itemtotal = itemprice.multiply(new BigDecimal(String.valueOf(goodView.getStatus())));
            allprice = allprice.add(itemtotal);
        }
        System.out.println("total price:"+allprice);
        model.addAttribute("payprice",allprice);
        return "/client/alert/alert_settlement";
    }

    @RequestMapping(value="/goPay",method = {RequestMethod.POST})
    public String payment(@RequestParam Integer[] goodidlist,@RequestParam Integer[] numberlist,@RequestParam String consignee,Model model,HttpSession httpSession){
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        Collection<GoodView> goodViews = new ArrayList<>();
        for(int i=0;i<goodidlist.length;i++){
            int goodid = goodidlist[i];
            Good good = customerService.findGoodByGoodId(goodid);
            Shop shop = customerService.findShopByShopId(good.getId());
            //private int status;  //此时status 作为选中的商品数量
            GoodView goodView = new GoodView(good.getGoodid(),good.getImage(),good.getPrice(),good.getGoodname(),good.getGoodnumber(),good.getId(),shop.getShopname(),numberlist[i]);
            System.out.println(goodView);
            goodViews.add(goodView);
        }
        //向MySQL中 initial_order 和 order_item表中添加内容
        //initial_order表仅用于是否支付
        //order_item表用于方便向商家表和顾客表中添加内容
        BigDecimal allprice = new BigDecimal(0);
        for(GoodView goodView:goodViews){
            BigDecimal itemprice = new BigDecimal(String.valueOf(goodView.getGoodprice()));
            BigDecimal itemtotal = itemprice.multiply(new BigDecimal(String.valueOf(goodView.getStatus())));
            allprice = allprice.add(itemtotal);
        }
        /**
         *     private int trade_status; 0 进行中，1 已完成，2 已取消
         *     private int pay_status; 0 未支付 ， 1 已支付
         */
        String ordernumber = OrderNumberUtils.generateOrderNumber();
        int id = Integer.parseInt(sessionCustomerId);
        //根据 consignee(addressid) 查询customeraddress表中的收货地址
        int addressid = Integer.parseInt(consignee);
        //TODO 转换异常处理
        CustomerAddress address = customerService.findAddressById(addressid);
        String address_context = ""+address.getRealname()+","+address.getPhonenumber()+","+address.getAddress();
        InitialOrder initialOrder = new InitialOrder(0,ordernumber,id,0,0,allprice,new BigDecimal(0),new Date(System.currentTimeMillis()),address_context);
        int n = customerService.insertOrder(initialOrder);
        //接收到的n 是影响数据库的行数
        int n1 = initialOrder.getOrder_id();
        /**
         *     private int order_id;
         *     private int goodid;
         *     private int goodnumber;
         *     private int item_status; 0 未支付无效状态 ， 1 支付有效状态， 2 已收货无效状态
         */
        for(GoodView goodView:goodViews){
            //TODO 处理添加到orderItem表中的商品超过库存数量或根本没有库存（特殊手段）
            /*
            if(goodView.getStatus()>customerService.queryGoodNumberByGoodId(goodView.getGoodid())){
                continue;
            }
             */
            int x = customerService.insertOrderItem(new OrderItem(0,n1,goodView.getGoodid(),goodView.getStatus(),0));
            //TODO 处理orderitem未能成功添加的情况
            if(x<=0){
                System.out.println("下单商品条目添加到OrderItem表中出现错误");
            }
        }
        //向Redis中添加订单支付时间限制
        Jedis jedis = new Jedis("127.0.0.1",6379);
        jedis.select(1); //选择1号库 作为订单超时处理
        jedis.setex(ordernumber,900,"0");  //默认60*15，15分钟

        //跳转到支付宝支付
        int total_number = 0;
        for(int i=0;i<numberlist.length;i++){
            total_number+=numberlist[i];
        }
        Good good = customerService.findGoodByGoodId(goodidlist[0]);
        String firstgoodname = good.getGoodname();
        StringBuffer sb = new StringBuffer(firstgoodname);
        sb.append("等");
        sb.append(total_number);
        sb.append("件商品");
        String subject = sb.toString();
        String result = alipayService.page(subject,ordernumber,allprice.toString());
        model.addAttribute("payPage",result);
        //返回HTML内容(form表单)到 alipay
        return "/client/alipay";
    }

    @ResponseBody
    @PostMapping("/client/refund")
    public Map<String,String> refund(HttpSession httpSession,@RequestBody Map<String,String> content) {
        //TODO 订单多商品 拆分退款 金额处理
        //目前仅支持订单同一退款，也就是一个订单号下所有商品退款
        Map<String,String> map = new HashMap<>();
        String ordernumber = content.get("ordernumber");
        //TODO 身份校验
        BigDecimal payamount = customerService.getPayAmountByOrderNumber(ordernumber);
        if(payamount==null){
            map.put("msg","查询已支付金额出现错误");
        }
        String result = alipayService.refund(ordernumber,payamount.toString());
        map.put("msg",result);
        if(result.equals("退款成功")){
            //修改顾客商品条目表和商家商品条目表，更新商品状态为已取消
            int n1 = customerService.refundOrderByOrderNumber(ordernumber);
            //修改订单总表 initial_order 中的支付状态 pay_status=2 已退款
            int n2 = customerService.refundIntialOrderByOrderNumber(ordernumber);
        }else{
            return map;
        }
        return map;
    }

    @GetMapping("/client/return")
    public String return_url(HttpServletRequest request,Model model){
        String trade_no = request.getParameter("out_trade_no");
        model.addAttribute("out_trade_no",trade_no);
        return "/client/return";
    }

    @PostMapping("/client/notify")
    public void notify_url(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                // System.out.println(name + " = " + request.getParameter(name));
            }

            // 支付宝验签
            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                //System.out.println("交易名称: " + params.get("subject"));
                //System.out.println("交易状态: " + params.get("trade_status"));
                //System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                //System.out.println("商户订单号: " + params.get("out_trade_no"));
                //更新initial_order表中的订单为已支付
                //TODO 支付成功 异步调用 修改支付状态和支付金额，激活order_item中的商品条目
                //修改支付金额方便退款使用
                int n = customerService.updatePayStatusAndPayAmount(new BigDecimal(params.get("buyer_pay_amount")),params.get("out_trade_no"));
                if(n!=0){
                    //更新支付状态成功
                    System.out.println(params.get("out_trade_no")+"订单支付成功");
                    //激活order_item表中的所有相关商品
                    int order_id = customerService.queryOrderIdByOrderNumber(params.get("out_trade_no")); //根据外部订单number查询自增主键id
                    int customer_id = customerService.queryCustomerIdFromInitialOrderByOrderNumber(params.get("out_trade_no")); //根据外部订单number查询所属顾客id
                    int item_number = customerService.updateOrderItemByOrderId(order_id);
                    if(item_number==0){
                        System.out.println("激活order_item表中的商品条目出现问题");
                    }
                    //从OrderItem表中查询待收货的商品条目 向 order_customer 和 order_business表中添加数据
                    List<OrderItem> orderItems = customerService.queryToBeReceivedGoodItem(order_id);
                    for(OrderItem orderItem:orderItems){
                        int shop_id = customerService.findShopIdByGoodId(orderItem.getGoodid());
                        String address_context = customerService.QueryAddressContextByOrderNumber(params.get("out_trade_no"));
                        OrderCustomer orderCustomer = new OrderCustomer(0,orderItem.getOrder_item_id(),customer_id,shop_id,orderItem.getGoodid(),orderItem.getGoodnumber(),address_context,params.get("out_trade_no"),0,"发货快递单号处理");
                        OrderBusiness orderBusiness = new OrderBusiness(0,orderItem.getOrder_item_id(),shop_id,customer_id,orderItem.getGoodid(),orderItem.getGoodnumber(),address_context,params.get("out_trade_no"),0,"发货快递单号处理");
                        int n1 = customerService.insertGoodItemToOrderCustomer(orderCustomer);
                        if(n1==0){
                            System.out.println("添加商品条目到顾客表出现错误");
                        }
                        int n2 = businessService.insertGoodItemToOrderBusiness(orderBusiness);
                        if(n2==0){
                            System.out.println("添加商品条目到商家表出现错误");
                        }

                    }

                }
                //System.out.println("交易金额: " + params.get("total_amount"));
                //System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                //System.out.println("买家付款时间: " + params.get("gmt_payment"));
                //System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
            }
        }
        PrintWriter writer = null;
        writer = response.getWriter();
        writer.write("success");
        writer.flush();
        writer.close();
    }

    @RequestMapping(value="/client/customer/tobereceived",method = {RequestMethod.GET})
    public String paidGoodShow(Model model,HttpSession httpSession){
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        int id = Integer.parseInt(sessionCustomerId);
         //order_customer good_status; 0待发货， 1 运输中，2 配送中，3 待评价 ，4 已完成
        Collection<OrderCustomer> orderCustomers = customerService.queryToBeReceivedGoodItemFromOrderCustomer(id);
        //THYMELEAF 不能同时迭代遍历两个对象集合 , 因此不再沿用GoodView类，单独创建GoodShow类
        Collection<GoodShow> goodShows = new ArrayList<>();
        for(OrderCustomer orderCustomer:orderCustomers){
            int goodid = orderCustomer.getGood_id();
            Good good = customerService.findGoodByGoodId(goodid);
            Shop shop = customerService.findShopByShopId(good.getId());
            int order_item_id = orderCustomer.getOrder_item_id();
            int order_id = customerService.findOrderIdByOrderItemId(order_item_id);
            Date itemDate = customerService.findDateByOrderId(order_id);
            // TODO 单独商品拆分显示付款价格功能
            GoodShow goodShow = new GoodShow(itemDate,orderCustomer.getOrder_number(),orderCustomer.getOrder_item_id(),orderCustomer.getOrder_customer_id(),goodid,good.getGoodname(),good.getImage(),orderCustomer.getGood_number(),orderCustomer.getAddress_context(),new BigDecimal("0.00"),shop.getId(),shop.getShopname(),orderCustomer.getGood_status());
            goodShows.add(goodShow);
        }
        model.addAttribute("goodShows",goodShows);
        return "/client/alert/alert_tobereceived";
    }

    @RequestMapping(value="/client/customer/nopayorder",method = {RequestMethod.GET})
    public String notPayOrderShow(Model model,HttpSession httpSession){
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        int id = Integer.parseInt(sessionCustomerId);
        Collection<InitialOrder> initialOrders = customerService.queryNotPayOrderByCustomerId(id);
        model.addAttribute("initialOrders",initialOrders);
        return "/client/alert/alert_notpayorder";
    }

    @ResponseBody
    @RequestMapping(value="/client/customer/cancelOrder",method = {RequestMethod.POST})
    public Map<String,String> cancelOrderManually(@RequestBody Map<String,String> content,HttpSession httpSession){
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        Map<String,String> map = new HashMap<>();
        int id = Integer.parseInt(sessionCustomerId);
        String ordernumber = content.get("ordernumber");
        int n = customerService.cancelOrderManually(id,ordernumber);
        if(n!=0){
            map.put("msg","success");
        }else{
            map.put("msg","fail");
        }
        return map;
    }

    @RequestMapping(value="/client/customer/resumeOrder",method = {RequestMethod.POST})
    public String resumeOrder(@RequestParam String ordernumber,@RequestParam String price, Model model,HttpSession httpSession){
        String subject = "恢复订单";
        System.out.println("恢复订单号:"+ordernumber);
        System.out.println("恢复订单价格:"+price);
        String result = alipayService.page(subject,ordernumber,price);
        model.addAttribute("payPage",result);
        //返回HTML内容(form表单)到 alipay
        return "/client/alipay";
    }

    @RequestMapping(value="/client/customer/orderHistory",method={RequestMethod.POST})
    public String orderHistory(Model model,HttpSession httpSession){
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        int id = Integer.parseInt(sessionCustomerId);
        Collection<InitialOrder> initialOrders = customerService.queryAllOrderByCustomerId(id);
        model.addAttribute("orderHistory",initialOrders);
        /**
         *     private int trade_status; 0 进行中，1 已完成，2 已取消
         *     private int pay_status; 0 未支付 ， 1 已支付, 2 已退款
         */
        return "/client/alert/alert_orderhistory";
    }

    @RequestMapping(value="/client/customer/orderInfo",method = {RequestMethod.GET})
    public String orderInfo(Model model,@RequestParam String ordernumber){
        //使用OrderShow对象封装
        InitialOrder initialOrder = customerService.findInitialOrderByOrderNumber(ordernumber);
        int initialOrderId = initialOrder.getOrder_id();
        //查询附属于该订单的所有商品条目
        List<OrderItem> orderItemList = customerService.queryOrderItemListByOrderId(initialOrderId);
        Collection<OrderShow> orderShows = new ArrayList<>();
        for(OrderItem orderItem:orderItemList){
            Good good = customerService.findGoodByGoodId(orderItem.getGoodid());
            OrderCustomer orderCustomer = customerService.getOrderCusotmerByOrderItemId(orderItem.getOrder_item_id());
            if(orderCustomer==null){
                //未付款 不会向order_business 和 order_customer中添加内容，查询出的orderCustomer结果为空
                int shopid = customerService.findShopIdByGoodId(good.getGoodid());
                Shop shop = customerService.findShopByShopId(shopid);
                OrderShow orderShow = new OrderShow(ordernumber,initialOrder.getCreate_time().toString(),good.getGoodid(),good.getGoodname(),good.getImage(),orderItem.getGoodnumber(),9,shop.getId(),shop.getShopname(),initialOrder.getAddress_context());
                orderShows.add(orderShow);
            }else{
                Shop shop = customerService.findShopByShopId(orderCustomer.getShop_id());
                OrderShow orderShow = new OrderShow(ordernumber,initialOrder.getCreate_time().toString(),good.getGoodid(),good.getGoodname(),good.getImage(),orderItem.getGoodnumber(),orderCustomer.getGood_status(),shop.getId(),shop.getShopname(),orderCustomer.getAddress_context());
                orderShows.add(orderShow);
            }
        }
        model.addAttribute("orderShows",orderShows);
        return "/client/alert/alert_orderinfo";
    }

    @ResponseBody
    @RequestMapping(value="/client/customer/confirmReceipt",method = {RequestMethod.POST})
    public Map<String,String> confirmReceipt(@RequestBody Map<String,String> content,HttpSession httpSession){
        String sessionCustomerId = (String)httpSession.getAttribute("customerId");
        Map<String,String> map = new HashMap<>();
        int customer_id = Integer.parseInt(sessionCustomerId);
        String orderitemid = content.get("orderitemid");
        int order_item_id = Integer.parseInt(orderitemid);
        int n = customerService.confirmBandCReceiptByOrderItemId(order_item_id,customer_id);
        if(n!=0){
            map.put("msg","success");
        }else{
            map.put("msg","fail");
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value="/client/customer/editPassword",method = {RequestMethod.POST})
    public Map<String,String> editPassword(@RequestBody Map<String,String> content,HttpSession httpSession){
        String sessionId = (String)httpSession.getAttribute("customerId");
        String username = (String)httpSession.getAttribute("loginUser");
        Map<String,String> map = new HashMap<>();
        int customer_id = Integer.parseInt(sessionId);
        String oldpwd = content.get("oldpwd");
        String newpwd = content.get("newpwd");
        //获取salt值，与提交的旧密码进行哈希
        String salt = customerService.findSaltByUsername(username);
        String oldhash = customerService.findPwdhashFromCustomerById(customer_id);
        String submit_hash = PwdUtils.getSHACode(oldpwd,salt,"SHA-256");
        if(oldhash.equals(submit_hash)){
            //旧密码校验成功
            String newhash = PwdUtils.getSHACode(newpwd,salt,"SHA-256");
            int n = customerService.resetPasswordByUsername(newhash,username);
            if(n!=0){
                map.put("msg","success");
            }else{
                map.put("msg","fail");
            }
        }else{
            map.put("msg","fail");
        }
        return map;
    }
    //向顾客展示待评价的商品列表
    @RequestMapping(value="/client/customer/goodreviewlist",method = {RequestMethod.POST})
    public String goodReviewList(HttpSession httpSession,Model model){
        String sessionId = (String)httpSession.getAttribute("customerId");
        String username = (String)httpSession.getAttribute("loginUser");
        List<OrderCustomer> list = customerService.queryToBeEvaluateGoodByCustomerId(Integer.parseInt(sessionId));
        /**
         * private Date date;
         *     private String ordernumber;
         *     private String goodname;
         *     private String shopname;
         *     private int buynumber;
         */
        List<ToBeReviews> toBeReviewsList = new ArrayList<>();
        for(OrderCustomer orderCustomer:list){
            Date create_time = customerService.findCreateOrderTimeByOrderNumber(orderCustomer.getOrder_number());
            Good good = customerService.findGoodByGoodId(orderCustomer.getGood_id());
            Shop shop = customerService.findShopByShopId(orderCustomer.getShop_id());
            ToBeReviews toBeReviews = new ToBeReviews(create_time,orderCustomer.getOrder_number(),good.getGoodname(),shop.getShopname(),orderCustomer.getGood_number(),orderCustomer.getOrder_customer_id());
            toBeReviewsList.add(toBeReviews);
        }
        model.addAttribute("ToBeReviewGoods",toBeReviewsList);
        return "/client/alert/alert_tobereview";
    }
    //处理用户提交的商品评价
    @ResponseBody
    @RequestMapping(value="/client/customer/submitcomment",method = {RequestMethod.POST})
    public Map<String,String> addGoodComment(HttpSession httpSession,@RequestBody Map<String,String> content){
        Map<String,String> map = new HashMap<>();
        String sessionId = (String)httpSession.getAttribute("customerId");
        String username = (String)httpSession.getAttribute("loginUser");
        String ordercustomerid = content.get("ordercustomerid");
        int goodid = customerService.findGoodIdByOrderCustomerId(Integer.parseInt(ordercustomerid));
        String comment = content.get("comment");
        //添加商品评论的同时需要 修改order_customer和order_business表的商品状态
        GoodReviews goodReviews = new GoodReviews(0,Integer.parseInt(ordercustomerid),Integer.parseInt(sessionId),comment,goodid);
        int n1 = customerService.addGoodComment(goodReviews);
        if(n1!=0){
            int n2 = customerService.finishOrder(Integer.parseInt(ordercustomerid));
            if(n2!=0){
                map.put("msg","success");
                return map;
            }else{
                map.put("msg","fail2");
                return map;
            }
        }else{
            map.put("msg","fail1");
        }
        return map;
    }
}
