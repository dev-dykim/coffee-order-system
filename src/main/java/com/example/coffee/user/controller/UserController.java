package com.example.coffee.user.controller;

import com.example.coffee.common.response.ApiResponseDto;
import com.example.coffee.common.response.MessageType;
import com.example.coffee.common.response.ResponseUtils;
import com.example.coffee.user.dto.PointRequestDto;
import com.example.coffee.user.dto.PointResponseDto;
import com.example.coffee.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Point", description = "point API document")
public class UserController {

    private final UserService userService;

    @PatchMapping("/users/points")
    @Operation(summary = "포인트 충전", description = "userName, 충전 금액을 입력받아 포인트를 충전합니다.", tags = {"Point"})
    ApiResponseDto<PointResponseDto> chargePoint(@RequestBody PointRequestDto requestDto) {

        return ResponseUtils.ok(userService.chargePoint(requestDto), MessageType.CHARGE_SUCCESSFULLY);
    }
}
