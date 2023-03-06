package com.example.jakartalab.controller;

import com.example.jakartalab.dto.PersonDto;
import com.example.jakartalab.entity.Person;
import com.example.jakartalab.exception.IdNotFoundException;
import com.example.jakartalab.mapper.PersonMapper;
import com.example.jakartalab.repository.PersonRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;


@Path("/persons")
public class PersonController {

    @Inject
    PersonMapper mapper;

    @Inject
    PersonRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PersonDto> getAll(@QueryParam("name") String name) {
        if (name == null)
            return mapper.map(repository.findAll());
        return mapper.map(repository.findAllByName(name));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@PathParam("id") Long id) {
        var person = repository.findOne(id);
        if (person.isPresent())
            return Response.ok().entity(person.get()).build();
        throw new IdNotFoundException(("Not found ID: " + id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOne(@Valid Person person) {
        repository.insertPerson(person);
        return Response.created(URI.create("persons/" + person.getId())).build();
    }

    @DELETE
    @Path("/{id}")
    public void deleteOne(@PathParam("id") Long id) { repository.deletePerson(id); }

}
