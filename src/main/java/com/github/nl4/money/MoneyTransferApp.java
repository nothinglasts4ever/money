package com.github.nl4.money;

import com.github.nl4.money.config.GuiceModule;
import com.github.nl4.money.controller.AccountController;
import com.github.nl4.money.controller.FundController;
import com.google.inject.Guice;
import com.google.inject.Injector;
import spark.Spark;

public class MoneyTransferApp {

    public static final String JSON = "application/json";

    public MoneyTransferApp() {
        Injector injector = Guice.createInjector(new GuiceModule());
        loadData(injector);
        createEndpoints(injector);
    }

    private void loadData(Injector injector) {
        DatabaseDataLoader databaseDataLoader = injector.getInstance(DatabaseDataLoader.class);
        databaseDataLoader.createSchema();
    }

    private void createEndpoints(Injector injector) {
        AccountController accountController = injector.getInstance(AccountController.class);
        Spark.post("/accounts", JSON, accountController::create);
        Spark.get("/accounts/:id", accountController::get);
        Spark.get("/accounts", accountController::getAll);
        Spark.put("/accounts/:id/activate", accountController::activate);
        Spark.put("/accounts/:id/deactivate", accountController::deactivate);

        FundController fundController = injector.getInstance(FundController.class);
        Spark.put("/accounts/:id/deposit", JSON, fundController::deposit);
        Spark.put("/accounts/:id/withdraw", JSON, fundController::withdraw);
        Spark.post("/transfer", JSON, fundController::transfer);
    }

    public static void main(String[] args) {
        new MoneyTransferApp();
    }

}