package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestByName {
    @NonNull
    private LocalDateTime orderDate;
    private List<ProductRequestByName> products = new ArrayList<>();

    public Optional<ProductRequestByName> findProductByName(String name) {
        return products.stream()
                .filter(product -> name.equals(product.getName()))
                .findFirst();
    }
}
