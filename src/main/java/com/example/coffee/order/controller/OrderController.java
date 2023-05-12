package com.example.coffee.order.controller;

import com.example.coffee.common.response.ApiResponseDto;
import com.example.coffee.common.response.MessageType;
import com.example.coffee.common.response.ResponseUtils;
import com.example.coffee.order.dto.OrderRequestDto;
import com.example.coffee.order.service.OrderService;
import com.example.coffee.user.dto.PointResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Order", description = "order API document")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    @Operation(summary = "커피 주문/결제", description = "userName, 메뉴ID를 입력받 주문하여 결제합니다.", tags = {"Order"})
    ApiResponseDto<PointResponseDto> orderMenu(@RequestBody OrderRequestDto requestDto) {

        return ResponseUtils.ok(orderService.makeOrder(requestDto), MessageType.ORDER_SUCCESSFULLY);
    }
}
