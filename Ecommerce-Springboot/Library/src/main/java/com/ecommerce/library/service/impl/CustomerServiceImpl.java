package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerDto save(CustomerDto customerDto) {

        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customer.setRoles(Arrays.asList(repository.findByName("CUSTOMER")));

        Customer customerSave = customerRepository.save(customer);
        return mapperDTO(customerSave);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public Customer saveInfor(Customer customer) {
        Customer customer1 = customerRepository.findByUsername(customer.getUsername());
        customer1.setAddress(customer.getAddress());
        customer1.setCity(customer.getCity());
        customer1.setCountry(customer.getCountry());
        customer1.setPhoneNumber(customer.getPhoneNumber());
        return customerRepository.save(customer1);
    }

    @Override
    public List<CustomerDto>findAll(){
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDto> customerDtos = transfer(customers);
        return  customerDtos;
    }

    @Override
    public Page<CustomerDto> pageUsers(int pageNo){
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<CustomerDto> users = transfer(customerRepository.findAll());
        Page<CustomerDto> userPages = toPage(users, pageable);
        return userPages;

    }
    @Override
    public CustomerDto getUserById(Long id){

        return mapperDTO(customerRepository.getById(id));

    }

    @Override
    public Customer update(CustomerDto customerDto){

        Customer customer = customerRepository.getById(customerDto.getId());
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customer.setCarNumber(customerDto.getCarNumber());
        Customer customerSave = customerRepository.save(customer);
        return customerSave;
    }

    private Page toPage(List<CustomerDto> list , Pageable pageable){
        if(pageable.getOffset() >= list.size()){
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    private CustomerDto mapperDTO(Customer customer){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setPassword(customer.getPassword());
        customerDto.setUsername(customer.getUsername());
        customerDto.setCarNumber(customer.getCarNumber());
        customerDto.setId(customer.getId());
        return customerDto;
    }

    private List<CustomerDto> transfer(List<Customer> customers){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        List<CustomerDto> customerDtos = new ArrayList<>();
        for(Customer customer : customers){
           CustomerDto customerDto = new CustomerDto();
           customerDto.setId(customer.getId());
           customerDto.setFirstName(customer.getFirstName());
           customerDto.setLastName(customer.getLastName());
           customerDto.setUsername(customer.getUsername());
           customerDto.setPassword(customer.getPassword());
           customerDtos.add(customerDto);
        }
        return customerDtos;
    }
}
