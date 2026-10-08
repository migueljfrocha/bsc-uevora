package eventastic.event;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import eventastic.utils.*;
import eventastic.users.Admin;
import eventastic.users.Operator;
import eventastic.users.Participant;

import java.util.ArrayList;


public class Event {

    public enum EventStatus {
        TO_BE_STARTED, 
        ONGOING,
        COMPLETED,
        CANCELLED
    }


    private int id;
    private String name;
    private String description;
    private Admin admin;
    private Local local;
    private int nMaxParticipants;
    private EventStatus status;

    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;

    private LocalDateTime registrationStart;
    private LocalDateTime registrationEnd;

    private List<Operator> operators;

    private List<RegistrationPhase> registrationPhases;
    private List<AdditionalOption> addons;
    private List<EventRegistration> registrations;
    

    // Constructor
    public Event (
        int id, Admin admin,
        String name, String description, Local local, int nMaxParticipants,
        DateInterval eventInterval, DateInterval intervalRegistration,
        List<RegistrationPhase> registrationPhases, List<AdditionalOption> addons) 
    {

        if (!validateDates(eventInterval.getStart(), eventInterval.getEnd(),
                           intervalRegistration.getStart(), intervalRegistration.getEnd())) {
            throw new IllegalArgumentException("Invalid date intervals for event and registration: Registration dates must be before event end date.");
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.admin = admin;
        this.local = local;
        this.nMaxParticipants = nMaxParticipants;
        this.status = EventStatus.TO_BE_STARTED;

        this.eventStart = eventInterval.getStart();
        this.eventEnd = eventInterval.getEnd();
        this.registrationStart = intervalRegistration.getStart();
        this.registrationEnd = intervalRegistration.getEnd();

        this.registrationPhases = registrationPhases;
        this.addons = addons;

        this.operators = new ArrayList<>();
        this.registrations = new ArrayList<>();
        
    }


    /**
     * Validates the event and registration date intervals to ensure registration
     * dates are before the event end date.
     * @param eventStart LocalDateTime Start of the event
     * @param eventEnd LocalDateTime End of the event
     * @param registrationStart LocalDateTime Start of registration
     * @param registrationEnd LocalDateTime End of registration
     * @return boolean indicating if the dates are valid
     */
    private boolean validateDates (LocalDateTime eventStart, LocalDateTime eventEnd,
                                  LocalDateTime registrationStart, LocalDateTime registrationEnd) {

        // Registration dates (start and end) must be before event ends 
        return registrationEnd.isBefore(eventEnd) && registrationStart.isBefore(eventEnd);
    }

    
    /**
     * Edits the basic information of the event.
     * @param name String Name of the event
     * @param description String Description of the event
     * @param local Local Location of the event
     * @param nMaxParticipants int Maximum number of participants
     */
    public void editBasicInfo(String name, String description, Local local, int nMaxParticipants){
        this.name = name;
        this.description = description;
        this.local = local;
        this.nMaxParticipants = nMaxParticipants;
    }
    

    /**
     * Edits the date intervals of the event and registration.
     * Verifies that the new intervals are compatible with the existing registration phases.
     * If the new registration interval is changed, adjusts the start and end dates of the first and last registration phases accordingly.
     * @param eventInterval DateInterval that defines the event start and end dates.
     * @param registrationInterval DateInterval that defines the registration start and end dates.
     * @throws IllegalArgumentException if the new registration interval is incompatible with existing registration phases.
     */
    public void editDateIntervals(DateInterval eventInterval, DateInterval registrationInterval){
        LocalDateTime eventStart = eventInterval.getStart();
        LocalDateTime eventEnd = eventInterval.getEnd();
        LocalDateTime registrationStart = registrationInterval.getStart();
        LocalDateTime registrationEnd = registrationInterval.getEnd();


        LocalDateTime maxEnd = LocalDateTime.MIN;
        LocalDateTime minStart = LocalDateTime.MAX;
        RegistrationPhase firstPhase = null;
        RegistrationPhase lastPhase = null;

        for(RegistrationPhase reg : this.registrationPhases){
            // Get the last phase
            if(reg.getEnd().isAfter(maxEnd)){
                maxEnd = reg.getEnd();
                lastPhase = reg;
            }
            // Get the first phase
            if(reg.getStart().isBefore(minStart)){
                minStart = reg.getStart();
                firstPhase = reg;
            }
        }

        // Adjust registration phases if needed
        // Throw exception if new registration interval is incompatible with phases
        if(firstPhase != null){
            firstPhase.setStart(registrationStart);
        }
        if(lastPhase != null){
            lastPhase.setEnd(registrationEnd);
        }
        this.eventStart = eventInterval.getStart();

        // Set the new intervals
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.registrationStart = registrationStart;
        this.registrationEnd = registrationEnd;
    }


    /**
     * Edits an additional option of the event.
     * 
     * @param name String Name of the additional option to be edited.
     * @param description String New description
     * @param price BigDecimal New price
     */
    public void editAdditionalOptions(String name, String description, BigDecimal price){
        for(AdditionalOption addon : this.addons){
            if(addon.getName().equals(name)){
                addon.editAdditionalOption(description, price);
                return;
            }
        }
    }

    
    /**
     * Cancels the event by setting its status to CANCELLED.
     */
    public void cancelEvent(){
        this.status = EventStatus.CANCELLED;
    }


    /**
     * Adds an operator to the event.
     * @param operator Operator to be added to the event.
     */
    public void addOperator(Operator operator) {
        this.operators.add(operator);
    }


    /**
     * Retrieves a list of participants registered for the event.
     * @return List of Participant objects registered for the event.
     */
    public List<Participant> getParticipants() {
        List<Participant> participants = new ArrayList<>();
        for (EventRegistration registration : this.registrations) {
            participants.add(registration.getParticipant());
        }
        return participants;
    }


    /**
     * Adds a registration to the event.
     * @param registration EventRegistration to be added to the event.
     */
    public void addRegistration(EventRegistration registration) {
        this.registrations.add(registration);
    }


    /**
     * Finds participants by name.
     * Given a name, returns a list of participants whose names contain the given string (case-insensitive).
     * @param name String - The name or part of the name to search for.
     * @return List<Participant> - List of participants matching the search criteria.
     */
    public List<Participant> findParticipantsByName(String name){
        List<Participant> participants = new ArrayList<>();
        name = name.trim().toLowerCase();

        for (EventRegistration r : this.registrations) {
            Participant p = r.getParticipant();
            if (p.getName().toLowerCase().contains(name)) {
                participants.add(p);
            }
        }
        return participants;
    }


    /**
     * Finds participants by email.
     * Given an email, returns a list of participants whose emails contain the given string (case-insensitive).
     * @param email String - The email or part of the email to search for.
     * @return List<Participant> - List of participants matching the search criteria.
     */
    public List<Participant> findParticipantsByEmail(String email){
        List<Participant> participants = new ArrayList<>();
        email = email.trim().toLowerCase();

        for (EventRegistration r : this.registrations) {
            Participant p = r.getParticipant();
            if (p.getEmail().toLowerCase().contains(email)) {
                participants.add(p);
            }
        }
        return participants;
    }


    /**
     * Exports the list of participants and their registrations to a formatted text file.
     * @param filepath String - Path where the text file will be saved.
     * @throws IOException if there's an error writing to the file.
     */
    public void exportRegistrationsToText(String filepath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(filepath))) {
            writer.println("----------------------------------------");
            writer.println("EVENT: " + this.name);
            writer.println("Total Registrations: " + this.registrations.size());
            writer.println("----------------------------------------\n");
            
            for (EventRegistration reg : this.registrations) {
                Participant p = reg.getParticipant();
                
                writer.println("Registration #" + reg.getId());
                writer.println("  Participant: " + p.getName());
                writer.println("  Email: " + p.getEmail());
                writer.println("  Phone: " + p.getPhoneNumber());
                writer.println("  Type: " + p.getParticipantType());
                writer.println("  Registration Phase: " + reg.getRegistrationPhaseName());
                writer.println("  Status: " + reg.getStatus());
                writer.printf("  Payment: %.2f EUR%n", reg.getPayment().getAmount());
                
                if (!reg.getAdditionalOptions().isEmpty()) {
                    writer.println("  Additional Options:");
                    for (AdditionalOption option : reg.getAdditionalOptions()) {
                        writer.printf("  " + option.toString() + "\n");
                    }
                }
                writer.println("----------------------------------------\n");
            }
        }
    }


    /**
     * toString method to represent the Event object as a formatted string.
     * @return Formatted string representation of the Event object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("---------------------------------------\n");
        sb.append("  EVENT #" + this.id + ": " + this.name.toUpperCase() + "\n");
        sb.append("---------------------------------------\n\n");
        
        // Description
        sb.append("DESCRIPTION:\n");
        sb.append("  ").append(this.description).append("\n\n");
        
        // Location
        sb.append("LOCATION:\n");
        sb.append(this.local.toString());
        sb.append("\n\n");

        // Event Details
        sb.append("EVENT DETAILS:\n");
        sb.append("  Status: " + this.status + "\n");
        sb.append("  Max Participants: " + this.nMaxParticipants + "\n");
        sb.append("  Event Date: " + this.eventStart.toLocalDate() + " to " + this.eventEnd.toLocalDate() + "\n\n");
        
        // Registration Period
        sb.append("REGISTRATION PERIOD:\n");
        sb.append("  From: " + this.registrationStart.toLocalDate() + " at " + this.registrationStart.toLocalTime() + "\n");
        sb.append("  To:   " + this.registrationEnd.toLocalDate() + " at " + this.registrationEnd.toLocalTime() + "\n\n");
        
        // Registration Phases and Prices
        if (!this.registrationPhases.isEmpty()) {
            sb.append("REGISTRATION PHASES:\n");
            for (RegistrationPhase phase : this.registrationPhases) {
                sb.append("\n");
                sb.append(phase.toString());
                sb.append("\n---------------------------------------\n");
            }
           
        }
        
        // Additional Options
        if (!this.addons.isEmpty()) {
            sb.append("ADDITIONAL OPTIONS:\n");
            for (AdditionalOption addon : this.addons) {
                sb.append(addon.toString());
            }
            sb.append("\n");
        }
        
        // Administrator
        sb.append("MANAGED BY:\n");
        sb.append("  Administrator: " + this.admin.getName() + "\n");
        sb.append("  Contact: " + this.admin.getEmail() + "\n");
        sb.append("---------------------------------------\n");
        
        return sb.toString();
    }
    
    
    // Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Admin getAdmin() {
        return admin;
    }
    public Local getLocal() {
        return local;
    }
    public EventStatus getStatus() {
        return status;
    }
    public LocalDateTime getEventStart() {
        return eventStart;
    }
    public LocalDateTime getEventEnd() {
        return eventEnd;
    }
    public int getnMaxParticipants() {
        return nMaxParticipants;
    }
    public LocalDateTime getRegistrationStart() {
        return registrationStart;
    }
    public LocalDateTime getRegistrationEnd() {
        return registrationEnd;
    }
    public List<Operator> getOperators() {
        return operators;
    }
    public List<RegistrationPhase> getRegistrationPhases() {
        return registrationPhases;
    }
    public List<AdditionalOption> getAddons() {
        return addons;
    }
    public List<EventRegistration> getRegistrations() {
        return registrations;
    }


    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    public void setLocal(Local local) {
        this.local = local;
    }
    public void setnMaxParticipants(int nMaxParticipants) {
        this.nMaxParticipants = nMaxParticipants;
    }
    public void setStatus(EventStatus status) {
        this.status = status;
    }
    public void setEventStart(LocalDateTime eventStart) {
        this.eventStart = eventStart;
    }
    public void setEventEnd(LocalDateTime eventEnd) {
        this.eventEnd = eventEnd;
    }
    public void setRegistrationStart(LocalDateTime registrationStart) {
        this.registrationStart = registrationStart;
    }
    public void setRegistrationEnd(LocalDateTime registrationEnd) {
        this.registrationEnd = registrationEnd;
    }
    public void setAdditionalOptions(List<AdditionalOption> addons) {
        this.addons = addons;
    }
}