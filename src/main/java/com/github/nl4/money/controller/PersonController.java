package com.github.nl4.money.controller;

import com.github.nl4.money.Constants;
import com.github.nl4.money.domain.Person;
import com.github.nl4.money.service.PersonService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

import java.util.Map;

public class PersonController {

    @Inject
    PersonService personService;

    public Object getAll(Request request, Response response) {
        response.type(Constants.JSON);
        Map<Long, Person> all = personService.findAll();
        return new Gson().toJson(all);
    }

    public Object get(Request request, Response response) {
        response.type(Constants.JSON);
        String id = request.params(":id");
        Person person = personService.findById(Long.parseLong(id));
        return new Gson().toJson(person);
    }

    public Object post(Request request, Response response) {
        response.type(Constants.JSON);
        Person person = new Gson().fromJson(request.body(), Person.class);
        return new Gson().toJson(personService.create(person));
    }

}