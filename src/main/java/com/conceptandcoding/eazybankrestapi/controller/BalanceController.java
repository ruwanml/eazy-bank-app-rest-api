package com.conceptandcoding.eazybankrestapi.controller;

import com.conceptandcoding.eazybankrestapi.entity.Transaction;
import com.conceptandcoding.eazybankrestapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BalanceController {

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/myBalance")
    public List<Transaction> getBalanceDetails(@RequestParam int id) {

        List<Transaction> accountTransactions = transactionRepository
                .findByCustomerIdOrderByTransactionDateDesc(id);

        if (accountTransactions != null) {
            return accountTransactions;
        } else {
            return null;
        }
    }
}
