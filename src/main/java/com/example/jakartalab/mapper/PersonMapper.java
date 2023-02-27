package com.example.jakartalab.mapper;

import com.example.jakartalab.dto.PersonDto;
import com.example.jakartalab.entity.Person;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PersonMapper {

    public List<PersonDto> map(List<Person> all) {
        return all.stream().map(person -> new PersonDto(person.getId(), person.getName(), person.getCellNumber())).toList();
    }

}
