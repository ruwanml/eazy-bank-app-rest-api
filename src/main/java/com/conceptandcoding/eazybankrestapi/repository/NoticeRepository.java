package com.conceptandcoding.eazybankrestapi.repository;

import com.conceptandcoding.eazybankrestapi.entity.Notice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Integer> {

    @Query(value = "FROM Notice n WHERE CURDATE() BETWEEN noticeStartDate AND noticeEndDate")
    List<Notice> findAllActiveNotice();
}
