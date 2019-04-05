package com.github.nl4.money.controller;

import com.github.nl4.money.api.BalanceUpdateRequest;
import com.github.nl4.money.api.TransferRequest;
import com.github.nl4.money.service.FundService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.util.Objects;

public class FundController {

    private static final String JSON = "application/json";

    private final FundService fundService;

    @Inject
    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    /**
     * Endpoint that adds fund to account specified by id.
     */
    public String deposit(Request request, Response response) {
        response.type(JSON);
        var id = request.params(":id");
        var balanceUpdateRequest = new Gson().fromJson(request.body(), BalanceUpdateRequest.class);
        if (balanceUpdateRequest == null || balanceUpdateRequest.getAmount() == null) {
            response.status(400);
            return new Gson().toJson("Balance update request does not contain amount information");
        }
        if (balanceUpdateRequest.getAmount() != null && balanceUpdateRequest.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            response.status(400);
            return new Gson().toJson("Balance update request cannot have negative amount");
        }
        fundService.deposit(Integer.parseInt(id), balanceUpdateRequest.getAmount());
        response.status(200);
        return new Gson().toJson("Balance update request for account [" + id + "] accepted for processing");
    }

    /**
     * Endpoint that subtracts fund from account specified by id.
     */
    public String withdraw(Request request, Response response) {
        response.type(JSON);
        var id = request.params(":id");
        var balanceUpdateRequest = new Gson().fromJson(request.body(), BalanceUpdateRequest.class);
        if (balanceUpdateRequest == null || balanceUpdateRequest.getAmount() == null) {
            response.status(400);
            return new Gson().toJson("Balance update request does not contain amount information");
        }
        if (balanceUpdateRequest.getAmount() != null && balanceUpdateRequest.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            response.status(400);
            return new Gson().toJson("Balance update request cannot have negative amount");
        }
        fundService.withdraw(Integer.parseInt(id), balanceUpdateRequest.getAmount());
        response.status(200);
        return new Gson().toJson("Balance update request for account [" + id + "] accepted for processing");
    }

    /**
     * Endpoint that transfers fund from one account to another.
     */
    public String transfer(Request request, Response response) {
        response.type(JSON);
        var transferRequest = new Gson().fromJson(request.body(), TransferRequest.class);
        if (transferRequest == null || transferRequest.getAccountFromId() == null || transferRequest.getAccountToId() == null || transferRequest.getAmount() == null) {
            response.status(400);
            return new Gson().toJson("Fund transfer request does not contain required information");
        }
        if (transferRequest.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            response.status(400);
            return new Gson().toJson("Fund transfer request cannot have negative amount");
        }
        var accountFromId = transferRequest.getAccountFromId();
        var accountToId = transferRequest.getAccountToId();
        if (Objects.equals(accountFromId, accountToId)) {
            response.status(400);
            return new Gson().toJson("Fund cannot be transferred to originator");
        }
        fundService.transfer(accountFromId, accountToId, transferRequest.getAmount());
        response.status(201);
        return new Gson().toJson("Fund transfer request from [" + accountFromId + "] to [" + accountToId + "] accepted for processing");
    }

}