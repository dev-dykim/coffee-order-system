package com.example.coffee.common;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionHandler {

    @Transactional
    public <T> T runOnWriteTransaction(Supplier<T> supplier) {
        return supplier.get();
    }
}
