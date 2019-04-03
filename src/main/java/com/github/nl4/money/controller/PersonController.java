package com.github.nl4.money.controller;

import com.github.nl4.money.Constants;
import com.github.nl4.money.api.PersonDTO;
import com.github.nl4.money.service.PersonService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

import java.util.List;

public class PersonController {

    private final PersonService personService;

    @Inject
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    public String getAll(Request request, Response response) {
        response.type(Constants.JSON);
        List<PersonDTO> all = personService.findAll();
        return new Gson().toJson(all);
    }

    public String get(Request request, Response response) {
        response.type(Constants.JSON);
        String id = request.params(":id");
        PersonDTO person = personService.findById(Integer.parseInt(id));
        return new Gson().toJson(person);
    }

    public String post(Request request, Response response) {
        response.type(Constants.JSON);
        response.status(201);
        PersonDTO person = new Gson().fromJson(request.body(), PersonDTO.class);
        return new Gson().toJson(personService.create(person));
    }

}