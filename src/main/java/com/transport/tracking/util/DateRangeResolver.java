package com.transport.tracking.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Resolves dateFilter= today|week|month|custom into start/end dates.
 * Java 8 compatible.
 */
public class DateRangeResolver {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static class Range {
        private final LocalDate start;
        private final LocalDate end;

        public Range(LocalDate start, LocalDate end) {
            this.start = start;
            this.end = end;
        }

        public LocalDate getStart() { return start; }
        public LocalDate getEnd() { return end; }
    }

    public static Range resolve(String dateFilter, String startDate, String endDate) {
        if (dateFilter == null || dateFilter.trim().isEmpty()) {
            throw new IllegalArgumentException("dateFilter is required");
        }

        String f = dateFilter.trim().toLowerCase();
        LocalDate today = LocalDate.now();

        switch (f) {
            case "today":
                return new Range(today, today);

            case "week":
                LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
                LocalDate weekEnd = weekStart.plusDays(6);
                return new Range(weekStart, weekEnd);

            case "month":
                LocalDate monthStart = today.withDayOfMonth(1);
                LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());
                return new Range(monthStart, monthEnd);

            case "custom":
                if (startDate == null || endDate == null) {
                    throw new IllegalArgumentException("startDate and endDate required for custom range");
                }
                LocalDate sd = parseDate(startDate);
                LocalDate ed = parseDate(endDate);
                if (sd.isAfter(ed)) {
                    throw new IllegalArgumentException("startDate cannot be after endDate");
                }
                return new Range(sd, ed);

            default:
                throw new IllegalArgumentException("Unknown dateFilter: " + dateFilter);
        }
    }

    public static LocalDate parseDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid date: empty");
        }
        try {
            return LocalDate.parse(date.trim(), FMT);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format (expected yyyy-MM-dd): " + date);
        }
    }
}
