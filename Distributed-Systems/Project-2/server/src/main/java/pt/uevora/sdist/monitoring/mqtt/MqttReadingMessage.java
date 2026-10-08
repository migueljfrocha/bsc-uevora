package pt.uevora.sdist.monitoring.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;


public class MqttReadingMessage {

    @JsonProperty("deviceId")
    private Long deviceId;

    @JsonProperty("temperature")
    private Double temperature;

    @JsonProperty("humidity")
    private Double humidity;

    @JsonProperty("timestamp")
    private String timestamp;

    public MqttReadingMessage() {}

    //Getters
    public Long getDeviceId() {
        return deviceId; 
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public String getTimestamp() {
        return timestamp; 
    }

    //Setters
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