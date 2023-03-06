package com.example.jakartalab.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IdNotFoundExceptionMapper implements ExceptionMapper<IdNotFoundException> {
    @Override
    public Response toResponse(IdNotFoundException e) {
        return Response.status(404).entity(e.getMessage()).build();
    }
}
