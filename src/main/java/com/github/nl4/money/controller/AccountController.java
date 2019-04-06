package com.github.nl4.money.controller;

import com.github.nl4.money.api.Account;
import com.github.nl4.money.api.EndpointResponse;
import com.github.nl4.money.service.AccountService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;

public class AccountController {

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
        var account = new Gson().fromJson(request.body(), Account.class);
        var userName = account.getUserName();
        if (userName == null) {
            return EndpointResponse.badRequest(response, "Account should have user name");
        }
        if (account.getBalance() != null && account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            return EndpointResponse.badRequest(response, "Account cannot have negative balance");
        }
        accountService.create(account);
        return EndpointResponse.created(response, "Account creation request for user [" + userName + "] accepted for processing");
    }

    /**
     * Endpoint that returns account by id.
     */
    public String get(Request request, Response response) {
        var id = request.params(":id");
        var account = accountService.findById(Integer.parseInt(id));
        if (account == null) {
            return EndpointResponse.notFound(response, "Account [" + id + "] not found");
        }
        return EndpointResponse.ok(response, account);
    }

    /**
     * Endpoint that returns all accounts.
     */
    public String getAll(Request request, Response response) {
        var accounts = accountService.findAll();
        return EndpointResponse.ok(response, accounts);
    }

    /**
     * Endpoint that activates account specified by id.
     */
    public String activate(Request request, Response response) {
        var id = request.params(":id");
        if (accountService.findById(Integer.parseInt(id)) == null) {
            return EndpointResponse.notFound(response, "Account [" + id + "] not found");
        }
        accountService.activate(Integer.parseInt(id));
        return EndpointResponse.ok(response, "Account activation request for [" + id + "] accepted for processing");
    }

    /**
     * Endpoint that deactivates account specified by id.
     */
    public String deactivate(Request request, Response response) {
        var id = request.params(":id");
        if (accountService.findById(Integer.parseInt(id)) == null) {
            return EndpointResponse.notFound(response, "Account [" + id + "] not found");
        }
        accountService.deactivate(Integer.parseInt(id));
        return EndpointResponse.ok(response, "Account deactivation request for [" + id + "] accepted for processing");
    }

}