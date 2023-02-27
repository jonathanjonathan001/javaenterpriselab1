package com.example.jakartalab.resource;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@ApplicationScoped
public class Resources {

    @Produces
    public Jsonb createJsonB() { return JsonbBuilder.create(); }
}
