package com.example.coffee.config;

import com.example.coffee.common.exception.CustomException;
import com.example.coffee.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(final String key) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000));
    }

    public Boolean unlock(final String key) {
        return redisTemplate.delete(generateKey(key));
    }

    private String generateKey(final String key) {
        return key.toString();
    }

    public <T> T runOnSpinLock(String key, Supplier<T> task) {
        while (true) {
            if (!lock(key)) {
                try {
                    log.info("락 획득 실패");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new CustomException(ErrorType.FAILED_TO_ACQUIRE_LOCK);
                }
            } else {
                log.info("락 획득 성공, lock number : {}", key);
                break;
            }
        }
        try {
            return task.get();
        } finally {
            log.info("락 해제");
            unlock(key);
        }
    }

    public <T> T runOnDistributedLock(RLock lock, Supplier<T> task) {
        try {
            if (!lock.tryLock(5, 3, TimeUnit.SECONDS)) {
                log.info("락 획득 실패");
                throw new CustomException(ErrorType.FAILED_TO_ACQUIRE_LOCK);
            }
            log.info("락 획득 성공");
            return task.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if(lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
