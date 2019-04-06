package com.github.nl4.money.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceUpdateRequest {
    private BigDecimal amount;
}