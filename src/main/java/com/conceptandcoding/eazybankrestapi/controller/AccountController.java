package com.conceptandcoding.eazybankrestapi.controller;

import com.conceptandcoding.eazybankrestapi.entity.Account;
import com.conceptandcoding.eazybankrestapi.entity.Customer;
import com.conceptandcoding.eazybankrestapi.repository.AccountRepository;
import com.conceptandcoding.eazybankrestapi.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/myAccount")
    public Account getAccountDetails(@RequestParam String email) {

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);

        if (optionalCustomer.isPresent()) {
            Account account = accountRepository.findByCustomerId(optionalCustomer.get().getId());
            if (account != null) {
                return account;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
