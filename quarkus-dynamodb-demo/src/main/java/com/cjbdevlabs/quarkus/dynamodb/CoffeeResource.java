package com.cjbdevlabs.quarkus.dynamodb;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/coffee")
public class CoffeeResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public List<String> getAll() {
        var lightRoast = "Light Roast";
        var mediumRoast = "Medium Roast";
        var boldRoast = "Bold Roast";
        return List.of(lightRoast, mediumRoast, boldRoast);
    }
}
