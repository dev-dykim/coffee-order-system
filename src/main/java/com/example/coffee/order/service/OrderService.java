package com.example.coffee.order.service;

import com.example.coffee.common.TransactionHandler;
import com.example.coffee.common.exception.NotFoundException;
import com.example.coffee.common.response.ErrorType;
import com.example.coffee.config.RedisLockRepository;
import com.example.coffee.menu.entity.Menu;
import com.example.coffee.menu.repository.MenuRepository;
import com.example.coffee.order.dto.OrderRequestDto;
import com.example.coffee.order.dto.OrderResponseDto;
import com.example.coffee.order.entity.Order;
import com.example.coffee.order.repository.OrderRepository;
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
public class OrderService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final RedisLockRepository redisLockRepository;
    private final TransactionHandler transactionHandler;


    public OrderResponseDto makeOrder(OrderRequestDto requestDto) {
        return redisLockRepository.runOnSpinLock(
                requestDto.getUserName(),
                () -> transactionHandler.runOnWriteTransaction(() -> makeOrderLogic(requestDto))
        );
    }


    @Transactional
    public OrderResponseDto makeOrderLogic(OrderRequestDto requestDto) {

        User user = userRepository.findByUserName(requestDto.getUserName()).orElseThrow(
                () -> new NotFoundException(ErrorType.NOT_FOUND_USER)
        );

        Menu menu = menuRepository.findById(requestDto.getMenuId()).orElseThrow(
                () -> new NotFoundException(ErrorType.NOT_FOUND_MENU)
        );

        user.minusPoint(Long.valueOf(menu.getMenuPrice()));

        Order order = orderRepository.save(Order.of(user, menu));

        pointTransactionRepository.save(PointTransaction.of(user, TransactionType.USE, Long.valueOf(menu.getMenuPrice())));

        return OrderResponseDto.of(order.getId());
    }
}
