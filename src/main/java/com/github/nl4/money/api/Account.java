package com.github.nl4.money.api;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Account {
    private Integer id;
    private String userName;
    private Boolean active;
    private BigDecimal balance;
}