package com.example.coffee.order.controller;

import com.example.coffee.order.dto.OrderRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Order", description = "order API document")
public class OrderController {

    @PostMapping("/order")
    @Operation(summary = "커피 주문/결제", description = "username, 메뉴ID를 입력받 주문하여 결제합니다.", tags = {"Order"})
    void orderMenu(@RequestBody OrderRequestDto requestDto) {
        return;
    }
}
