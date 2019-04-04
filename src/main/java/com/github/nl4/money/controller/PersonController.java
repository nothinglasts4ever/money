package com.github.nl4.money.controller;

import com.github.nl4.money.api.PersonDTO;
import com.github.nl4.money.service.PersonService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

import java.util.List;

public class PersonController {

    private static final String JSON = "application/json";

    private final PersonService personService;

    @Inject
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    public String getAll(Request request, Response response) {
        response.type(JSON);
        List<PersonDTO> all = personService.findAll();
        return new Gson().toJson(all);
    }

    public String get(Request request, Response response) {
        response.type(JSON);
        String id = request.params(":id");
        PersonDTO person = personService.findById(Integer.parseInt(id));
        if (person == null) {
            response.status(404);
            return new Gson().toJson("Person with id [" + id + "] not found");
        }
        return new Gson().toJson(person);
    }

    public String post(Request request, Response response) {
        response.type(JSON);
        PersonDTO person = new Gson().fromJson(request.body(), PersonDTO.class);
        if (person.getFirstName() == null || person.getLastName() == null) {
            response.status(400);
            return new Gson().toJson("Person has invalid first name and/or last name");
        }
        response.status(201);
        return new Gson().toJson(personService.create(person));
    }

}