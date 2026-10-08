package pt.uevora.sdist.monitoring.model;

public class ReadingDTO {
    private Long id;
    private Long deviceId;
    private Double temperature;
    private Double humidity;
    private String timestamp;

    public ReadingDTO() {
    }

    public ReadingDTO(Long id, Long deviceId, Double temperature, Double humidity, String timestamp) {
        this.id = id;
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
    }
    
    public ReadingDTO(Long deviceId, Double temperature, Double humidity, String timestamp) {
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
    }

    // Getters
    public Long getId() {
        return this.id;
    }
    public Long getDeviceId() {
        return this.deviceId;
    }
    public Double getTemperature() {
        return this.temperature;
    }
    public Double getHumidity() {
        return this.humidity;
    }
    public String getTimestamp() {
        return this.timestamp;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}