package com.example.coffee.user.service;

import com.example.coffee.common.TransactionHandler;
import com.example.coffee.common.exception.NotFoundException;
import com.example.coffee.common.response.ErrorType;
import com.example.coffee.config.RedisLockRepository;
import com.example.coffee.user.dto.PointRequestDto;
import com.example.coffee.user.dto.PointResponseDto;
import com.example.coffee.user.entity.PointTransaction;
import com.example.coffee.user.entity.TransactionType;
import com.example.coffee.user.entity.User;
import com.example.coffee.user.repository.PointTransactionRepository;
import com.example.coffee.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final RedisLockRepository redisLockRepository;
    private final RedissonClient redissonClient;
    private final TransactionHandler transactionHandler;


    public PointResponseDto chargePoint(PointRequestDto requestDto) {

        final String lockName = "lock:" + requestDto.getUserName();
        final RLock lock = redissonClient.getLock(lockName);

        return redisLockRepository.runOnDistributedLock(
                lock, () -> transactionHandler.runOnWriteTransaction(
                        () -> chargePointLogic(requestDto))
        );
    }

    @Transactional
    public PointResponseDto chargePointLogic(PointRequestDto requestDto) {

        User user = userRepository.findByUserName(requestDto.getUserName()).orElseThrow(
                () -> new NotFoundException(ErrorType.NOT_FOUND_USER)
        );

        user.plusPoint(requestDto.getPoint());

        pointTransactionRepository.save(PointTransaction.of(user, TransactionType.CHARGE, requestDto.getPoint()));

        return PointResponseDto.of(user.getPoint());
    }

}
