package pt.uevora.sdist.monitoring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
public class Room {
    @Id
    @Column(name = "room_id", nullable = false, updatable = false)
    private String roomId;

    public Room(String roomId) {
        this.roomId = roomId;
    }
    
    protected Room() {}

    // Getter
    public String getRoomId() {
        return roomId;
    }
}