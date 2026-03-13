package com.cjbdevlabs.quarkus.athena;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ApplicationScoped
public class QueryService {

    public String buildQuery(QueryRequest req) {
        QueryBuilder qb = new QueryBuilder();

        switch (req.reportType.toLowerCase()) {
            case "table":
                qb.addSelect("*");
                if ("*".equals(req.reportFilter)) {
                    if (req.reportCriteria != null && !req.reportCriteria.isEmpty()) {
                        addReportCriteria(qb, req.reportCriteria);
                    }
                } else {
                    addFilterConditions(qb, req.reportFilter);
                    if (req.reportCriteria != null && !req.reportCriteria.isEmpty()) {
                        addReportCriteria(qb, req.reportCriteria);
                    }
                }
                break;

            case "summary":
                addFilterConditions(qb, req.reportFilter);
                if (req.reportCriteria != null && !req.reportCriteria.isEmpty()) {
                    addReportCriteria(qb, req.reportCriteria);
                }

                if (req.groupByFields != null && !req.groupByFields.isEmpty()) {
                    qb.addGroupBy(req.groupByFields);
                }

                qb.addSelectAggregates("COUNT(*)", "SIZE(*)");
                break;

            default:
                throw new IllegalArgumentException("Unsupported report type: " + req.reportType);
        }

        addTimeFilters(qb, req);

        return qb.build();
    }

    /** Adds the report filters like field1(val1,val2,...) */
    private void addFilterConditions(QueryBuilder qb, String reportFilter) {
        Pattern p = Pattern.compile("(\\w+)\\((.*?)\\)");
        Matcher m = p.matcher(reportFilter);

        while (m.find()) {
            String field = m.group(1);
            String values = m.group(2).trim();

            if ("*".equals(values)) {
                continue; // skip wildcards
            }

            // Split by comma and trim spaces
            String[] vals = Arrays.stream(values.split(","))
                    .map(String::trim)
                    .toArray(String[]::new);

            if (vals.length == 1) {
                qb.addWhere(field + " = '" + vals[0] + "'");
            } else {
                String in = Arrays.stream(vals)
                        .map(v -> "'" + v + "'")
                        .collect(Collectors.joining(", "));
                qb.addWhere(field + " IN (" + in + ")");
            }
        }
    }

    /** Adds custom reportCriteria, e.g., id(123) or other conditions */
    private void addReportCriteria(QueryBuilder qb, String criteria) {
        qb.addWhere(criteria);
    }

    private void addTimeFilters(QueryBuilder qb, QueryRequest req) {
        LocalDate start = LocalDate.parse(req.earliestTime);
        LocalDate end = LocalDate.parse(req.latestTime);

        qb.addWhere("year = " + start.getYear());
        qb.addWhere("month = " + start.getMonthValue());
        qb.addWhere("day BETWEEN " + start.getDayOfMonth() +
                " AND " + end.getDayOfMonth());
    }
}