package com.example.coffee.user.controller;

import com.example.coffee.user.dto.PointRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Point", description = "point API document")
public class UserController {

    @PatchMapping("/point")
    @Operation(summary = "포인트 충전", description = "username, 충전 금액을 입력받아 포인트를 충전합니다.", tags = {"Point"})
    void chargePoint(@RequestBody PointRequestDto requestDto) {
        return;
    }
}
