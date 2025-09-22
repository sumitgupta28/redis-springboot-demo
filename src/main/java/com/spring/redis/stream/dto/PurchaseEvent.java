package com.spring.redis.stream.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PurchaseEvent {
    private String purchaseId;
    private String productId;
    private String productName;
    private int price;
    private int quantity;
}
