package com.cjbdevlabs.quarkus.athena;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;


@ApplicationScoped
public class AthenaService {

    public List<Map<String,Object>> executeAthenaQuery(String sql) {
        // call Athena JDBC / SDK here
        // return results as list of maps (JSON-ready)
        return null;
    }
}
