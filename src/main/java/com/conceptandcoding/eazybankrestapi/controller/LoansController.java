package com.conceptandcoding.eazybankrestapi.controller;

import com.conceptandcoding.eazybankrestapi.entity.Account;
import com.conceptandcoding.eazybankrestapi.entity.Customer;
import com.conceptandcoding.eazybankrestapi.entity.Loan;
import com.conceptandcoding.eazybankrestapi.repository.CustomerRepository;
import com.conceptandcoding.eazybankrestapi.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class LoansController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/myLoans")
    public List<Loan> getLoanDetails(@RequestParam String email) {

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);

        if (optionalCustomer.isPresent()) {
            List<Loan> loans = loanRepository.findByCustomerIdOrderByStartDateDesc(optionalCustomer.get().getId());
            if (!loans.isEmpty()) {
                return loans;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
