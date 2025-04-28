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
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestById {
    @NonNull
    private LocalDateTime orderDate;
    private List<ProductRequestById> products = new ArrayList<>();

    public Optional<ProductRequestById> findProductById(long id) {
        return products.stream()
                .filter(product -> product.getProductId() == id)
                .findFirst();
    }

}
