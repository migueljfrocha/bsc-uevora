package eventastic.users;

import java.util.List;
import java.util.ArrayList;

import eventastic.event.Event;
import eventastic.utils.Invite;


public class Admin extends Staff{

    // Constructor
    public Admin(int id, String name, String email, String phoneNumber, String address) {
        super(id, name, email, phoneNumber, address);
    }


    /**
     * Gets all events created by this admin from a list of events.
     * @param allEvents List<Event> - List of all events.
     * @return List<Event> - List of events created by this admin.
     */
    public List<Event> getMyEvents(List<Event> allEvents){
        List<Event> adminEvents = new ArrayList<>();
        
        for(Event event : allEvents){
            if(event.getAdmin().getId() == this.id){
                adminEvents.add(event);
            }
        }
        return adminEvents;
    }


    /**
     * Sends an invite to an operator for a specific event.
     * @param allOperators List<Operator> - List of all operators.
     * @param allEvents List<Event> - List of all events.
     * @param operatorId int - ID of the operator to invite.
     * @param eventId int - ID of the event for which the invite is sent.
     * @throws IllegalArgumentException if the operator or event is not found, or if the admin is not the creator of the event.
     */
    public void sendOpInvite(List<Operator> allOperators, List<Event> allEvents, int operatorId, int eventId) {

        Operator op = null;
        
        for(Operator operator : allOperators){
            if(operator.getId() == operatorId){
                op = operator;
                break;
            }
        }
        if(op == null){
            throw new IllegalArgumentException("Operator with ID " + operatorId + " not found.");
        }

        // find the event
        Event event = null;
        for (Event e : allEvents){
            if(e.getId() == eventId){
                event = e;
                break;
            }
        }
        //check if event exists
        if(event == null){
            throw new IllegalArgumentException("Event with ID " + eventId + " not found.");
        }

        // check if this admin is the creator
        if(event.getAdmin().getId() != this.id){
            throw new IllegalArgumentException("Admin with ID " + this.id + " is not the creator of event with ID " + eventId + ".");
        }


        Invite invite = new Invite(event, this.id, operatorId);
        op.receiveInvite(invite);
    }


    /**
     * Removes an operator from a specific event.
     * @param allEvents List<Event> - List of all events.
     * @param eventId int - ID of the event.
     * @param operatorId int - ID of the operator to be removed.
     * @throws IllegalArgumentException if the event or operator is not found, or if the admin is not the creator of the event.
     */
    public void removeOperatorFromEvent(List<Event> allEvents, int eventId, int operatorId) {
        Event event = null;
        for (Event e : allEvents){
            if(e.getId() == eventId){
                event = e;
                break;
            }
        }
        //check if event exists
        if(event == null){
            throw new IllegalArgumentException("Event with ID " + eventId + " not found.");
        }

        // check if this admin is the creator
        if(event.getAdmin().getId() != this.id){
            throw new IllegalArgumentException("Admin with ID " + this.id + " is not the creator of event with ID " + eventId + ".");
        }

        //find and remove operator
        List<Operator> operators = event.getOperators();
        Operator operatorToRemove = null;
        for (Operator op : operators) {
            if (op.getId() == operatorId) {
                operatorToRemove = op;
                break;
            }
        }
        if (operatorToRemove != null) {
            operators.remove(operatorToRemove);
        } else {
            throw new IllegalArgumentException("Operator with ID " + operatorId + " not found in event with ID " + eventId + ".");
        }
    }
}
