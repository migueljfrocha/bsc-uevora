import java.time.LocalDateTime;
import java.util.List;

import eventastic.Eventastic;

import eventastic.utils.*;
import eventastic.event.*;
import eventastic.payment.*;
import eventastic.users.*;

import java.math.BigDecimal;


public class Main {
    
    public static void main(String[] args) {
        Eventastic eventastic = new Eventastic();

        // Admin population
        Admin admin1 = eventastic.registerAdmin("Luís Roberto", "luisr@gmail.com", "+351760100120", "Rua A, 123");
        Admin admin2 = eventastic.registerAdmin("João Matias", "jotam@gmail.com", "+351761100900", "Rua B, 456");
        Admin admin3 = eventastic.registerAdmin("Maria Santos", "marias@gmail.com", "+351761100300", "Rua C, 789");

        // Operator population
        Operator operator1 = eventastic.registerOperator("Ana Paula", "anap@gmail.com", "+351761201020", "Rua D, 101");
        Operator operator2 = eventastic.registerOperator("Carlos Tiago", "carlost@gmail.com", "+351761100100", "Rua E, 202");

        // Create Events
        Event event1 = createEvent1(eventastic, admin1);
        Event event2 = createEvent2(eventastic, admin2);

        // Show Event Cancellation Flow
        System.out.println("\n############### EVENT CANCELLATION FLOW ###############\n");
        showEventCancelFlow(eventastic, admin1, admin2, event1);


        // Show Operator Invitation Flow
        System.out.println("\n############### OPERATOR INVITATION FLOW ###############\n");
        showOperatorFlow(eventastic, admin1, operator1);

        // Show Participant Registration
        System.out.println("\n############### PARTICIPANT REGISTRATION FLOW ###############\n");
        showParticipantRegistrationFlow(eventastic, admin1, event1);

        // Show Export Participant Registrations Flow
        System.out.println("\n############### EXPORT PARTICIPANT REGISTRATIONS FLOW ###############\n");
        String exportFilePath = "Event1_Participants.txt";
        eventastic.exportParticipantRegistrations(admin1, event1, exportFilePath);
        System.out.println("Participant registrations for Event ID " + event1.getId() + " exported to " + exportFilePath + "\n");

    }


    /**
     * Shows the participant registration flow.
     */
    public static void showParticipantRegistrationFlow(Eventastic eventastic, Admin admin1, Event event1) {
        Participant participant1 = eventastic.createParticipant(ParticipantType.STUDENT, "Marta Silva", "marta.s@email.com", "+351912345678", "Av. Exemplo, 123");
        Participant participant2 = eventastic.createParticipant(ParticipantType.NON_STUDENT, "Adalberto Fernando Maçãs", "af@email.com", "+351912345679", "Av. Exemplo, 567");

        System.out.println("\nAddons for Event 1: ");
        for(AdditionalOption a : event1.getAddons()) {
            System.out.println(a);
        }

        System.out.println("\nParticipant 1 Registration:");
        List<AdditionalOption> participant1Addons = List.of(event1.getAddons().get(0),
                                                            event1.getAddons().get(1));
        Payment payment1 = eventastic.registerParticipantToEvent(participant1, participant1Addons, event1);
        System.out.println(payment1);

        System.out.println("\nParticipant 2 Registration:");
        List<AdditionalOption> participant2Addons = List.of(event1.getAddons().get(1));
        Payment payment2 = eventastic.registerParticipantToEvent(participant2, participant2Addons, event1);
        System.out.println(payment2);

        System.out.println("\nSearching Participants by Email");
        List<Participant> searchedParticipants = eventastic.searchParticipantsByEmail(admin1, event1, "marta");
        for(Participant p : searchedParticipants) {
            System.out.println(p);
        }
        
        System.out.println("\nListing participants for Event 1:");
        for(Participant p : eventastic.listParticipantsOfEvent(admin1, event1)) {
            System.out.println(p);
        }
    }
        

    /**
     * Shows the operator invitation, acceptance, listing and removal flow.
     */
    public static void showOperatorFlow(Eventastic eventastic, Admin admin1, Operator operator1){
        List<Event> adminEvents = eventastic.listAdminEvents(admin1);
        Event eventToInvite = adminEvents.get(0);

        eventastic.inviteOperatorToEvent(admin1, operator1.getId(), eventToInvite.getId());

        System.out.println("Operator Invites:");
        for(Invite i : eventastic.listOperatorInvites(operator1)) {
            System.out.println(i);
        }
        eventastic.acceptOperatorInvite(operator1, eventToInvite.getId());

        System.out.println("Operator Events:");
        for(Event e : eventastic.listOperatorEvents(operator1)) {
            System.out.println(e);
        }

        System.out.println("Removing Operator from Event...");
        eventastic.removeOperatorFromEvent(admin1, operator1.getId(), eventToInvite.getId());

        System.out.println("Operator Events (After Removal):");
        for(Event e : eventastic.listOperatorEvents(operator1)) {
            System.out.println(e);
        }
    }


    /**
     * Shows the event cancellation.
     */
    public static void showEventCancelFlow(Eventastic eventastic, Admin admin1, Admin admin2, Event eventToCancel) {
        // List All Events
        System.out.println("All Events:");
        for(Event e : eventastic.listAvailableEvents()) {
            System.out.println(e);
        }
        
        // Cancel Event1
        eventastic.cancelEvent(admin1, eventToCancel);

        // List All Events after cancellation
        System.out.println("All Events (After 1 cancelled):");
        for(Event e : eventastic.listAvailableEvents()) {
            System.out.println(e);
        }

        // List Events of Admins
        List<Event> le = eventastic.listAdminEvents(admin1);
        System.out.println("Admin1 Events:");
        for(Event e : le) {
            System.out.println(e);
        }

        le = eventastic.listAdminEvents(admin2);
        System.out.println("Admin2 Events:");
        for(Event e : le) {
            System.out.println(e);
        }
    }
    

    /**
     * Creates Event 1 with predefined data.
     */
    public static Event createEvent1(Eventastic eventastic, Admin admin) {
        
        Local eventLocation1 = new Local("PACT", "R. Luís Adelino Fonseca 1A","7005-345", "Évora", "Portugal");


        LocalDateTime now = LocalDateTime.now();

        DateInterval event1Interval = new DateInterval(
            now, 
            now.plusDays(5));
            
        DateInterval event1Registration = new DateInterval(
            now.minusDays(30), 
            now.plusDays(2));
        
        
        

        RegistrationPhase event1Phase1 = new RegistrationPhase(
                                            "Early Bird", 
                                            new DateInterval(now.minusDays(30),
                                            now.minusDays(10)), 
                                            List.of(new PhasePrice(new BigDecimal("100.00"), ParticipantType.NON_STUDENT),
                                                                new PhasePrice(new BigDecimal("40.00"), ParticipantType.STUDENT))
                                            );
        RegistrationPhase event1Phase2 = new RegistrationPhase(
                                            "Late Registration", 
                                            new DateInterval(now.minusDays(9),
                                            now.minusSeconds(1)), 
                                            List.of(new PhasePrice(new BigDecimal("150.00"), ParticipantType.NON_STUDENT),
                                                                new PhasePrice(new BigDecimal("70.00"), ParticipantType.STUDENT))
                                            );
        RegistrationPhase event1Phase3 = new RegistrationPhase(
                                            "On Site", 
                                            new DateInterval(
                                            now,
                                            now.plusDays(4)), 
                                            List.of(new PhasePrice(new BigDecimal("200.00"), ParticipantType.NON_STUDENT),
                                                                new PhasePrice(new BigDecimal("70.00"), ParticipantType.STUDENT))
                                        );
        
        List<RegistrationPhase> event1Phases = List.of(event1Phase1, event1Phase2, event1Phase3);


        List<AdditionalOption> event1Addons = List.of(
            new AdditionalOption("Network Dinner", "Formal dinner event with keynote speakers.", new BigDecimal("80.00"), false),
            new AdditionalOption("Workshop Access", "Access to all workshops.", new BigDecimal("50.00"), false)
        );

        Event event1 = eventastic.createEvent(
            admin,
            "Tech Conference 2026",
            "A conference bringing together professionals from PACT and key industry leaders from around the world.",
            eventLocation1,
            200,
            event1Interval,
            event1Registration,
            event1Phases,
            event1Addons
        );

        return event1;
        
    }


    /**
     * Creates Event 2 with predefined data.
     */
    public static Event createEvent2(Eventastic eventastic, Admin admin) {
        
        Local eventLocation2 = new Local("CLAV - UÉvora", "R. Romão Ramalho 59","7002-554", "Évora", "Portugal");

        DateInterval event2Interval = new DateInterval(
            LocalDateTime.of(2026, 07, 20, 0, 0), 
            LocalDateTime.of(2026, 07, 22, 0, 0));
            
        DateInterval event2Registration = new DateInterval(
            LocalDateTime.of(2026, 03, 01, 0, 0), 
            LocalDateTime.of(2026, 07, 19, 23, 59));
        
        
    
        RegistrationPhase event2Phase1 = new RegistrationPhase(
                                            "Super Early Bird", 
                                            new DateInterval(LocalDateTime.of(2026, 03, 01, 0, 0),
                                            LocalDateTime.of(2026, 04, 30, 23, 59)), 
                                            List.of(new PhasePrice(new BigDecimal("85.00"), ParticipantType.NON_STUDENT),
                                                                new PhasePrice(new BigDecimal("35.00"), ParticipantType.STUDENT))
                                            );
        RegistrationPhase event2Phase2 = new RegistrationPhase(
                                            "Regular Registration", 
                                            new DateInterval(LocalDateTime.of(2026, 05, 01, 0, 0),
                                            LocalDateTime.of(2026, 06, 30, 23, 59)), 
                                            List.of(new PhasePrice(new BigDecimal("130.00"), ParticipantType.NON_STUDENT),
                                                                new PhasePrice(new BigDecimal("60.00"), ParticipantType.STUDENT))
                                            );
        RegistrationPhase event2Phase3 = new RegistrationPhase(
                                            "Late Registration", 
                                            new DateInterval(LocalDateTime.of(2026, 07, 01, 0, 0),
                                            LocalDateTime.of(2026, 07, 19, 23, 59)), 
                                            List.of(new PhasePrice(new BigDecimal("190.00"), ParticipantType.NON_STUDENT),
                                                                new PhasePrice(new BigDecimal("90.00"), ParticipantType.STUDENT))
                                        );
        
        List<RegistrationPhase> event2Phases = List.of(event2Phase1, event2Phase2, event2Phase3);


        List<AdditionalOption> event2Addons = List.of(
            new AdditionalOption("AI Workshop", "Hands-on workshop on Artificial Intelligence and Machine Learning.", new BigDecimal("95.00"), false),
            new AdditionalOption("Innovation Lab Tour", "Exclusive tour of UÉvora's innovation laboratories.", new BigDecimal("30.00"), false)
        );

        Event event2 = eventastic.createEvent(
            admin,
            "Digital Innovation Summit 2026",
            "International summit on emerging technologies, digital transformation, and scientific innovation in the digital age.",
            eventLocation2,
            150,
            event2Interval,
            event2Registration,
            event2Phases,
            event2Addons
        );

        return event2;
        
    }

}