package eventastic.users;

import java.util.List;

import eventastic.event.*;
import eventastic.payment.Payment;
import java.util.ArrayList;

public class Participant extends User{
    private ParticipantType participantType;

    // Constructor
    public Participant(ParticipantType participantType, String name, String email, String phoneNumber, String address) {
        super(name, email, phoneNumber, address);
        this.participantType = participantType;
    }


    /**
     * Gets all available events (TO_BE_STARTED or ONGOING) from a list of events.
     * @param allEvents List of all events.
     * @return List of available events.
     */
    public static List<Event> getAllAvailableEvents(List<Event> allEvents) {
        List<Event> availableEvents = new ArrayList<>();

        for(Event e : allEvents){
            Event.EventStatus status = e.getStatus();
            if(status == Event.EventStatus.TO_BE_STARTED || status == Event.EventStatus.ONGOING){
                availableEvents.add(e);
            }
        }
        return availableEvents;
    }


    /**
     * Searches for events by name from a list of events.
     * @param allEvents List of all events.
     * @param eventName Name of the event to search for.
     * @return List of events that match the search criteria.
     */
    public static List<Event> searchEventsByName(List<Event> allEvents, String eventName) {
        List<Event> foundEvents = new ArrayList<>();

        eventName = eventName.trim().toLowerCase();

        for(Event e : allEvents){
            if(e.getName().toLowerCase().contains(eventName)){
                foundEvents.add(e);
            }
        }
        return foundEvents;
    }
    

    /**
     * Returns a string representation of the participant's details.
     * @return Formatted string of participant details.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------------ Participant Details ------------\n");
        sb.append("Name: " + getName() + "\n");
        sb.append("Email: " + getEmail() + "\n");
        sb.append("Phone Number: " + getPhoneNumber() + "\n");
        sb.append("Address: " + getAddress() + "\n");
        sb.append("Participant Type: " + participantType + "\n");
        sb.append("---------------------------------------------\n");
        return sb.toString();
    }


    /**
     * Gets all event registrations of this participant from a list of events.
     * This method uses the participant's email to identify their registrations
     * because the participant doesn't have an account
     * @param allEvents List of all events.
     * @return List of event registrations of this participant.
     */
    public List<EventRegistration> getMyRegistrations(List<Event> allEvents){
        List<EventRegistration> myRegistrations = new ArrayList<>();

        for(Event e : allEvents){
            for(EventRegistration er : e.getRegistrations()){
                if(er.getParticipant().getEmail().equals(this.getEmail())){
                    myRegistrations.add(er);
                }
            }
        }
        return myRegistrations;
    }


    /**
     * Registers this participant in an event with additional options.
     * @param id Unique ID for the registration.
     * @param additionalOptions List of additional options selected by the participant.
     * @param event The event to register in.
     * @return Payment object associated with the registration.
     * @throws IllegalStateException if the event has reached maximum participants or if the participant is already registered.
     */
    public Payment registerInEvent(int id, List<AdditionalOption> additionalOptions, Event event) {

        // Check if the event has reached maximum participants
        int size = event.getRegistrations().size();
        if( size >= event.getnMaxParticipants()){
            throw new IllegalStateException("Cannot register: Event has reached maximum number of participants.");
        }

        // Check if the participant is already registered
        for (EventRegistration er : event.getRegistrations()) {
            if (er.getParticipant().getEmail().equals(this.getEmail())) {
                throw new IllegalStateException("Participant already registered.");
            }
        }

        EventRegistration newRegistration = new EventRegistration(id, this, additionalOptions, event);
        event.addRegistration(newRegistration);
        return newRegistration.getPayment();
    }


    // Getter
    public ParticipantType getParticipantType() {
        return participantType;
    }

    // Setter
    public void setParticipantType(ParticipantType participantType) {
        this.participantType = participantType;
    }
}