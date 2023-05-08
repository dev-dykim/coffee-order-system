package com.example.coffee.order.controller;

import com.example.coffee.user.dto.PointRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    @PostMapping("/order")
    void orderMenu(@RequestBody PointRequestDto requestDto) {
        return;
    }
}
