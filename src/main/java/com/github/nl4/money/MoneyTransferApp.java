package com.github.nl4.money;

import com.github.nl4.money.config.GuiceModule;
import com.github.nl4.money.controller.PersonController;
import com.google.inject.Guice;
import com.google.inject.Injector;
import spark.Spark;

public class MoneyTransferApp {

    public MoneyTransferApp() {
        Injector injector = Guice.createInjector(new GuiceModule());
        PersonController personController = injector.getInstance(PersonController.class);

        Spark.get("/persons", personController::getAll);
        Spark.get("/persons/:id", personController::get);
        Spark.post("/persons", Constants.JSON, personController::post);
    }

    public static void main(String[] args) {
        new MoneyTransferApp();
    }

}