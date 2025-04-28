package com.example.orderservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.example.orderservice.dto.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    @NonNull
    private long userId;
    @NonNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @NonNull
    private LocalDateTime orderDate;
    @NonNull
    private double totalCost;
    @NonNull
    private Long totalQuantity;
    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();

}
