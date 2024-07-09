package com.conceptandcoding.eazybankrestapi.repository;

import com.conceptandcoding.eazybankrestapi.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository  extends CrudRepository<Customer, Integer> {

    List<Customer> findByEmail(String email); // derived method name query
}
