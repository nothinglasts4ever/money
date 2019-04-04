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
public class Account {
    private Integer id;
    private String userName;
    private Boolean active;
    private BigDecimal balance;
}