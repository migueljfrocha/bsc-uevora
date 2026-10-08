package pt.uevora.sdist.monitoring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
public class Floor {
    @Id
    @Column(name = "floor_id", nullable = false, updatable = false)
    private String floorId;

    public Floor(String floorId) {
        this.floorId = floorId;
    }
    
    protected Floor() {}

    // Getters
    public String getFloorId() {
        return floorId;
    }
}