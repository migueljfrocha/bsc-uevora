package eventastic;

import eventastic.utils.*;
import eventastic.event.*;
import eventastic.payment.*;
import eventastic.users.*;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.math.BigDecimal;



public class Eventastic {
    List<Admin> adminList;
    List<Operator> operatorList;
    List<Event> eventList;
    int eventRegisterCounter;
    

    // Constructor
    public Eventastic() {
        this.adminList = new ArrayList<>();
        this.operatorList = new ArrayList<>();
        this.eventList = new ArrayList<>();
        this.eventRegisterCounter = 0;
    }
    
    
    /**
     * Registers a new admin in the system.
     * @param name String - The name of the admin.
     * @param email String - The email of the admin.
     * @param phoneNumber String - The phone number of the admin.
     * @param address String - The address of the admin.
     * @return The newly created Admin object.
     */
    public Admin registerAdmin(String name, String email, String phoneNumber, String address) {
        int newId = adminList.size() + 1;
        Admin newAdmin = new Admin(newId, name, email, phoneNumber, address);
        adminList.add(newAdmin);
        return newAdmin;
    }


    /**
     * Registers a new operator in the system.
     * @param name String - The name of the operator.
     * @param email String - The email of the operator.
     * @param phoneNumber String - The phone number of the operator.
     * @param address String - The address of the operator.
     * @return The newly created Operator object.
     */
    public Operator registerOperator(String name, String email, String phoneNumber, String address) {
        int newId = operatorList.size() + 1;
        Operator newOperator = new Operator(newId, name, email, phoneNumber, address);
        operatorList.add(newOperator);
        return newOperator;
    }
    

    /**
     * Creates a new participant for an event 
     * @param participantType ParticipantType - The type of the participant (e.g., ATTENDEE, SPEAKER).
     * @param name String - The name of the participant.
     * @param email String - The email of the participant.
     * @param phoneNumber String - The phone number of the participant.
     * @param address String - The address of the participant.
     * @return The newly created Participant object.
     */
    public Participant createParticipant(ParticipantType participantType, String name, String email, String phoneNumber, String address) {
        Participant newParticipant = new Participant(participantType, name, email, phoneNumber, address);
        return newParticipant;
    }


    /**
     * Creates a new event in the system.
     * @param admin Admin - The admin creating the event.
     * @param name String - The name of the event.
     * @param description String - The description of the event.
     * @param local Local - The location of the event.
     * @param nMaxParticipants int - The maximum number of participants allowed.
     * @param eventInterval DateInterval - The date interval for the event.
     * @param intervalRegistration DateInterval - The registration date interval for the event.
     * @param registrationPhases List<RegistrationPhase> - The list of registration phases for the event.
     * @param addons List<AdditionalOption> - The list of additional options for the event.
     * @return The newly created Event object.
     */
    public Event createEvent(Admin admin,
        String name, String description, Local local, int nMaxParticipants,
        DateInterval eventInterval, DateInterval intervalRegistration,
        List<RegistrationPhase> registrationPhases, List<AdditionalOption> addons) 
    {

        int newId = eventList.size() + 1;
        Event newEvent = new Event(newId, admin, name, description, local, nMaxParticipants,
            eventInterval, intervalRegistration, registrationPhases, addons);

        eventList.add(newEvent);

        return newEvent;

    }


    /**
     * Lists all events created by a specific admin.
     * @param admin Admin - The admin whose events are to be listed.
     * @return List<Event> - A list of events created by the specified admin.
     */
    public List<Event> listAdminEvents(Admin admin){
        return admin.getMyEvents(eventList);
    }


    /**
     * Invites an operator to manage a specific event.
     * @param admin Admin - The admin sending the invitation.
     * @param operatorId int - The ID of the operator to be invited.
     * @param eventId int - The ID of the event for which the operator is being invited.
     * @throws IllegalArgumentException if the operator or event does not exist, or if the admin is not the creator of the event.
     */
    public void inviteOperatorToEvent(Admin admin, int operatorId, int eventId){
        admin.sendOpInvite(operatorList, eventList, operatorId, eventId);
    }


    /**
     * Removes an operator from managing a specific event.
     * @param admin Admin - The admin performing the removal.
     * @param operatorId int - The ID of the operator to be removed.
     * @param eventId int - The ID of the event from which the operator is being removed.
     * @throws IllegalArgumentException if the event does not exist, the admin is not the creator of the event, or the operator is not assigned to the event.
     */
    public void removeOperatorFromEvent(Admin admin, int operatorId, int eventId){
        admin.removeOperatorFromEvent(eventList, eventId, operatorId);
    }


    /**
     * Lists all events managed by a specific operator.
     * @param operator Operator - The operator whose events are to be listed.
     * @return List<Event> - A list of events managed by the specified operator.
     */
    public List<Event> listOperatorEvents(Operator operator){
        return operator.getMyEvents(eventList);
    }


    /**
     * Lists all invitations received by a specific operator.
     * @param operator Operator - The operator whose invitations are to be listed.
     * @return List<Invite> - A list of invitations received by the specified operator.
     */
    public List<Invite> listOperatorInvites(Operator operator){
        return operator.getInvites();
    }


    /**
     * Accepts an invitation for an operator to manage an event.
     * @param operator Operator - The operator accepting the invitation.
     * @param inviteId int - The ID of the invitation to be accepted.
     * @throws IllegalArgumentException if the invitation does not exist or has already been accepted.
     */
    public void acceptOperatorInvite(Operator operator, int inviteId){
        operator.acceptInvite(inviteId);
    }


    /**
     * Declines an invitation for an operator to manage an event.
     * @param operator Operator - The operator declining the invitation.
     * @param inviteId int - The ID of the invitation to be declined.
     * @throws IllegalArgumentException if the invitation does not exist or has already been declined.
     */
    public void declineOperatorInvite(Operator operator, int inviteId){
        operator.declineInvite(inviteId);
    }


    /**
     * Lists all available events in the system.
     * @return List<Event> - A list of all available events.
     */
    public List<Event> listAvailableEvents(){
        return Participant.getAllAvailableEvents(eventList);
    }


    /**
     * Searches for events by name.
     * @param name String - The name or partial name of the event to search for.
     * @return List<Event> - A list of events matching the search criteria.
     */
    public List<Event> searchEventsByName(String name){
        return Participant.searchEventsByName(eventList, name);
    }


    /**
     * Registers a participant for a specific event with selected additional options.
     * @param participant Participant - The participant to be registered.
     * @param selectedAddons List<AdditionalOption> - The list of additional options selected by the participant.
     * @param event Event - The event for which the participant is being registered.
     * @return Payment - The payment details for the registration.
     */
    public Payment registerParticipantToEvent(Participant participant, List<AdditionalOption> selectedAddons, Event event) {
        return participant.registerInEvent(getEventRegisterId(), selectedAddons, event);    
    }

    
    /**
     * Generates a unique event registration ID.
     * @return int - A unique event registration ID.
     */
    private int getEventRegisterId() {
        return ++eventRegisterCounter;
    }


    /**
     * Lists all event registrations of a specific participant.
     * @param participant Participant - The participant whose registrations are to be listed.
     * @return List<EventRegistration> - A list of event registrations of the specified participant.
     */
    public List<EventRegistration> getParticipantRegistrations(Participant participant){
        return participant.getMyRegistrations(eventList);
    }



    /**
     * Edits the basic information of an event.
     * This includes the name, description, location, and maximum number of participants.
     * @param admin Admin - The admin requesting the edit.
     * @param event Event - The event to be edited.
     * @param newName String - The new name of the event.
     * @param newDescription String - The new description of the event.
     * @param newLocal Local - The new location of the event.
     * @param nMaxParticipants int - The new maximum number of participants.
     * @return Event - The edited event.
     */    
    public Event editEventBasicInfo (Admin admin, 
            Event event, String newName, String newDescription, Local newLocal, int nMaxParticipants) 
    {
        // Verify that the admin is the creator of the event
        if (!event.getAdmin().equals(admin)) {
            throw new IllegalArgumentException("Only the admin who created the event can edit its basic info.");
        }

        event.editBasicInfo(newName, newDescription, newLocal, nMaxParticipants);

        return event;
    }


    /**
     * Edits the date intervals of an event.
     * This includes the event date interval (start and end of the event) 
     * and the registration date interval (start and end of the registration period).
     * @param admin Admin - The admin requesting the edit.
     * @param event Event - The event to be edited.
     * @param newEventInterval DateInterval - The new event date interval.
     * @param newRegistrationInterval DateInterval - The new registration date interval.
     * @return Event - The edited event.
     */
    public Event editEventDateIntervals (Admin admin, 
            Event event, DateInterval newEventInterval, DateInterval newRegistrationInterval) 
    {
        // Verify that the admin is the creator of the event
        if (!event.getAdmin().equals(admin)) {
            throw new IllegalArgumentException("Only the admin who created the event can edit its date intervals.");
        }

        event.editDateIntervals(newEventInterval, newRegistrationInterval);

        return event;
    }


    /**
     * Edits an additional option of an event.
     * Only the description and price of the additional option can be edited.
     * @param admin Admin - The admin requesting the edit.
     * @param event Event - The event to be edited.
     * @param optionName String - The name of the additional option to be edited.
     * @param newDescription String - The new description of the additional option.
     * @param newPrice BigDecimal - The new price of the additional option.
     * @return Event - The edited event.
     */
    public Event editEventAdditionalOption (Admin admin,
            Event event, String optionName, String newDescription, BigDecimal newPrice) 
    {
        // Verify that the admin is the creator of the event
        if (!event.getAdmin().equals(admin)) {
            throw new IllegalArgumentException("Only the admin who created the event can edit its additional options.");
        }

        event.editAdditionalOptions(optionName, newDescription, newPrice);

        return event;
    }


    /**
     * Cancels an event.
     * @param admin Admin - The admin requesting the cancellation.
     * @param event Event - The event to be cancelled.
     * @throws IllegalArgumentException if the admin is not the creator of the event.
     */
    public void cancelEvent(Admin admin, Event event) {

        // Verify that the admin is the creator of the event
        if (!event.getAdmin().equals(admin)) {
            throw new IllegalArgumentException("Only the admin who created the event can cancel it.");
        }

        event.cancelEvent();
    }


    /**
     * Lists all participants of a specific event.
     * @param staff Staff - The staff member (Admin or Operator) requesting the list.
     * @param event Event - The event whose participants are to be listed.
     * @return List<Participant> - A list of participants registered for the specified event.
     * @throws IllegalArgumentException if the staff member is not authorized to view the participants of the event.
     */
    public List<Participant> listParticipantsOfEvent(Staff staff, Event event) {
        
        if (staff instanceof Admin) {
            Admin admin = (Admin) staff;        
            // Verify that the admin is the creator of the event
            if (!event.getAdmin().equals(admin)) {
                throw new IllegalArgumentException("Only the admin who created the event can list its participants.");
            }
        }
        else if (staff instanceof Operator) {
            Operator operator = (Operator) staff;
            // Verify that the operator is assigned to the event
            if (!isOperatorOfEvent(operator, event)) {
                throw new IllegalArgumentException("Only operators assigned to the event can list its participants.");
            }
        } else {
            throw new IllegalArgumentException("Staff member must be either an Admin or an Operator.");
        }
        
        return event.getParticipants();
    }


    /**
     * Searches for participants of a specific event by name.
     * @param staff Staff - The staff member (Admin or Operator) requesting the search.
     * @param event Event - The event whose participants are to be searched.
     * @param name String - The name or partial name of the participant to search for.
     * @return List<Participant> - A list of participants matching the search criteria.
     * @throws IllegalArgumentException if the staff member is not authorized to view the participants of the event.
     */
    public List<Participant> searchParticipantsByName(Staff staff, Event event, String name) {
        if (staff instanceof Admin) {
            Admin admin = (Admin) staff;        
            // Verify that the admin is the creator of the event
            if (!event.getAdmin().equals(admin)) {
                throw new IllegalArgumentException("Only the admin who created the event can search its participants.");
            }
        }
        else if (staff instanceof Operator) {
            Operator operator = (Operator) staff;
            // Verify that the operator is assigned to the event
            if (!isOperatorOfEvent(operator, event)) {
                throw new IllegalArgumentException("Only operators assigned to the event can search its participants.");
            }
        } else {
            throw new IllegalArgumentException("Staff member must be either an Admin or an Operator.");
        }

        return event.findParticipantsByName(name);
    }


    /**
     * Searches for participants of a specific event by email.
     * @param staff Staff - The staff member (Admin or Operator) requesting the search.
     * @param event Event - The event whose participants are to be searched.
     * @param email String - The email or partial email of the participant to search for.
     * @return List<Participant> - A list of participants matching the search criteria.
     * @throws IllegalArgumentException if the staff member is not authorized to view the participants of the event.
     */
    public List<Participant> searchParticipantsByEmail(Staff staff, Event event, String email) {
        if (staff instanceof Admin) {
            Admin admin = (Admin) staff;        
            // Verify that the admin is the creator of the event
            if (!event.getAdmin().equals(admin)) {
                throw new IllegalArgumentException("Only the admin who created the event can search its participants.");
            }
        }
        else if (staff instanceof Operator) {
            Operator operator = (Operator) staff;
            // Verify that the operator is assigned to the event
            if (!isOperatorOfEvent(operator, event)) {
                throw new IllegalArgumentException("Only operators assigned to the event can search its participants.");
            }
        } else {
            throw new IllegalArgumentException("Staff member must be either an Admin or an Operator.");
        }

        return event.findParticipantsByEmail(email);
    }


    /**
     * Checks if an operator is assigned to a specific event.
     * @param operator Operator - The operator to check.
     * @param event Event - The event to check against.
     * @return boolean - True if the operator is assigned to the event, false otherwise.
     */
    private boolean isOperatorOfEvent(Operator operator, Event event) {
        for (Operator op : event.getOperators()) {
            if (op.getId() == operator.getId()) {
                return true;
            }
        }
        return false;
    }

    
    /**
     * Gets an event by its ID.
     * @param eventId int - The ID of the event to retrieve.
     * @return Event - The event with the specified ID
     * @throws IllegalArgumentException if the event with the specified ID is not found.
     */
    public Event getEventById(int eventId) {
        for(Event event : eventList) {
            if(event.getId() == eventId) {
                return event;
            }
        }
        throw new IllegalArgumentException("Event with ID " + eventId + " not found.");
    }


    /**
     * Exports the list of participants of a specific event to a text file.
     * Only the admin who created the event or operators assigned to the event can perform this action.
     * @param staff Staff - The staff member (Admin or Operator) requesting the export.
     * @param event Event - The event whose participants are to be exported.
     * @param filePath String - The file path where the participant list will be exported.
     * @throws IllegalArgumentException if the staff member is not authorized to export the participants of the event.
     */
    public void exportParticipantRegistrations(Staff staff, Event event, String filePath) {
        
        if (staff instanceof Admin) {
            Admin admin = (Admin) staff;        
            // Verify that the admin is the creator of the event
            if (!event.getAdmin().equals(admin)) {
                throw new IllegalArgumentException("Only the admin who created the event can export its participants.");
            }
        }
        else if (staff instanceof Operator) {
            Operator operator = (Operator) staff;
            // Verify that the operator is assigned to the event
            if (!isOperatorOfEvent(operator, event)) {
                throw new IllegalArgumentException("Only operators assigned to the event can export its participants.");
            }
        } else {
            throw new IllegalArgumentException("Staff member must be either an Admin or an Operator.");
        }
        
        try{
            event.exportRegistrationsToText(filePath);
        }catch(IOException e){
            throw new RuntimeException("Error exporting registrations: " + e.getMessage());
        }
    }
}