package com.graduation.ecommerce.design.component;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object object = request.getSession().getAttribute("loginUser");
        if(object!=null){
            return true;
        }
        String cookie_username = null;
        if(request.getCookies()!=null){
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie:cookies){
                if("cookie_username".equals(cookie.getName())){
                    cookie_username = cookie.getValue();
                    break;
                }
            }
        }
        if(cookie_username!=null){
            String[] str = cookie_username.split(":");
            String rightstr = str[0]+"personalNote";
            String rightmd = DigestUtils.md5DigestAsHex(rightstr.getBytes(StandardCharsets.UTF_8));
            if(!rightmd.equals(str[1])){
                request.setAttribute("msg","Are you bully me?");
                request.getRequestDispatcher("/signin").forward(request,response);
                return false;
            }else{
                request.getSession().setAttribute("loginUser",cookie_username);
                return true;
            }
        }else{
            //不存在指定cookie 且session为空
            request.setAttribute("msg","没有权限，请先登录");
            request.getRequestDispatcher("/signin").forward(request,response);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
