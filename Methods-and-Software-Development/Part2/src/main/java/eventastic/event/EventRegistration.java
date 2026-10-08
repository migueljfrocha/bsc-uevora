package eventastic.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import eventastic.payment.Payment;
import eventastic.users.Participant;

public class EventRegistration {

    public enum RegistrationStatus {
        PENDING,
        CONFIRMED,
        CANCELLED
    }

    private int id;
    private List<AdditionalOption> additionalOptions;
    private Payment payment;
    private Participant participant;
    private RegistrationStatus status;
    private String registrationPhaseName;


    // Constructor
    public EventRegistration(int id, Participant participant, List<AdditionalOption> additionalOptions, Event event) {
        this.id = id;
        this.additionalOptions = additionalOptions;
        this.participant = participant;
        this.status = RegistrationStatus.CONFIRMED; //Implementation restriction: not using PENDING 
        setupPayment(id, event);
    }


    /**
     * Auxiliary method to setup the payment for the registration.
     * Includes calculating the total amount based on the registration phase and additional options,
     * and generating a random IBAN for the payment.
     * Also sets the registrationPhaseName attribute.
     * @param id int - The registration ID.
     * @param event Event - The event for which the registration is made.
     * @throws IllegalStateException if no active registration phase is found for the event at the current time.
     */
    private void setupPayment(int id, Event event) {
        BigDecimal total = BigDecimal.ZERO;

        LocalDateTime now = LocalDateTime.now();
        RegistrationPhase currentPhase = null;
        for(RegistrationPhase phase : event.getRegistrationPhases()) {
            if(phase.isActiveAt(now)) {
                currentPhase = phase;
                break;
            }
        }
        if(currentPhase == null) {
            throw new IllegalStateException("No active registration phase found for the event at the current time.");
        }

        this.registrationPhaseName = currentPhase.getName();
        
        BigDecimal basePrice = currentPhase.getPriceForParticipantType(participant.getParticipantType());
        total = total.add(basePrice);

        // Add prices of additional options
        for (AdditionalOption option : additionalOptions) {
            total = total.add(option.getPrice());
        }

        // Generate a random payment reference
        Random rd = new Random();
        StringBuilder sb = new StringBuilder(21);
        sb.append("PT50");
        for (int i = 0; i < 21; i++) {
            sb.append(rd.nextInt(10));
        }
        
        String description = "Eventastic: Registration fee for registration #" + id + " - Event #" + event.getId();
        
        this.payment = new Payment(id, total, sb.toString(), now, description);
    }


    /**
     * toString method for printing the EventRegistration details.
     * @return String - The string representation of the EventRegistration.
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(participant.toString());
        sb.append("\n---------- Registration #"+ id + "----------\n");
        sb.append("Registration Phase: " + registrationPhaseName + "\n");
        sb.append("Status: " + status + "\n");
        sb.append("Additional Options:\n");
        for(AdditionalOption option : additionalOptions){
            sb.append(option.toString() + "\n");
        }
        sb.append("Payment Details:\n");
        sb.append(String.format("Amount: %.2f EUR\n", payment.getAmount()));
        sb.append("IBAN: " + payment.getIBAN() + "\n");
        return sb.toString();
    }


    // Getters
    public int getId() {
        return id;
    }
    public List<AdditionalOption> getAdditionalOptions() {
        return additionalOptions;
    }
    public Payment getPayment() {
        return payment;
    }
    public Participant getParticipant() {
        return participant;
    }
    public RegistrationStatus getStatus() {
        return status;
    }
    public String getRegistrationPhaseName() {
        return registrationPhaseName;
    }


    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setAdditionalOptions(List<AdditionalOption> additionalOptions) {
        this.additionalOptions = additionalOptions;
    }
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }
}