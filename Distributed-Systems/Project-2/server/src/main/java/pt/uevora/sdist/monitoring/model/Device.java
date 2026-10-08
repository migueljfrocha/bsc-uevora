package pt.uevora.sdist.monitoring.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name = "devices")

public class Device {

    public enum ProtocolType {
        MQTT, GRPC, REST
    }

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "protocol", nullable = false, length = 16)
    private ProtocolType protocol;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(name = "state", nullable = false)
    private Boolean state;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Device(ProtocolType protocol, Room room, Department department, Floor floor, Building building, Boolean state) {
        this.protocol = protocol;
        this.room = room;
        this.department = department;
        this.floor = floor;
        this.building = building;
        this.state = state;
    }

    protected Device() {}
    

    // Getters
    public Long getId() {
        return id;
    }
    public ProtocolType getProtocol() {
        return protocol;
    }
    public Room getRoom() {
        return room;
    }
    public Department getDepartment() {
        return department;
    }
    public Floor getFloor() {
        return floor;
    }
    public Building getBuilding() {
        return building;
    }
    public Boolean getState() {
        return state;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setProtocol(ProtocolType protocol) {
        this.protocol = protocol;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
    public void setFloor(Floor floor) {
        this.floor = floor;
    }
    public void setBuilding(Building building) {
        this.building = building;
    }
    public void setState(Boolean state) {
        this.state = state;
    }

}

