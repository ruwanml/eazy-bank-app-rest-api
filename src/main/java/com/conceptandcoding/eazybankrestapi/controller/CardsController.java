package com.conceptandcoding.eazybankrestapi.controller;

import com.conceptandcoding.eazybankrestapi.entity.Card;
import com.conceptandcoding.eazybankrestapi.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardsController {

    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/myCards")
    public List<Card> getCardDetails(@RequestParam int id) {

        List<Card> cards = cardRepository.findByCustomerId(id);

        if (cards != null) {
            return cards;
        } else {
            return null;
        }
    }
}
