package com.conceptandcoding.eazybankrestapi.controller;

import com.conceptandcoding.eazybankrestapi.entity.Notice;
import com.conceptandcoding.eazybankrestapi.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
//@CrossOrigin(origins = "http://localhost:4200") // CORS configurations - API level
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    @GetMapping("/notices")
    public ResponseEntity<List<Notice>> getNotices() {

        List<Notice> notices = noticeRepository.findAllActiveNotice();

        if (notices != null) {
            /*
            * In the response along with the notices information,
            * sending an cacheControl(attribute in Response Header) information with maxAge as 60 seconds.
            * This means, telling to browser for next 60s do not invoke the '/notices' API.
            * Can reuse whatever initial notices details have received.
            * Without making a server side request, client(browser) is going to leverage the data inside the cache.
            * This can be used to achieve some performance If you feel that some data is not going to change very frequently.
            * */

            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                    .body(notices);
        } else {
            return null;
        }
    }
}
