package eventastic.utils;

import java.time.LocalDateTime;


/**
 * Represents a date interval with a start and end date-time.
 * Auxiliary class for event scheduling.
 */
public class DateInterval {
    private LocalDateTime start;
    private LocalDateTime end;

    public DateInterval(LocalDateTime start, LocalDateTime end) {
        if (!isValidInterval(start, end)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }


    /**
     * * Validates that the start date is before the end date.
     * * @param start The start date-time.
     * * @param end The end date-time.
     * * @return true if the interval is valid, false otherwise.
     */
    private boolean isValidInterval(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }
}