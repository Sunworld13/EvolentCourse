package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequestById;
import com.example.orderservice.dto.Status;
import com.example.orderservice.model.Order;
import com.example.orderservice.dto.OrderRequestByName;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping()
    public ResponseEntity<List<Order>> getOrders() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable long id) {
        return orderService.findById(id);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable long userId) {
        return orderService.findAllByUser(userId);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<HttpStatus> deleteAllOrdersByUser(@PathVariable long userId) {
        return orderService.deleteAllByUserID(userId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable long id) {
        return orderService.deleteById(id);
    }

    @PostMapping("/user/{userId}/name")
    public ResponseEntity<?> createOrderByName(@PathVariable long userId, @RequestBody OrderRequestByName orderRequestByName) {
        return orderService.createByName(userId, orderRequestByName,null);
    }
    @PostMapping("/user/{userId}/id")
    public ResponseEntity<?> createOrderById(@PathVariable long userId, @RequestBody OrderRequestById orderRequestById) {
        return orderService.createById(userId, orderRequestById,null);
    }

    @PutMapping("{orderId}/name")
    public ResponseEntity<?> updateOrderByName(@PathVariable long orderId , @RequestBody OrderRequestByName orderRequestByName) {
        return orderService.updateByName(orderRequestByName,orderId);
    }
    @PutMapping("{orderId}/id")
    public ResponseEntity<?> updateOrderById(@PathVariable long orderId, @RequestBody OrderRequestById orderRequestById) {
        return orderService.updateById(orderRequestById,orderId);
    }

    @PutMapping("{orderId}/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable long orderId,@PathVariable Status status) {
        return orderService.updateStatus(orderId,status);
    }




}
