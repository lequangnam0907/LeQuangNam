package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    CustomerDto save(CustomerDto customerDto);

    Customer findByUsername(String username);

    Customer saveInfor(Customer customer);

    List<CustomerDto> findAll();

    Page<CustomerDto> pageUsers(int pageNo);

    CustomerDto getUserById(Long id);

    Customer update(CustomerDto customerDto);


}
