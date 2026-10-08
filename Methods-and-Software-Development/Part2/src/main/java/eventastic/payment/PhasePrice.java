package eventastic.payment;

import java.math.BigDecimal;

import eventastic.users.ParticipantType;

public class PhasePrice {
    private BigDecimal price;
    private ParticipantType participantType;

    // Constructor
    public PhasePrice(BigDecimal price, ParticipantType participantType) {
        this.price = price;
        this.participantType = participantType;
    }

    /**
     * toString method to represent the PhasePrice object as a formatted string.
     * Prints in the following format:
     *   ParticipantType: Price EUR
     * @return Formatted string representation of the PhasePrice object.
     */
    public String toString() {
        return String.format("%s: %.2f EUR", this.participantType, this.price);
    }

    // Getters

    public BigDecimal getPrice() {
        return price;
    }
    public ParticipantType getParticipantType() {
        return participantType;
    }

    // Setters

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public void setParticipantType(ParticipantType participantType) {
        this.participantType = participantType;
    }
    
}