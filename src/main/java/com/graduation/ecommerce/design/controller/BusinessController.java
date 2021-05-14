package com.graduation.ecommerce.design.controller;

import com.graduation.ecommerce.design.entity.DeliverGood;
import com.graduation.ecommerce.design.entity.Good;
import com.graduation.ecommerce.design.entity.OrderBusiness;
import com.graduation.ecommerce.design.entity.Shop;
import com.graduation.ecommerce.design.service.BusinessService;
import com.graduation.ecommerce.design.utils.GoodStateUtils;
import com.graduation.ecommerce.design.utils.ImageProcessUtils;
import com.graduation.ecommerce.design.utils.PwdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class BusinessController {
    @Autowired
    private BusinessService businessService;
    @ResponseBody
    @RequestMapping(value="/shop/business/addShop",method = {RequestMethod.POST},consumes = "application/json")
    public Map<String,String> addShop(@RequestBody Map<String,String> content,HttpSession httpSession){
        Map<String,String> map = new HashMap<>();
        String sessionBusinessId = (String)httpSession.getAttribute("businessId");
        int businessid = Integer.parseInt(sessionBusinessId);
        //TODO 转换异常处理
        /*
        Collection<Shop> shops = businessService.shoplist(content.get("businessid"));
        if(shops.size()!=0){
            map.put("msg","不允许创建多个店铺");
            //TODO 创建店铺条目不显示
        }
        */
        //Shop shop = new Shop(businessid,content.get("shopname"),content.get("shopintroduce"));
        /**
         * 使用MyBatis 有则修改 无则插入出现问题
         * shop表使用 id(businessid)和status共同作为主键， 修改主键由于别的表已经外键关联，无法删除修改
         * MyBatis(on duplicate key update) 使用唯一索引
         * 为shop表创建唯一索引 alter table shop add unique(id);
         */
        int status = Integer.parseInt(content.get("shopstatus"));
        Shop shop = new Shop(businessid,content.get("shopname"),content.get("shopintroduce"),status,1);
        //默认一个business只能创建一个shop，shop主键 是businessid 和 stauts字段，id(businessid)添加唯一索引
        int n = businessService.addShop(shop);
        if(n!=0) {
            map.put("msg", "success");
        }else{
            map.put("msg","fail");
        }
        return map;
    }

    @RequestMapping(value="/shop/business/showallshop",method = {RequestMethod.GET})
    public String shoplist(Model model, HttpSession httpSession){
        String sessionBusinessId = (String)httpSession.getAttribute("businessId");
        Collection<Shop> shops = businessService.shoplist(sessionBusinessId);
        model.addAttribute("shops",shops);
        return "/shop/alert/alert_allshop";
    }

    @ResponseBody
    @RequestMapping(value="/shop/business/uploadimage",method = {RequestMethod.POST})
    public Map<String,String> uploadImage(@RequestParam(value="file",required = false)MultipartFile file, HttpServletRequest httpServletRequest){
        //TODO 上传图片行为限制 如频繁上传
        Map<String,String> map = new HashMap<>();
        String filename = file.getOriginalFilename();
        String postfix = filename != null ? filename.substring(filename.lastIndexOf(".")) : null;
        if(postfix==null){
            map.put("msg","文件名不符合规范");
            return map;
        }
        if(!".jpg".equals(postfix)&&!".jpeg".equals(postfix)&&!".gif".equals(postfix)&&!".png".equals(postfix)){
            map.put("msg","文件格式不符合");
            return map;
        }
        if(file.getSize()>1048576){
            map.put("msg","图片过大");
            return map;
        }
        String picId = UUID.randomUUID().toString();
        String objectName = picId+postfix;
        String imgVisitUrl = null;
        imgVisitUrl = ImageProcessUtils.imageupload(objectName,file);
        //TODO 异步处理
        ImageProcessUtils.imageProcess(objectName);
        if(!StringUtils.isEmpty(imgVisitUrl)){
            map.put("msg","上传成功");
            map.put("imageurl",imgVisitUrl);
        }else{
            map.put("msg","上传失败");
        }
        /*
        //保存到本地
        String picId = UUID.randomUUID().toString();
        String targetname = picId+postfix;
        try {
            byte[] bytes = file.getBytes();
            File pfile = new File("D:\\design\\goodimages\\");
            if(!pfile.exists()){
                pfile.mkdirs();
            }
            File target = new File(pfile,targetname);
            OutputStream out = new FileOutputStream(target);
            out.write(bytes);
            map.put("msg","上传成功");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            map.put("msg","上传失败");
        }
         */
        return map;
    }

    @ResponseBody
    @RequestMapping(value="/shop/business/addGood",method = {RequestMethod.POST},consumes = "application/json")
    public Map<String,String> addGood(@RequestBody Map<String,String> content,HttpSession httpSession){
        String sessionBusinessId = (String)httpSession.getAttribute("businessId");
        Map<String,String> map = new HashMap<>();
        Shop check_shop = businessService.findShopById(Integer.parseInt(sessionBusinessId));
        if(check_shop==null){
            //未创建店铺
            map.put("msg","请先创建店铺");
            return map;
        }
        Collection<Shop> shops = businessService.shoplist(sessionBusinessId);
        for(Shop shop:shops){
            if(shop.getStatus()==0){
                map.put("msg","请先开启店铺");
                return map;
            }
            if(shop.getAdmin_status()==0){
                map.put("msg","店铺违规");
                return map;
            }
        }
        int businessid = Integer.parseInt(sessionBusinessId);
        int goodid = Integer.parseInt(content.get("goodid"));
        //TODO goodid防止伪造
        BigDecimal price = new BigDecimal(content.get("price"));
        int goodnumber = Integer.parseInt(content.get("goodnumber"));
        int goodstate = Integer.parseInt(content.get("state"));
        if(goodid!=0){
            System.out.println("editGood");
        }
        Good good = new Good(businessid,goodid,content.get("goodname"),content.get("brand"),content.get("introduce"),price,goodnumber,content.get("weight"),content.get("address"),content.get("image"),1,1,goodstate);
        int n = businessService.addGood(good);
        if(n!=0){
            map.put("msg","编辑商品成功");
        }else{
            map.put("msg","编辑商品失败");
        }
        //TODO 返回至商品列表页面
        return map;
    }

    @RequestMapping(value="/shop/business/showallgood",method = {RequestMethod.GET})
    public String goodlist(Model model,HttpSession httpSession){
        String sessionBusinessId = (String)httpSession.getAttribute("businessId");
        //创建map表 便于更新商品状态表
        //获取状态 status admin_status state
        //查询是否存在对应店铺
        Shop shop = businessService.findShopById(Integer.parseInt(sessionBusinessId));
        if(shop==null){
            //未创建店铺
            model.addAttribute("tip","请先创建店铺");
            return "/shop/alert/alert_allgood";
        }
        Collection<Good> goods = businessService.goodlist(sessionBusinessId);
        Iterator<Good> iterator = goods.iterator();
        int businessid = Integer.parseInt(sessionBusinessId);
        int shopState = businessService.shopState(businessid);
        if(shopState==0){
            //TODO 店铺违规关闭
            return "/shop/business";
        }
        while(iterator.hasNext()){
            Good good = iterator.next();
            int status = good.getStatus();
            int status_admin = good.getAdmin_status();
            int state = good.getState();
            int goodstate = GoodStateUtils.finalState(status,status_admin,state);
            good.setState(goodstate);
        }
        model.addAttribute("goods",goods);
        return "/shop/alert/alert_allgood";
    }

    @RequestMapping(value = "/shop/business/deleteGood/{goodid}",method = {RequestMethod.POST})
    public String deleteNote(@PathVariable("goodid") Integer goodid){
        int n = businessService.deleteGood(goodid);
        //TODO ajax请求删除 局部刷新
        return "redirect:/shop/business";
    }

    @RequestMapping(value="/shop/business/tobedeliver",method = {RequestMethod.POST})
    public String toDeliverGoodShow(Model model,HttpSession httpSession){
        String sessionBusinessId = (String)httpSession.getAttribute("businessId");
        int id = Integer.parseInt(sessionBusinessId);
        Collection<OrderBusiness> orderBusinesses = businessService.queryToBeDeliveredGoodItemFromOrderBusiness(id);
        Collection<DeliverGood> deliverGoods = new ArrayList<>();
        for(OrderBusiness orderBusiness:orderBusinesses){
            Date date = businessService.queryOrderCreateTimeByOrderNumber(orderBusiness.getOrder_number());
            //SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
            //Timestamp timestamp = new Timestamp(date.getTime());
            //String str = sdf.format(timestamp);
            //TODO 数据库存储类型为 sql.Date，时分秒精度丢失
            String str = date.toString();
            Good good = businessService.findGoodByGoodId(orderBusiness.getGood_id());
            String customername = businessService.findCustomerNameByCustomerId(orderBusiness.getCustomer_id());
            DeliverGood deliverGood = new DeliverGood(orderBusiness.getOrder_item_id(),str,orderBusiness.getOrder_number(),orderBusiness.getGood_id(),good.getGoodname(),orderBusiness.getGood_number(),customername,orderBusiness.getAddress_context(),orderBusiness.getGood_status());
            deliverGoods.add(deliverGood);
        }
        model.addAttribute("deliverGoods",deliverGoods);
        return "/shop/alert/alert_delivergood";
    }

    @ResponseBody
    @RequestMapping(value="/shop/business/changeOrderItemStatus",method = {RequestMethod.POST})
    public Map<String,String> changeOrderItemStatus(@RequestBody Map<String,String> content,HttpSession httpSession){
        Map<String,String> map = new HashMap<>();
        String val = content.get("val");
        int order_item_id = Integer.parseInt(content.get("orderitemid"));
        int n = 0;
        if(val.equals("1")){
            //从待发货状态修改为运输中状态
             n = businessService.updateOrderBandCToDeliveredByOrderItemId(order_item_id);
        }else if(val.equals("2")){
            //从运输状态修改为配送中状态
             n = businessService.updateOrderBandCInDeliveredByOrderItemId(order_item_id);
        }else if(val.equals("9")){
            //订单取消
        }
        if(n!=0){
            map.put("msg","success");
        }else{
            map.put("msg","fail");
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value="/client/business/editPassword",method = {RequestMethod.POST})
    public Map<String,String> editPassword(@RequestBody Map<String,String> content,HttpSession httpSession){
        String sessionId = (String)httpSession.getAttribute("businessId");
        String username = (String)httpSession.getAttribute("loginUser");
        Map<String,String> map = new HashMap<>();
        int customer_id = Integer.parseInt(sessionId);
        String oldpwd = content.get("oldpwd");
        String newpwd = content.get("newpwd");
        //获取salt值，与提交的旧密码进行哈希
        String salt = businessService.findSaltByUsername(username);
        String oldhash = businessService.findPwdhashFromBusinessById(customer_id);
        String submit_hash = PwdUtils.getSHACode(oldpwd,salt,"SHA-256");
        if(oldhash.equals(submit_hash)){
            //旧密码校验成功zong
            String newhash = PwdUtils.getSHACode(newpwd,salt,"SHA-256");
            int n = businessService.resetPasswordByUsername(newhash,username);
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
}
