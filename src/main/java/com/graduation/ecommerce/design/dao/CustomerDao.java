package com.graduation.ecommerce.design.dao;

import com.graduation.ecommerce.design.entity.Customer;
import com.graduation.ecommerce.design.entity.CustomerAddress;
import com.graduation.ecommerce.design.entity.GoodReviews;
import com.graduation.ecommerce.design.entity.OrderCustomer;
import com.graduation.ecommerce.design.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDao {
    @Autowired
    private CustomerMapper customerMapper;

    public int registerByPhoneNumber(String username,String pwdsalt,String pwdhash,String phonenumber){
        return customerMapper.registerByPhone(new Customer(0,username,pwdsalt,pwdhash,phonenumber,""));
    }

    public int registerByMailAddress(String username,String pwdsalt,String pwdhash,String mailaddress){
        return customerMapper.registerByMail(new Customer(0,username,pwdsalt,pwdhash,"",mailaddress));
    }

    public Customer findConsumerByUsername(String username){
        return customerMapper.findUserByUsername(username);
    }

    public String findSaltByUsername(String username){
        return customerMapper.findSaltByUsername(username);
    }

    public Customer login(String username,String pwdhash){
        return customerMapper.login(username,pwdhash);
    }

    public Customer findCustomerByUsernameAndPhonenumber(String username,String phonenumber){
        return customerMapper.findCustomerByUsernameAndPhonenumber(username,phonenumber);
    }
    public int resetPasswordByUsername(String pwdhash,String username){
        return customerMapper.resetPasswordByUsername(pwdhash,username);
    }
    public String findPwdhashFromCustomerById(int id){
        return customerMapper.findPwdhashFromCustomerById(id);
    }
    public int addGoodComment(GoodReviews goodReviews){
        return customerMapper.addGoodComment(goodReviews);
    }
    public List<OrderCustomer> queryToBeEvaluateGoodByCustomerId(int customerid){
        return customerMapper.queryToBeEvaluateGoodByCustomerId(customerid);
    }

    public String findUserNameById(int id){
        return customerMapper.findUserNameById(id);
    }
}
