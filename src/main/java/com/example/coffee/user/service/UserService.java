package com.example.coffee.user.service;

import com.example.coffee.common.TransactionHandler;
import com.example.coffee.common.exception.CustomException;
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
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final RedissonClient redissonClient;
    private final TransactionHandler transactionHandler;


    public PointResponseDto chargePoint(PointRequestDto requestDto) {

        final String lockName = "lock:" + requestDto.getUserName();
        final RLock lock = redissonClient.getLock(lockName);

        try {
            if (!lock.tryLock(5, 3, TimeUnit.SECONDS)) {
                log.info("락 획득 실패");
                throw new CustomException(ErrorType.FAILED_TO_ACQUIRE_LOCK);
            }
            log.info("락 획득 성공");
            return transactionHandler.runOnWriteTransaction(() -> chargePointLogic(requestDto));
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if(lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
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
