package com.github.nl4.money;

import com.github.nl4.money.controller.PersonController;
import com.google.inject.Inject;
import spark.Spark;

public class MoneyTransferApp {

    @Inject
    PersonController personController;

    public MoneyTransferApp() {
        Spark.get("/persons", personController::getAll);
        Spark.get("/persons/:id", personController::get);
        Spark.post("/persons", Constants.JSON, personController::post);
    }

    public static void main(String[] args) {
        new MoneyTransferApp();
    }

}