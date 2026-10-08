package eventastic;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;


import org.junit.jupiter.api.*;

import eventastic.utils.DateInterval;
import eventastic.utils.Local;
import eventastic.event.AdditionalOption;
import eventastic.event.Event;
import eventastic.users.Admin;


public class EventTest {

    private Admin admin;
    private Local local;
    private DateInterval eventInterval;
    private DateInterval registrationInterval;

    private ArrayList<AdditionalOption> addons;

    @BeforeEach
    public void setup() {

        admin = new Admin(1, "Admin Name", "admin@example.com", "928371228", "Street 123");
        local = new Local("LocalXXX", "123 Main St", "12345", "CityName", "CountryName");
        
        LocalDateTime eventStart = LocalDateTime.of(2026, 12, 1, 10, 0);
        LocalDateTime eventEnd = LocalDateTime.of(2026, 12, 1, 18, 0);
        eventInterval = new DateInterval(eventStart, eventEnd);
        
        LocalDateTime regStart = LocalDateTime.of(2026, 11, 1, 9, 0);
        LocalDateTime regEnd = LocalDateTime.of(2026, 11, 30, 23, 59);
        registrationInterval = new DateInterval(regStart, regEnd);

        addons = new ArrayList<>();
        addons.add(new AdditionalOption("additional1", "Extra option 1", new BigDecimal("20.0"), false));
    }


    /**
     * Tests the creation of an event with valid parameters.
     */
    @Test
    public void testEventCreation() {
        Event event = new Event(
            1, admin,
            "Conference", "Amazing conference", local, 500,
            eventInterval, registrationInterval,
            new ArrayList<>(), new ArrayList<>()
        );
        
        Assertions.assertEquals(1, event.getId());
        Assertions.assertEquals("Conference", event.getName());
        Assertions.assertEquals("Amazing conference", event.getDescription());
        Assertions.assertEquals(local, event.getLocal());
        Assertions.assertEquals(500, event.getnMaxParticipants());
        Assertions.assertEquals(Event.EventStatus.TO_BE_STARTED, event.getStatus());
    }
    

    /**
     * Tests updating event basic information.
     */
    @Test
    public void testEventUpdate() {
        Event event = new Event(
            1, admin,
            "Conference", "Amazing conference", local, 500,
            eventInterval, registrationInterval,
            new ArrayList<>(), new ArrayList<>()
        );

        Local newlocal = new Local("Updated", "125 Main St", "12345678", "City123", "Country123");
        event.editBasicInfo("Updated Conference", "Updated description", newlocal, 600);
        
        Assertions.assertEquals("Updated Conference", event.getName());
        Assertions.assertEquals("Updated description", event.getDescription());
        Assertions.assertEquals(newlocal, event.getLocal());
        Assertions.assertEquals(600, event.getnMaxParticipants());
    }

    
    /**
     * Tests event cancellation.
     */
    @Test
    public void testEventCancellation() {
        Event event = new Event(
            1, admin,
            "Conference", "Amazing conference", local, 500,
            eventInterval, registrationInterval,
            new ArrayList<>(), new ArrayList<>()
        );
        event.cancelEvent();
        Assertions.assertEquals(Event.EventStatus.CANCELLED, event.getStatus());
    }


    /**
     * Tests invalid date intervals during event creation.
     */
    @Test
    public void testInvalidDateIntervals() {
        LocalDateTime eventStart = LocalDateTime.of(2026, 12, 1, 10, 0);
        LocalDateTime eventEnd = LocalDateTime.of(2026, 12, 1, 18, 0);
        DateInterval invalidEventInterval = new DateInterval(eventStart, eventEnd);
        
        // Registration end after event end (invalid)
        LocalDateTime regStart = LocalDateTime.of(2026, 11, 1, 9, 0);
        LocalDateTime regEnd = LocalDateTime.of(2026, 12, 2, 23, 59);
        DateInterval invalidRegistrationInterval = new DateInterval(regStart, regEnd);
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Event(
                1, admin,
                "Concert", "Amazing concert event", local, 500,
                invalidEventInterval, invalidRegistrationInterval,
                new ArrayList<>(), new ArrayList<>()
            );
        });
    }
}