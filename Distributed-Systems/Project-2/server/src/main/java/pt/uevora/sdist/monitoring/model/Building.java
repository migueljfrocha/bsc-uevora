package pt.uevora.sdist.monitoring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
public class Building {
    @Id
    @Column(name = "building_id", nullable = false, updatable = false)
    private String buildingId;

    public Building(String buildingId) {
        this.buildingId = buildingId;
    }
    
    protected Building() {}

    // Getter
    public String getBuildingId() {
        return buildingId;
    }
}