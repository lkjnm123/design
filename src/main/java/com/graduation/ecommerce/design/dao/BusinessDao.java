package com.graduation.ecommerce.design.dao;

import com.graduation.ecommerce.design.entity.Business;
import com.graduation.ecommerce.design.entity.Customer;
import com.graduation.ecommerce.design.mapper.BusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BusinessDao {
    @Autowired
    private BusinessMapper businessMapper;

    public int registerByPhoneNumber(String username,String pwdsalt,String pwdhash,String phonenumber){
        return businessMapper.registerByPhone(new Business(0,username,pwdsalt,pwdhash,phonenumber,""));
    }

    public int registerByMailAddress(String username,String pwdsalt,String pwdhash,String mailaddress){
        return businessMapper.registerByMail(new Business(0,username,pwdsalt,pwdhash,"",mailaddress));
    }

    public Business findBusinessByUsername(String username){
        return businessMapper.findUserByUsername(username);
    }

    public String findSaltByUsername(String username){
        return businessMapper.findSaltByUsername(username);
    }

    public Business login(String username,String pwdhash){
        return businessMapper.login(username,pwdhash);
    }

    public Business findBusinessByUsernameAndPhonenumber(String username,String phonenumber){
        return businessMapper.findBusinessByUsernameAndPhonenumber(username,phonenumber);
    }

    public int resetPasswordByUsername(String pwdhash,String username){
        return businessMapper.resetPasswordByUsername(pwdhash,username);
    }

    public String findPwdhashFromBusinessById(int id){
        return businessMapper.findPwdhashFromBusinessById(id);
    }
}
