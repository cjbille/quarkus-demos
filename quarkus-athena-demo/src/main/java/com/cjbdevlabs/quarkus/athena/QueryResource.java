package com.cjbdevlabs.quarkus.athena;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/query")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QueryResource {

    @Inject
    QueryService queryService;

    @Inject
    AthenaService athenaService;

    @POST
    public Response runQuery(QueryRequest req) {

        String sql = queryService.buildQuery(req);
        List<Map<String,Object>> results = athenaService.executeAthenaQuery(sql);

        return Response.ok(results).build();
    }
}