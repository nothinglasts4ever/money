package com.github.nl4.money.service;

import com.github.nl4.money.api.Account;
import com.google.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.github.nl4.money.domain.tables.Account.ACCOUNT;

public class AccountService {

    private final DSLContext dsl;

    @Inject
    public AccountService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void create(Account account) {
        var active = account.getActive() != null ? account.getActive() : false;
        var balance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;
        dsl.transaction(ctx ->
                DSL.using(ctx)
                        .insertInto(ACCOUNT, ACCOUNT.USER_NAME, ACCOUNT.ACTIVE, ACCOUNT.BALANCE)
                        .values(account.getUserName(), active, balance)
                        .returning()
                        .fetchOne().into(Account.class)
        );
    }

    public Account findById(Integer id) {
        return dsl.select()
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(id))
                .fetchOneInto(Account.class);
    }

    public Account findActiveById(Integer id) {
        return dsl.select()
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(id)
                        .and(ACCOUNT.ACTIVE.eq(true)))
                .fetchOneInto(Account.class);
    }

    public List<Account> findAll() {
        return dsl.select()
                .from(ACCOUNT)
                .fetchInto(Account.class);
    }

    public void activate(Integer id) {
        dsl.transaction(ctx ->
                DSL.using(ctx)
                        .update(ACCOUNT)
                        .set(ACCOUNT.ACTIVE, true)
                        .where(ACCOUNT.ID.eq(id))
                        .execute()
        );
    }

    public void deactivate(Integer id) {
        dsl.transaction(ctx ->
                DSL.using(ctx)
                        .update(ACCOUNT)
                        .set(ACCOUNT.ACTIVE, false)
                        .where(ACCOUNT.ID.eq(id))
                        .execute()
        );
    }

}