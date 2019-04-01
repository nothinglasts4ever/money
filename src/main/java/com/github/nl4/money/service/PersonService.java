package com.github.nl4.money.service;

import com.github.nl4.money.domain.Person;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PersonService {

    private static Map<Long, Person> personMap = new ConcurrentHashMap<>();

    public  Map<Long, Person> findAll() {
        return personMap;
    }

    public  Person findById(long id) {
        if (personMap.containsKey(id)) {
            return personMap.get(id);
        }
        return new Person();
    }

    public  Person create(Person person) {
        long id = System.currentTimeMillis();
        System.out.println("Person with [" + id + "] created");
        person.setId(id);
        return personMap.putIfAbsent(id, person);
    }

}