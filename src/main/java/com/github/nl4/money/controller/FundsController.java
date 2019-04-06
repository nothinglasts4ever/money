package com.github.nl4.money.controller;

import com.github.nl4.money.api.BalanceUpdateRequest;
import com.github.nl4.money.api.EndpointResponse;
import com.github.nl4.money.api.TransferRequest;
import com.github.nl4.money.service.AccountService;
import com.github.nl4.money.service.FundsService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.util.Objects;

public class FundsController {

    private final AccountService accountService;
    private final FundsService fundsService;

    @Inject
    public FundsController(AccountService accountService, FundsService fundsService) {
        this.accountService = accountService;
        this.fundsService = fundsService;
    }

    /**
     * Endpoint that adds funds to account specified by id.
     */
    public String deposit(Request request, Response response) {
        var id = request.params(":id");
        var balanceUpdateRequest = new Gson().fromJson(request.body(), BalanceUpdateRequest.class);
        if (balanceUpdateRequest == null || balanceUpdateRequest.getAmount() == null) {
            return EndpointResponse.badRequest(response, "Balance update request does not contain amount information");
        }
        if (balanceUpdateRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return EndpointResponse.badRequest(response, "Balance update request cannot have negative or zero amount");
        }
        if (accountService.findActiveById(Integer.parseInt(id)) == null) {
            return EndpointResponse.notFound(response, "No active account with id [" + id + "] found");
        }
        fundsService.deposit(Integer.parseInt(id), balanceUpdateRequest.getAmount());
        return EndpointResponse.ok(response, "Balance update request for account [" + id + "] accepted for processing");
    }

    /**
     * Endpoint that subtracts funds from account specified by id.
     */
    public String withdraw(Request request, Response response) {
        var id = request.params(":id");
        var balanceUpdateRequest = new Gson().fromJson(request.body(), BalanceUpdateRequest.class);
        if (balanceUpdateRequest == null || balanceUpdateRequest.getAmount() == null) {
            return EndpointResponse.badRequest(response, "Balance update request does not contain amount information");
        }
        if (balanceUpdateRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return EndpointResponse.badRequest(response, "Balance update request cannot have negative or zero amount");
        }
        if (accountService.findActiveById(Integer.parseInt(id)) == null) {
            return EndpointResponse.notFound(response, "No active account with id [" + id + "] found");
        }
        fundsService.withdraw(Integer.parseInt(id), balanceUpdateRequest.getAmount());
        return EndpointResponse.ok(response, "Balance update request for account [" + id + "] accepted for processing");
    }

    /**
     * Endpoint that transfers funds from one account to another.
     */
    public String transfer(Request request, Response response) {
        var transferRequest = new Gson().fromJson(request.body(), TransferRequest.class);
        if (transferRequest == null || transferRequest.getAccountFromId() == null || transferRequest.getAccountToId() == null || transferRequest.getAmount() == null) {
            return EndpointResponse.badRequest(response, "Funds transfer request does not contain required information");
        }
        if (transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return EndpointResponse.badRequest(response, "Funds transfer request cannot have negative or zero amount");
        }
        var accountFromId = transferRequest.getAccountFromId();
        var accountToId = transferRequest.getAccountToId();
        if (Objects.equals(accountFromId, accountToId)) {
            return EndpointResponse.badRequest(response, "Funds cannot be transferred to originator");
        }
        if (accountService.findActiveById(accountFromId) == null || accountService.findActiveById(accountToId) == null) {
            return EndpointResponse.notFound(response, "No active accounts with id [" + accountFromId + "] and/or [" + accountToId + "] found");
        }
        fundsService.transfer(accountFromId, accountToId, transferRequest.getAmount());
        return EndpointResponse.created(response, "Funds transfer request from [" + accountFromId + "] to [" + accountToId + "] accepted for processing");
    }

}