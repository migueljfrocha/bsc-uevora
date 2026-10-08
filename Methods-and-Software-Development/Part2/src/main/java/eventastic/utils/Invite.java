package eventastic.utils;

import eventastic.event.Event;

public class Invite {
    private int id;
    private final Event event;
    private final int adminId;
    private final int operatorId;
    private boolean accepted = false;

    public Invite(Event event, int adminId, int operatorId) {
        this.event = event;
        this.adminId = adminId;
        this.operatorId = operatorId;
    }


    /**
     * toString method for printing the Invite details.
     * Prints in the following format:
     * ------------- Invite #[id] -------------
     * Event: [event name] (ID: [event id])
     * From Admin ID: [adminId]
     * To Operator ID: [operatorId]
     * Status: [ACCEPTED/NOT ACCEPTED]
     * ----------------------------------------
     * @return A formatted string representing the Invite details.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("------------- Invite #" + this.id + " -------------\n");
        sb.append("Event: " + this.event.getName() + " (ID: " + this.event.getId() + ")\n");
        sb.append("From Admin ID: " + this.adminId + "\n");
        sb.append("To Operator ID: " + this.operatorId + "\n");
        sb.append("Status: " + (this.accepted ? "ACCEPTED" : "NOT ACCEPTED") + "\n");
        sb.append("----------------------------------------\n");
        
        return sb.toString();
    }
    
    
    // Getters
    public int getId() {
        return id;
    }
    public Event getEvent() {
        return event;
    }
    public int getAdminId() {
        return adminId;
    }
    public int getOperatorId() {
        return operatorId;
    }
    public boolean isAccepted() {
        return accepted;
    }   


    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }


}
