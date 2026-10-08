package eventastic.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import eventastic.users.ParticipantType;
import eventastic.utils.DateInterval;
import eventastic.payment.PhasePrice;

public class RegistrationPhase {
    String name;
    LocalDateTime start;
    LocalDateTime end;
    List<PhasePrice> priceTable;

    // Constructor
    public RegistrationPhase(String name, DateInterval interval, List<PhasePrice> priceTable) {
        this.name = name;
        this.start = interval.getStart();
        this.end = interval.getEnd();
        this.priceTable = priceTable;
    }


    /**
     * toString method for printing the RegistrationPhase details.
     * Prints in the following format:
     *  Phase: [name]
     *   Start: [start date]
     *   End:   [end date]
     *  Price Table:
     *   - [PhasePrice 1]
     * @return A formatted string representing the RegistrationPhase.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  Phase: "+ this.name + "\n");
        sb.append("    Start: " + this.start.toLocalDate() + " at " + this.start.toLocalTime() + "\n");
        sb.append("    End:   " + this.end.toLocalDate() + " at " + this.end.toLocalTime() + "\n");
        sb.append("    Price Table:\n");

        for (PhasePrice pp : this.getPriceTable()) {
            sb.append("      - "+ pp.toString() +"\n");
        }

        return sb.toString();
    }


    /**
     * Checks if the given timestamp falls within this registration phase interval.
     * @param timestamp LocalDateTime - The timestamp to check.
     * @return boolean - true if the timestamp is within the interval, false otherwise.
     */
    public boolean isActiveAt(LocalDateTime timestamp) {
        return timestamp.isAfter(start) && timestamp.isBefore(end);
    }


    public BigDecimal getPriceForParticipantType(ParticipantType participantType) {
        for (PhasePrice pp : priceTable) {
            if (pp.getParticipantType() == participantType) {
                return pp.getPrice();
            }
        }
        throw new IllegalArgumentException("No price found for participant type: " + participantType);
    }
    
    // Getters
    public String getName() {
        return name;
    }
    public LocalDateTime getStart() {
        return start;
    }
    public LocalDateTime getEnd() {
        return end;
    }
    public List<PhasePrice> getPriceTable() {
        return priceTable;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setStart(LocalDateTime start) {

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        this.start = start;
    }
    public void setEnd(LocalDateTime end) {

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        this.end = end;
    }
    public void setPriceTable(List<PhasePrice> priceTable) {
        this.priceTable = priceTable;
    }
}