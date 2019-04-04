package com.github.nl4.money.controller;

import com.github.nl4.money.api.BalanceUpdateRequest;
import com.github.nl4.money.api.TransferRequest;
import com.github.nl4.money.service.FundService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class FundController {

    private static final String JSON = "application/json";

    private final FundService fundService;

    @Inject
    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    public String deposit(Request request, Response response) {
        response.type(JSON);
        String id = request.params(":id");
        BalanceUpdateRequest balanceUpdateRequest = new Gson().fromJson(request.body(), BalanceUpdateRequest.class);
        if (balanceUpdateRequest == null || balanceUpdateRequest.getAmount() == null) {
            response.status(400);
            return new Gson().toJson("Balance update request does not contain amount information");
        }
        response.status(200);
        return new Gson().toJson(fundService.deposit(id, balanceUpdateRequest.getAmount()));
    }

    public String withdraw(Request request, Response response) {
        response.type(JSON);
        String id = request.params(":id");
        BalanceUpdateRequest balanceUpdateRequest = new Gson().fromJson(request.body(), BalanceUpdateRequest.class);
        if (balanceUpdateRequest == null || balanceUpdateRequest.getAmount() == null) {
            response.status(400);
            return new Gson().toJson("Balance update request does not contain amount information");
        }
        response.status(200);
        return new Gson().toJson(fundService.withdraw(id, balanceUpdateRequest.getAmount()));
    }

    public String transfer(Request request, Response response) {
        response.type(JSON);
        TransferRequest transferRequest = new Gson().fromJson(request.body(), TransferRequest.class);
        if (transferRequest == null || transferRequest.getAccountFromId() == null || transferRequest.getAccountToId() == null || transferRequest.getAmount() == null) {
            response.status(400);
            return new Gson().toJson("Transfer request does not contain required information");
        }
        if (Objects.equals(transferRequest.getAccountFromId(), transferRequest.getAccountToId())) {
            response.status(400);
            return new Gson().toJson("Fund cannot be transferred to originator");
        }
        response.status(201);
        return new Gson().toJson(fundService.transfer(transferRequest.getAccountFromId(), transferRequest.getAccountToId(), transferRequest.getAmount()));
    }

}