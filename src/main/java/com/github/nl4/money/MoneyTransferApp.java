package com.github.nl4.money;

import com.github.nl4.money.config.GuiceModule;
import com.github.nl4.money.controller.AccountController;
import com.github.nl4.money.controller.FundsController;
import com.google.inject.Guice;
import com.google.inject.Injector;
import spark.Spark;

public class MoneyTransferApp {

    private static final String JSON = "application/json";

    private MoneyTransferApp() {
        var injector = Guice.createInjector(new GuiceModule());
        loadData(injector);
        createEndpoints(injector);
    }

    private void loadData(Injector injector) {
        var databaseDataLoader = injector.getInstance(DatabaseDataLoader.class);
        databaseDataLoader.createSchema();
    }

    private void createEndpoints(Injector injector) {
        var accountController = injector.getInstance(AccountController.class);
        Spark.post("/accounts", JSON, accountController::create);
        Spark.get("/accounts/:id", accountController::get);
        Spark.get("/accounts", accountController::getAll);
        Spark.put("/accounts/:id/activate", accountController::activate);
        Spark.put("/accounts/:id/deactivate", accountController::deactivate);

        var fundsController = injector.getInstance(FundsController.class);
        Spark.put("/accounts/:id/deposit", JSON, fundsController::deposit);
        Spark.put("/accounts/:id/withdraw", JSON, fundsController::withdraw);
        Spark.post("/transfer", JSON, fundsController::transfer);
    }

    public static void main(String[] args) {
        new MoneyTransferApp();
    }

}