package com.conceptandcoding.eazybankrestapi.repository;

import com.conceptandcoding.eazybankrestapi.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByCustomerId(int customerId);
}
