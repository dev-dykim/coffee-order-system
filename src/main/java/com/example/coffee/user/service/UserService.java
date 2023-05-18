package com.example.coffee.user.service;

import com.example.coffee.common.exception.NotFoundException;
import com.example.coffee.common.response.ErrorType;
import com.example.coffee.user.dto.PointRequestDto;
import com.example.coffee.user.dto.PointResponseDto;
import com.example.coffee.user.entity.PointTransaction;
import com.example.coffee.user.entity.TransactionType;
import com.example.coffee.user.entity.User;
import com.example.coffee.user.repository.PointTransactionRepository;
import com.example.coffee.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointTransactionRepository pointTransactionRepository;


    @Transactional
    public PointResponseDto chargePoint(PointRequestDto requestDto) {

        User user = userRepository.findByUserName(requestDto.getUserName()).orElseThrow(
                () -> new NotFoundException(ErrorType.NOT_FOUND_USER)
        );

        user.plusPoint(requestDto.getPoint());

        pointTransactionRepository.save(PointTransaction.of(user, TransactionType.CHARGE, requestDto.getPoint()));

        return PointResponseDto.of(user.getPoint());
    }

}
