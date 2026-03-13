package com.cjbdevlabs.quarkus.athena;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private List<String> selectFields = new ArrayList<>();
    private List<String> whereClauses = new ArrayList<>();
    private List<String> groupByFields = new ArrayList<>();
    private List<String> aggregates = new ArrayList<>();
    private String fromDb = "my_db";
    private String fromTable = "my_table"; // default table, replace if needed

    /** Add columns to SELECT */
    public void addSelect(String... fields) {
        for (String f : fields) {
            selectFields.add(f);
        }
    }

    /** Add aggregate functions with proper aliases */
    public void addSelectAggregates(String... aggs) {
        for (String agg : aggs) {
            // Automatically alias COUNT(*) → count, SIZE(*) → size
            if ("COUNT(*)".equalsIgnoreCase(agg)) {
                aggregates.add("COUNT(*) AS count");
            } else if ("SIZE(*)".equalsIgnoreCase(agg)) {
                aggregates.add("SIZE(*) AS size");
            } else {
                aggregates.add(agg);
            }
        }
    }

    /** Add a single WHERE clause */
    public void addWhere(String condition) {
        whereClauses.add(condition);
    }

    /** Add GROUP BY fields */
    public void addGroupBy(List<String> fields) {
        groupByFields.addAll(fields);
    }

    /** Optional: set table name if not default */
    public void setFromTable(String tableName) {
        this.fromTable = tableName;
    }

    /** Build the final SQL query */
    public String build() {
        StringBuilder sb = new StringBuilder("SELECT ");

        // Build select part
        if (!selectFields.isEmpty()) {
            sb.append(String.join(", ", selectFields));
        }
        if (!aggregates.isEmpty()) {
            if (!selectFields.isEmpty()) sb.append(", ");
            sb.append(String.join(", ", aggregates));
        }

        sb.append(" FROM ").append(fromDb).append(".").append(fromTable);

        // Build where part
        if (!whereClauses.isEmpty()) {
            sb.append(" WHERE ").append(String.join(" AND ", whereClauses));
        }

        // Build group by part
        if (!groupByFields.isEmpty()) {
            sb.append(" GROUP BY ").append(String.join(", ", groupByFields));
        }

        return sb.toString();
    }
}