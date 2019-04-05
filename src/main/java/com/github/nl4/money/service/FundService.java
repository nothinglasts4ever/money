package com.github.nl4.money.service;

import com.github.nl4.money.api.Account;
import com.google.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;

import static com.github.nl4.money.domain.tables.Account.ACCOUNT;

public class FundService {

    private final DSLContext dsl;

    @Inject
    public FundService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void deposit(Integer id, BigDecimal balance) {
        dsl.transaction(ctx -> {
            var account = DSL.using(ctx)
                    .select()
                    .from(ACCOUNT)
                    .where(ACCOUNT.ID.eq(id)
                            .and(ACCOUNT.ACTIVE.eq(true)))
                    .fetchOneInto(Account.class);
            if (account != null) {
                DSL.using(ctx)
                        .update(ACCOUNT)
                        .set(ACCOUNT.BALANCE, account.getBalance().add(balance))
                        .where(ACCOUNT.ID.eq(id))
                        .execute();
            }
        });
    }

    public void withdraw(Integer id, BigDecimal balance) {
        dsl.transaction(ctx -> {
            var account = DSL.using(ctx)
                    .select()
                    .from(ACCOUNT)
                    .where(ACCOUNT.ID.eq(id)
                            .and(ACCOUNT.ACTIVE.eq(true)))
                    .fetchOneInto(Account.class);
            if (account != null && account.getBalance().compareTo(balance) > 0) {
                DSL.using(ctx)
                        .update(ACCOUNT)
                        .set(ACCOUNT.BALANCE, account.getBalance().subtract(balance))
                        .where(ACCOUNT.ID.eq(id))
                        .execute();
            }
        });
    }

    public void transfer(Integer accountFromId, Integer accountToId, BigDecimal amount) {
        dsl.transaction(ctx -> {
            var from = DSL.using(ctx)
                    .select()
                    .from(ACCOUNT)
                    .where(ACCOUNT.ID.eq(accountFromId)
                            .and(ACCOUNT.ACTIVE.eq(true)))
                    .fetchOneInto(Account.class);
            var to = DSL.using(ctx)
                    .select()
                    .from(ACCOUNT)
                    .where(ACCOUNT.ID.eq(accountToId)
                            .and(ACCOUNT.ACTIVE.eq(true)))
                    .fetchOneInto(Account.class);
            if (from != null && to != null && from.getBalance().compareTo(amount) > 0) {
                DSL.using(ctx)
                        .update(ACCOUNT)
                        .set(ACCOUNT.BALANCE, from.getBalance().subtract(amount))
                        .where(ACCOUNT.ID.eq(accountFromId))
                        .execute();
                DSL.using(ctx)
                        .update(ACCOUNT)
                        .set(ACCOUNT.BALANCE, to.getBalance().add(amount))
                        .where(ACCOUNT.ID.eq(accountToId))
                        .execute();
            }
        });
    }

}