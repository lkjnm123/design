package com.graduation.ecommerce.design.dao;

import com.graduation.ecommerce.design.entity.CustomerAddress;
import com.graduation.ecommerce.design.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class AddressDao {
    @Autowired
    private CustomerMapper customerMapper;

    public int addAddress(CustomerAddress customerAddress){
        return customerMapper.addAddress(customerAddress);
    }

    public List<CustomerAddress> addressList(int customerid){
        return customerMapper.addresslist(customerid);
    }
    public CustomerAddress findAddressById(int addressid){
        return customerMapper.findAddressById(addressid);
    }
}
