package com.github.nl4.money.config;

import com.github.nl4.money.controller.PersonController;
import com.google.inject.AbstractModule;

public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PersonController.class);
    }

}