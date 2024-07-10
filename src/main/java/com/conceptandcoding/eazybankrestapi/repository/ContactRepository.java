package com.conceptandcoding.eazybankrestapi.repository;

import com.conceptandcoding.eazybankrestapi.entity.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {
}
