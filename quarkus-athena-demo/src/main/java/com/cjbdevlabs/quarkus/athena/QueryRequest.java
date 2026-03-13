package com.cjbdevlabs.quarkus.athena;

import java.util.List;

public class QueryRequest {
    public String reportType;
    public String reportFilter;
    public String reportCriteria;
    public List<String> groupByFields;
    public String earliestTime;
    public String latestTime;
}