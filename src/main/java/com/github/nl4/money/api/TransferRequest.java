package com.github.nl4.money.api;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferRequest {
    private Integer accountFromId;
    private Integer accountToId;
    private BigDecimal amount;
}