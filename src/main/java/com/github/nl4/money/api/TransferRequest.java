package com.github.nl4.money.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    private Integer accountFromId;
    private Integer accountToId;
    private BigDecimal amount;
}