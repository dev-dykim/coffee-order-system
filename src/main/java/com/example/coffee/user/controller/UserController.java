package com.example.coffee.user.controller;

import com.example.coffee.user.dto.PointRequestDto;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @PatchMapping("/point")
    void chargePoint(@RequestBody PointRequestDto requestDto) {
        return;
    }
}
