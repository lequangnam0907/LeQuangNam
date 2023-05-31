package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller

public class UserController {

    private final CustomerService customerService;

    public UserController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping("/users")
    public String getListUser(Model model , Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        List<CustomerDto>  users = this.customerService.findAll();
        model.addAttribute("title","Manage User");
        model.addAttribute("users",users);
        model.addAttribute("size",users.size());
        model.addAttribute("categoryNew", new Customer());
        return  "users";
    }
    @GetMapping("/users/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        Page<CustomerDto> users = customerService.pageUsers(pageNo);
        model.addAttribute("title", "Manage User");
        model.addAttribute("size", users.getSize());
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/update-user/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model, Principal principal){

        if(principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title", "Update user");
        CustomerDto customerDto = customerService.getUserById(id);
        model.addAttribute("userDto", customerDto);
        return "update-user";
    }

    @PostMapping("/update-user/{id}")
    public String processUpdate(@PathVariable("id") Long id,
                                @ModelAttribute("userDto") CustomerDto customerDto,
                                RedirectAttributes attributes
    ){
        try {
            customerService.update(customerDto);
            attributes.addFlashAttribute("success", "Update successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update!");
        }
        return "redirect:/users/0";

    }

}
