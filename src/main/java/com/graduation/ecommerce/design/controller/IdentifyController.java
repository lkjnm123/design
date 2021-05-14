package com.graduation.ecommerce.design.controller;

import com.graduation.ecommerce.design.entity.Business;
import com.graduation.ecommerce.design.entity.Customer;
import com.graduation.ecommerce.design.service.BusinessService;
import com.graduation.ecommerce.design.service.CustomerService;
import com.graduation.ecommerce.design.utils.AESUtils;
import com.graduation.ecommerce.design.utils.PwdUtils;
import com.graduation.ecommerce.design.utils.RSAUtils;
import com.graduation.ecommerce.design.utils.TencentSmsUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class IdentifyController {
    //TODO 创建权限表 使用status限制商户和顾客登录
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private TencentSmsUtils tencentSmsUtils = new TencentSmsUtils();
    private final Base64.Encoder encoder = Base64.getEncoder(); //base64编码
    private final Base64.Decoder decoder = Base64.getDecoder(); //base64解码

    @ResponseBody
    @RequestMapping(value="/registerUsernameCheck",method={RequestMethod.POST},consumes = "application/json")
    public Map<String,String> registerUsernameCheck(@RequestBody Map<String,String> username){
        Map<String,String> map = new HashMap<>();
        String identity = username.get("identity");
        String uname = username.get("username");
        if(identity.equals("customer")){
            Customer customer = customerService.findUserByUsername(uname);
            if(customer!=null){
                //已经存在该用户
                map.put("result","no");
            }else{
                map.put("result","yes");
            }
        }else if(identity.equals("business")){
            Business business = businessService.findUserByUsername(uname);
            if(business!=null){
                map.put("result","no");
            }else{
                map.put("result","yes");
            }
        }
        return map;
    }
    @ResponseBody
    @RequestMapping(value="/getKey",method = RequestMethod.POST,consumes = "application/json")
    public Map<String,String> getKey(HttpSession httpSession) throws Exception {
        Map<String,String> map = new HashMap<>();
        String sessionID = httpSession.getId();
        //避免重复请求 判断redis中是否已经存在指定nonce
        if(stringRedisTemplate.hasKey(sessionID)){
            String nonce = stringRedisTemplate.opsForList().index(sessionID,0);
            String rsapub = stringRedisTemplate.opsForList().index(sessionID,1);
            map.put("nonce",nonce); //保存 nonce
            map.put("rsaPub",rsapub); //保存RSA公钥
        }else{
            String nonce = UUID.randomUUID().toString();
            KeyPair keyPair = RSAUtils.getKey(); //生成密钥对
            List<String> requestInfo = new ArrayList<>();
            requestInfo.add(nonce);
            requestInfo.add(encoder.encodeToString(keyPair.getPublic().getEncoded()));
            requestInfo.add(encoder.encodeToString(keyPair.getPrivate().getEncoded()));
            stringRedisTemplate.opsForList().rightPushAll(sessionID,requestInfo); //Redis 存储请求信息
            stringRedisTemplate.expire(sessionID,60*5,TimeUnit.SECONDS); //有效时间5分钟
            map.put("nonce",nonce);
            //下发的key是经过Base64编码的
            map.put("rsaPub",requestInfo.get(1));
            List<String> testlist = stringRedisTemplate.opsForList().range(sessionID,0,-1);
            //redis 索引从0开始，取出从0到最后一个元素
        }
        String timestamp = String.valueOf(new Date().getTime());
        map.put("timestamp",timestamp); //保存 timestamp
        return map;
    }

    @ResponseBody
    @RequestMapping(value="/getPhoneNumber",method = {RequestMethod.POST},consumes = "application/json")
    public Map<String,String> sendPhoneCode(@RequestBody Map<String,String> nonce, HttpSession httpSession){
        Map<String,String> map = new HashMap<>();
        String sessionId = httpSession.getId();
        if(stringRedisTemplate.hasKey(sessionId)){
            String submit_nonce = nonce.get("nonce"); //取得nonce
            String submit_phonenumber = nonce.get("phonenumber"); //取得手机号
            //检验nonce
            String real_nonce = stringRedisTemplate.opsForList().index(sessionId,0);
            if(submit_nonce.equals(real_nonce)){
                StringBuffer stringBuffer = new StringBuffer();
                String code = "";
                for(int i=0;i<6;i++){
                    stringBuffer.append((int)(Math.random()*9));
                    code = stringBuffer.toString();
                }
                try {
                    //发送验证码
                    tencentSmsUtils.registerSendSms(submit_phonenumber,code);
                } catch (TencentCloudSDKException e) {
                    e.printStackTrace();
                }
                stringRedisTemplate.opsForValue().set(submit_phonenumber,code,60, TimeUnit.SECONDS);
                map.put("msg","success");
            }else{
                map.put("msg","nonce 无效");
            }
        }
        return map;
    }
    @RequestMapping(value="/register",method = {RequestMethod.POST},consumes = "application/x-www-form-urlencoded")
    public String register(@RequestParam("identity") String identity, @RequestParam("encrypt_content") String encrypt_content, Map<String,Object> map, HttpSession httpSession) throws Exception {
        //商家和顾客的注册流程相同，仅存储用户信息到表不同
        String sessionID = httpSession.getId();
        String rsaPrivate = stringRedisTemplate.opsForList().index(sessionID,2);
        String decrypt_content = RSAUtils.decryptJSStr((RSAPrivateKey)RSAUtils.getPrivateKey(rsaPrivate),encrypt_content);
        String[] split = decrypt_content.split("\\|");
        for(int i=0;i<split.length;i++){
            split[i] = split[i].trim();
        }//去除多余空格
        String decrypt_username = split[0];
        String decrypt_password = split[1];
        String decrypt_phonenumber = split[2];
        String decrypt_timestamp = split[3];
        String decrypt_code = split[4];
        //提取信息解密后 根据不同身份到相应controller中完成注册
        //检测nonce
        String real_code = stringRedisTemplate.opsForValue().get(decrypt_phonenumber);
        if(real_code.equals(decrypt_code)){
            if(identity.equals("customer")){
                String salt = PwdUtils.getSalt();
                String hash = PwdUtils.getSHACode(decrypt_password,salt,"SHA-256");
                //String username,String pwdsalt,String pwdhash,String phonenumber
                int n = customerService.registerByPhoneNumber(decrypt_username,salt,hash,decrypt_phonenumber);
                if(n!=0){
                    map.put("msg","顾客注册成功");
                    return "/signin";
                }
            }else if(identity.equals("business")){
                String salt = PwdUtils.getSalt();
                String hash = PwdUtils.getSHACode(decrypt_password,salt,"SHA-256");
                //String username,String pwdsalt,String pwdhash,String phonenumber
                int n = businessService.registerByPhoneNumber(decrypt_username,salt,hash,decrypt_phonenumber);
                if(n!=0){
                    map.put("msg","商家注册成功");
                    return "/signin";
                }
            }else{
                map.put("msg","身份有误");
            }
        }else{
            map.put("msg","验证码错误");
            return "/register";
        }
        map.put("msg","注册失败");
        return "/signin";
    }

    @RequestMapping(value="/signinCheck",method = {RequestMethod.POST})
    public String signinCheck(@RequestParam String identity, @RequestParam String encrypt_key, @RequestParam String encrypt_iv, @RequestParam String content, Map<String,Object> map, HttpSession httpSession, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Customer customer = null;
        Business business = null;
        String sessionID = httpSession.getId();
        //注册和登录 共用getKey redis中存储sessionId : List(nonce,rsaPub,rsaPri)
        String rsaPrivate = stringRedisTemplate.opsForList().index(sessionID,2);
        String aes_key = RSAUtils.decryptJSStr((RSAPrivateKey)RSAUtils.getPrivateKey(rsaPrivate),encrypt_key);
        String aes_iv = RSAUtils.decryptJSStr((RSAPrivateKey)RSAUtils.getPrivateKey(rsaPrivate),encrypt_iv);
        String real_content = AESUtils.decryptStr(content,aes_key,aes_iv);
        String[] split = real_content.split("\\|");
        for(int i=0;i<split.length;i++){
            split[i] = split[i].trim();
        }//去除多余空格
        String decrypt_username = split[0];
        String decrypt_password = split[1];
        String decrypt_timestamp = split[2];
        String decrypt_remember = split[3];
        if(StringUtils.isEmpty(decrypt_username)||StringUtils.isEmpty(decrypt_password)){
            map.put("msg","用户名密码错误"); //登录错误信息显示
            return "/signin";
        }
        if(identity.equals("customer")){
            String salt = customerService.findSaltByUsername(decrypt_username);
            String pwdhash=PwdUtils.getSHACode(decrypt_password,salt,"SHA-256");
            customer = (Customer) customerService.login(decrypt_username,pwdhash);
        }else if(identity.equals("business")){
            String salt = businessService.findSaltByUsername(decrypt_username);
            String pwdhash=PwdUtils.getSHACode(decrypt_password,salt,"SHA-256");
            business = (Business) businessService.login(decrypt_username,pwdhash);
        }
        if(customer!=null){
            //TODO 登录成功设置状态使用随机值
            httpSession.setAttribute("loginUser",decrypt_username);
            //保存顾客id 方便后续操作
            httpSession.setAttribute("identity","customer");
            httpSession.setAttribute("customerId",String.valueOf(customer.getId()));
            if(!("false".equals(decrypt_remember))){ //记住我功能 设置cookie
                String str = "PersonalDeveloper";
                String temp = decrypt_username+str;
                String md = DigestUtils.md5DigestAsHex(temp.getBytes(StandardCharsets.UTF_8));
                String result = decrypt_username+":"+md; //md验证字符串
                System.out.println(result);
                Cookie cookie_username = new Cookie("cookie_username",result);
                cookie_username.setMaxAge(60*60);
                cookie_username.setPath(httpServletRequest.getContextPath());
                System.out.println("Setting Cookie");
                httpServletResponse.addCookie(cookie_username);
            }
            map.put("msg","顾客登录成功");
            return "/index";
        }else if(business!=null){
            httpSession.setAttribute("loginUser",decrypt_username);
            //保存商家id 方便后续操作
            httpSession.setAttribute("identity","business");
            httpSession.setAttribute("businessId",String.valueOf(business.getId()));
            if(!("false".equals(decrypt_remember))){ //记住我功能 设置cookie
                String str = "PersonalDeveloper";
                String temp = decrypt_username+str;
                String md = DigestUtils.md5DigestAsHex(temp.getBytes(StandardCharsets.UTF_8));
                String result = decrypt_username+":"+md; //md验证字符串
                System.out.println(result);
                Cookie cookie_username = new Cookie("cookie_username",result);
                cookie_username.setMaxAge(60*60);
                cookie_username.setPath(httpServletRequest.getContextPath());
                System.out.println("Setting Cookie");
                httpServletResponse.addCookie(cookie_username);
            }
            map.put("msg","商家登录成功");
            return "redirect:/shop/business";
        }
        map.put("msg","用户名或密码错误"); //登录错误信息显示
        return "/signin";
    }

    @RequestMapping(value="/logout",method = {RequestMethod.POST})
    public String logout(HttpSession httpSession, HttpServletResponse response, HttpServletRequest request){
        Map<String,String> map = new HashMap<>();
        httpSession.removeAttribute("loginUser");
        httpSession.removeAttribute("identity");
        httpSession.removeAttribute("customerId");
        httpSession.removeAttribute("businessId");
        Cookie cookie = new Cookie("cookie_username","");
        cookie.setMaxAge(0);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);
        map.put("msg","您已退出登录");
        return "/signin";
    }

    //找回密码 发送验证码功能
    @ResponseBody
    @RequestMapping(value="/sendCode",method = {RequestMethod.POST})
    public Map<String,String> sendCode(HttpSession httpSession,@RequestBody Map<String,String> content){
        Map<String,String> map = new HashMap<>();
        String sessionID = httpSession.getId();
        String username = content.get("username");
        String phonenumber = content.get("phonenumber");
        String submit_nonce = content.get("nonce");
        String submit_identity = content.get("identity");
        if(submit_identity.equals("customer")){
            Customer temp = customerService.findCustomerByUsernameAndPhonenumber(username,phonenumber);
            if(temp==null){
                map.put("msg","用户名或手机号错误");
                return map;
            }
        }else{
            Business temp = businessService.findBusinessByUsernameAndPhonenumber(username,phonenumber);
            if(temp==null){
                map.put("msg","用户名或手机号错误");
                return map;
            }
        }
        String real_nonce = stringRedisTemplate.opsForList().index(sessionID,0);
        if(submit_nonce.equals(real_nonce)){
            StringBuffer stringBuffer = new StringBuffer();
            String code = "";
            for(int i=0;i<6;i++){
                stringBuffer.append((int)(Math.random()*9));
                code = stringBuffer.toString();
            }
            try {
                tencentSmsUtils.registerSendSms(phonenumber,code);
                Jedis jedis = new Jedis("127.0.0.1",6379);
                jedis.select(2); //选择2号库 作为重置验证码存储手机号和验证码
                jedis.setex(phonenumber,300,code);  //默认60*15，15分钟
                jedis.close();
                map.put("msg","success");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            map.put("msg","nonce 无效");
        }
        return map;
    }
    @RequestMapping(value="/resetPassword",method = {RequestMethod.POST})
    public String resetPassword(HttpSession httpSession, Map<String,String> map, @RequestParam String identity, @RequestParam String username, @RequestParam String phonenumber, @RequestParam String code, RedirectAttributes redirectAttributes){
        System.out.println("重置密码功能接收到参数:"+identity+"="+username+"="+phonenumber+"="+code);
        Jedis jedis = new Jedis("127.0.0.1",6379);
        jedis.select(2);
        String real_code = jedis.get(phonenumber);
        if(real_code.equals(code)){
            //验证码有效
            String token = UUID.randomUUID().toString(); //生成令牌
            //需要传递给密码重置页面的参数有 identity username token
            jedis.select(3); //选择3号库 存储令牌
            jedis.setex(username,300,token); //有效时间5分钟
            jedis.close();
            redirectAttributes.addAttribute("identity",identity);
            redirectAttributes.addAttribute("username",username);
            redirectAttributes.addAttribute("token",token);
            return "redirect:/reset";
        }
        map.put("msg","短信验证码无效");
        return "/signin";
    }
    //处理密码重置页面请求
    //TODO 密码重置功能本应同样全程加密，为赶功能进度取消
    @RequestMapping(value="/submitreset",method = {RequestMethod.POST})
    public String reset(HttpSession httpSession,Map<String,String> map,@RequestParam String identity,@RequestParam String username,@RequestParam String password,@RequestParam String token){
        int n = 0;
        //检查令牌是否有效
        Jedis jedis = new Jedis("127.0.0.1",6379);
        jedis.select(3);
        String real_token = jedis.get(username);
        if(real_token.equals(token)){
            if(identity.equals("customer")){
                String pwdsalt = customerService.findSaltByUsername(username);
                //重新hash
                String hash = PwdUtils.getSHACode(password,pwdsalt,"SHA-256");
                n = customerService.resetPasswordByUsername(hash,username);
            }else{
                String pwdsalt = businessService.findSaltByUsername(username);
                //重新hash
                String hash = PwdUtils.getSHACode(password,pwdsalt,"SHA-256");
                n = businessService.resetPasswordByUsername(hash,username);
            }
            if(n!=0){
                map.put("msg","密码重置成功");
            }else{
                map.put("msg","密码重置失败");
            }
        }
        return "/signin";
    }
}
