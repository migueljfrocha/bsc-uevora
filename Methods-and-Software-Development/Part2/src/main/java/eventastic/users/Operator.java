package eventastic.users;

import java.util.List;

import eventastic.utils.Invite;
import eventastic.event.Event;

import java.util.ArrayList;

public class Operator extends Staff {
    private List<Invite> invites = new ArrayList<>();
    
    // Constructor
    public Operator(int id, String name, String email, String phoneNumber, String address) {
        super(id, name, email, phoneNumber, address);
        this.invites = new ArrayList<>();
    }


    /**
     * Gets all events associated with this operator from a list of events.
     * @param allEvents List<Event> - List of all events.
     * @return List<Event> - List of events associated with this operator.
     */
    public List<Event> getMyEvents(List<Event> allEvents) {
        List<Event> myEvents = new ArrayList<>();

        for (Event event : allEvents) {
            for (Operator op : event.getOperators()) {
                if (op.getId() == this.getId()) {
                    myEvents.add(event);
                }
            }
        }

        return myEvents;
    }


    /**
     * Receives an invite.
     * @param invite Invite - The invite to be received.
     */
    public void receiveInvite(Invite invite) {
        invite.setId(invites.size() + 1);
        this.invites.add(invite);
    }
    

    /**
     * Accepts an invite by its ID.
     * @param inviteId int - ID of the invite to accept.
     * @throws IllegalArgumentException if the invite is not found or already accepted.
     */
    public void acceptInvite(int inviteId) {
        Invite inviteToAccept = null;
        for (Invite invite : invites) {
            if (invite.getId() == inviteId && !invite.isAccepted()) {
                inviteToAccept = invite;
                break;
            }
        }
        if (inviteToAccept == null) {
            throw new IllegalArgumentException("Invite with ID " + inviteId + " not found or already accepted.");
        }
        inviteToAccept.setAccepted(true);
        inviteToAccept.getEvent().addOperator(this);
    }
    

    /**
     * Declines an invite by its ID.
     * @param inviteId int - ID of the invite to decline.
     * @throws IllegalArgumentException if the invite is not found or already accepted.
     */
    public void declineInvite(int inviteId) {
        Invite inviteToDecline = null;
        for (Invite invite : invites) {
            if (invite.getId() == inviteId && !invite.isAccepted()) {
                inviteToDecline = invite;
                break;
            }
        }
        if (inviteToDecline == null) {
            throw new IllegalArgumentException("Invite with ID " + inviteId + " not found or already accepted.");
        }
        inviteToDecline.setAccepted(false);
    }


    /**
     * Gets all pending invites for this operator.
     * @return List<Invite> - List of pending invites.
     */
    public List<Invite> getInvites() {
        List<Invite> notAccInvites = new ArrayList<>();
        for(Invite invite : invites){
            if(!invite.isAccepted()){
                notAccInvites.add(invite);
            }
        }
        return notAccInvites;
    }
}