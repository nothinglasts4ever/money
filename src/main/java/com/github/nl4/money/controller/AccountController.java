package com.github.nl4.money.controller;

import com.github.nl4.money.api.Account;
import com.github.nl4.money.service.AccountService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;

public class AccountController {

    private static final String JSON = "application/json";

    private final AccountService accountService;

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Endpoint that creates new account.
     * It will be inactive and have no fund by default if no info provided in request.
     */
    public String create(Request request, Response response) {
        response.type(JSON);
        var account = new Gson().fromJson(request.body(), Account.class);
        var userName = account.getUserName();
        if (userName == null) {
            response.status(400);
            return new Gson().toJson("Account should have user name");
        }
        if (account.getBalance() != null && account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            response.status(400);
            return new Gson().toJson("Account cannot have negative balance");
        }
        accountService.create(account);
        response.status(201);
        return new Gson().toJson("Account creation request for user [" + userName + "] accepted for processing");
    }

    /**
     * Endpoint that returns account by id.
     */
    public String get(Request request, Response response) {
        response.type(JSON);
        var id = request.params(":id");
        var account = accountService.findById(Integer.parseInt(id));
        if (account == null) {
            response.status(404);
            return new Gson().toJson("Account [" + id + "] not found");
        }
        return new Gson().toJson(account);
    }

    /**
     * Endpoint that returns all accounts.
     */
    public String getAll(Request request, Response response) {
        response.type(JSON);
        var accounts = accountService.findAll();
        return new Gson().toJson(accounts);
    }

    /**
     * Endpoint that activates account specified by id.
     */
    public String activate(Request request, Response response) {
        response.type(JSON);
        var id = request.params(":id");
        accountService.activate(Integer.parseInt(id));
        response.status(200);
        return new Gson().toJson("Account activation request for [" + id + "] accepted for processing");
    }

    /**
     * Endpoint that deactivates account specified by id.
     */
    public String deactivate(Request request, Response response) {
        response.type(JSON);
        var id = request.params(":id");
        accountService.deactivate(Integer.parseInt(id));
        response.status(200);
        return new Gson().toJson("Account activation request for [" + id + "] accepted for processing");
    }

}