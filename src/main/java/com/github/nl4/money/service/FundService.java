package com.github.nl4.money.service;

import com.github.nl4.money.api.Account;
import com.google.inject.Inject;
import org.jooq.DSLContext;

import java.math.BigDecimal;

public class FundService {

    @Inject
    DSLContext dsl;

    public Account deposit(String id, BigDecimal balance) {
        return null;
    }

    public Account withdraw(String id, BigDecimal balance) {
        return null;
    }

    public String transfer(Integer accountFromId, Integer accountToId, BigDecimal amount) {
        return null;
    }

}