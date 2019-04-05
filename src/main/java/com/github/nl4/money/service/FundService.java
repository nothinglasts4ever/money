package com.github.nl4.money.service;

import com.google.inject.Inject;
import org.jooq.DSLContext;

import java.math.BigDecimal;

public class FundService {

    private final DSLContext dsl;

    @Inject
    public FundService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void deposit(Integer id, BigDecimal balance) {
    }

    public void withdraw(Integer id, BigDecimal balance) {
    }

    public String transfer(Integer accountFromId, Integer accountToId, BigDecimal amount) {
        return null;
    }

}