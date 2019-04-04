package com.github.nl4.money.controller;

import com.github.nl4.money.api.Account;
import com.github.nl4.money.service.AccountService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

import java.util.List;

public class AccountController {

    private static final String JSON = "application/json";

    private final AccountService accountService;

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public String create(Request request, Response response) {
        response.type(JSON);
        Account account = new Gson().fromJson(request.body(), Account.class);
        if (account.getUserName() == null || account.getBalance() == null) {
            response.status(400);
            return new Gson().toJson("Account has no user name and/or balance information");
        }
        response.status(201);
        return new Gson().toJson(accountService.create(account));
    }

    public String get(Request request, Response response) {
        response.type(JSON);
        String id = request.params(":id");
        Account account = accountService.findById(Integer.parseInt(id));
        if (account == null) {
            response.status(404);
            return new Gson().toJson("Account with id [" + id + "] not found");
        }
        return new Gson().toJson(account);
    }

    public String getAll(Request request, Response response) {
        response.type(JSON);
        List<Account> all = accountService.findAll();
        return new Gson().toJson(all);
    }

    public String activate(Request request, Response response) {
        response.type(JSON);
        String id = request.params(":id");
        response.status(200);
        return new Gson().toJson(accountService.activate(id));
    }

    public String deactivate(Request request, Response response) {
        response.type(JSON);
        String id = request.params(":id");
        response.status(200);
        return new Gson().toJson(accountService.deactivate(id));
    }

}