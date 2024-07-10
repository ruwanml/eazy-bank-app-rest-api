package com.conceptandcoding.eazybankrestapi.repository;

import com.conceptandcoding.eazybankrestapi.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String> {

    List<Transaction> findByCustomerIdOrderByTransactionDateDesc(int customerId);
}
