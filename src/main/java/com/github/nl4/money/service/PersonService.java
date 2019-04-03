package com.github.nl4.money.service;

import com.github.nl4.money.api.PersonDTO;
import com.github.nl4.money.domain.tables.Person;
import com.google.inject.Inject;
import org.jooq.DSLContext;

import java.util.List;

public class PersonService {

    @Inject
    DSLContext dsl;

    public List<PersonDTO> findAll() {
        return dsl.select()
                .from(Person.PERSON)
                .fetchInto(PersonDTO.class);
    }

    public PersonDTO findById(Integer id) {
        return dsl.select()
                .from(Person.PERSON)
                .where(Person.PERSON.ID.eq(id))
                .fetchOneInto(PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {
        return dsl.insertInto(Person.PERSON, Person.PERSON.FIRST_NAME, Person.PERSON.LAST_NAME)
                .values(person.getFirstName(), person.getLastName())
                .returning()
                .fetchOne().into(PersonDTO.class);
    }

}