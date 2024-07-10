package com.conceptandcoding.eazybankrestapi.repository;

import com.conceptandcoding.eazybankrestapi.entity.Loan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Integer> {

    List<Loan> findByCustomerIdOrderByStartDateDesc(int customerId);
}
