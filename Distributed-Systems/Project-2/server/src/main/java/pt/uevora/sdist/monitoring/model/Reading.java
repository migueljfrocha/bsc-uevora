package pt.uevora.sdist.monitoring.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name = "readings")
public class Reading {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "temperature", nullable = false, length = 100)
    private Double temperature;

    @Column(name = "humidity", nullable = false, length = 100)
    private Double humidity;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "arrived_timestamp", nullable = false)
    private LocalDateTime arrivedTimestamp;


    public Reading(Device device, Double temperature, Double humidity, LocalDateTime timestamp) {
        this.device = device;
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
        this.arrivedTimestamp = LocalDateTime.now();
    }

    protected Reading() {}

    // Getters
    public Long getId() {
        return id;
    }
    public Device getDevice() {
        return device;
    }
    public Double getTemperature() {
        return temperature;
    }
    public Double getHumidity() {
        return humidity;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getArrivedTimestamp() {
        return arrivedTimestamp;
    }

    // Setters
    public void setDevice(Device device) {
        this.device = device;
    }
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public void setArrivedTimestamp(LocalDateTime arrivedTimestamp) {
        this.arrivedTimestamp = arrivedTimestamp;
    }
}
