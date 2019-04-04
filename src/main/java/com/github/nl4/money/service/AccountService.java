package com.github.nl4.money.service;

import com.github.nl4.money.api.Account;
import com.google.inject.Inject;
import org.jooq.DSLContext;

import java.util.List;

import static com.github.nl4.money.domain.tables.Account.ACCOUNT;

public class AccountService {

    @Inject
    DSLContext dsl;

    public Account create(Account account) {
        return dsl.insertInto(ACCOUNT, ACCOUNT.USER_NAME, ACCOUNT.ACTIVE, ACCOUNT.BALANCE)
                .values(account.getUserName(), account.getActive(), account.getBalance())
                .returning()
                .fetchOne().into(Account.class);
    }

    public Account findById(Integer id) {
        return dsl.select()
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(id))
                .fetchOneInto(Account.class);
    }

    public List<Account> findAll() {
        return dsl.select()
                .from(ACCOUNT)
                .fetchInto(Account.class);
    }

    public Account activate(String id) {
        return null;
    }

    public Account deactivate(String id) {
        return null;
    }

}