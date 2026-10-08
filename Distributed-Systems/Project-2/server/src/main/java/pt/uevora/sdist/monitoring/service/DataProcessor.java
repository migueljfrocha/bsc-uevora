package pt.uevora.sdist.monitoring.service;

import pt.uevora.sdist.monitoring.model.Device.ProtocolType;
import pt.uevora.sdist.monitoring.model.Reading;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class DataProcessor {
    
    private final DeviceService deviceService;
    private final ReadingService readingService;
    
    /**
     * Constructor for DataProcessor.
     * @param deviceService The DeviceService instance.
     * @param readingService The ReadingService instance.
     */
    public DataProcessor(DeviceService deviceService, ReadingService readingService) {
        this.deviceService = deviceService;
        this.readingService = readingService;
    }
    
    /**
     * Validate if a device is active (and exists).
     * @param deviceId The ID of the device to validate.
     * @return true if the device is active, false otherwise.
     */
    public boolean validateDevice(Long deviceId) {        
        if (!deviceService.isDeviceActive(deviceId)) {
            return false;
        }
        return true;
    }
    
    /**
     * Save a reading to the database.
     * @param deviceId The ID of the device.
     * @param temperature The temperature value.
     * @param humidity The humidity value.
     * @param timestamp The timestamp of the reading.
     * @param protocolType The protocol type used to send the reading.
     * @return true if the reading was saved successfully, false otherwise.
     */
    public boolean saveReading(
                        Long deviceId, 
                        Double temperature, 
                        Double humidity, 
                        LocalDateTime timestamp, 
                        ProtocolType protocolType) 
    {
        
        if(!isValidTemperature(temperature) || !isValidHumidity(humidity)) {
            throw new IllegalArgumentException("Invalid temperature or humidity values.");
        }
        try {
            readingService.createReading(deviceId, temperature, humidity, timestamp, protocolType);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Process a timestamp string into LocalDateTime.
     * @param timestampStr The timestamp string to process.
     * @return LocalDateTime object representing the timestamp.
     * @throws IllegalArgumentException if the timestamp string is null or blank.
     */
    public LocalDateTime processTimestamp(String timestampStr) throws IllegalArgumentException {

        LocalDateTime timestamp;
        if (timestampStr == null || timestampStr.isBlank()) {
            throw new IllegalArgumentException("Timestamp is null or blank");
        } else {
            
            try {
                timestamp = OffsetDateTime.parse(timestampStr).toLocalDateTime();
            } catch (Exception e) {
                // Fallback to local date time
                timestamp = LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        }
        return timestamp;
    }

    /**
     * Validate temperature value.
     * @param temperature The temperature value to validate.
     * @return true if valid, false otherwise.
     */
    public boolean isValidTemperature(Double temperature) {
        return temperature != null && temperature >= 15.0 && temperature <= 30.0;
    }
    
    /**
     * Validate humidity value.
     * @param humidity The humidity value to validate.
     * @return true if valid, false otherwise.
     */
    public boolean isValidHumidity(Double humidity) {
        return humidity != null && humidity >= 30.0 && humidity <= 80.0;
    }


    /**
     * Analyse performance metrics for each protocol over a specified time interval.
     * @param endTimeSeconds The time interval in seconds to look back from the current time.
     * @return A map containing performance metrics (latency, throughput, size) for MQTT, GRPC, and REST protocols.
     */
    public Map<String, Object> analysePerformance(int endTimeSeconds) {

        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusSeconds(endTimeSeconds);

        List<Reading> mqttReadings = readingService.getAllReadingsByDeviceProtocolAndTimeInterval(ProtocolType.MQTT, startTime, endTime);
        List<Reading> grpcReadings = readingService.getAllReadingsByDeviceProtocolAndTimeInterval(ProtocolType.GRPC, startTime, endTime);
        List<Reading> restReadings = readingService.getAllReadingsByDeviceProtocolAndTimeInterval(ProtocolType.REST, startTime, endTime);
        
        Double mqttLatency = calculateAverageLatency(mqttReadings);
        Double grpcLatency = calculateAverageLatency(grpcReadings);
        Double restLatency = calculateAverageLatency(restReadings);

        Double mqttThroughput = calculateThroughput(mqttReadings);
        Double grpcThroughput = calculateThroughput(grpcReadings);
        Double restThroughput = calculateThroughput(restReadings);


        Map<String, Object> performanceMetrics = Map.of(
            "mqtt", Map.of(
                "latency", mqttLatency,
                "throughputRPS", mqttThroughput,
                "size", mqttReadings.size()
            ),
            "grpc", Map.of(
                "latency", grpcLatency,
                "throughputRPS", grpcThroughput,
                "size", grpcReadings.size()
            ),
            "rest", Map.of(
                "latency", restLatency,
                "throughputRPS", restThroughput,
                "size", restReadings.size()
            )
        );
        return performanceMetrics;
    }


    /**
     * Calculate average latency from a list of readings.
     * @param readings The list of readings to calculate latency from.
     * @return The average latency in milliseconds, rounded to 4 decimal places.
     */
    private Double calculateAverageLatency(List<Reading> readings) {
        if (readings.isEmpty()) {
            return 0.0;
        }

        Double totalLatency = 0.0;

        for (Reading r : readings) {
            LocalDateTime sendingTime = r.getTimestamp();
            LocalDateTime arrivingTime = r.getArrivedTimestamp();
            totalLatency += Duration.between(sendingTime, arrivingTime).toMillis();
        }

        totalLatency = (totalLatency / readings.size());

        Double roundedLatency = BigDecimal.valueOf(totalLatency)
                                    .setScale(4, RoundingMode.HALF_UP)
                                    .doubleValue();

        return roundedLatency;
    }

    /**
     * Calculate throughput from a list of readings.
     * @param readings The list of readings to calculate throughput from.
     * @return The throughput in readings per second, rounded to 4 decimal places.
     */
    private Double calculateThroughput(List<Reading> readings) {
        if (readings.isEmpty()) {
            return 0.0;
        }

        LocalDateTime MIN = LocalDateTime.MAX;
        LocalDateTime MAX = LocalDateTime.MIN;

        for (Reading r : readings) {
            LocalDateTime arrivingTime = r.getArrivedTimestamp();
            if (arrivingTime.isBefore(MIN)){
                MIN = arrivingTime;
            }
            if (arrivingTime.isAfter(MAX)){
                MAX = arrivingTime;
            }
        }

        Long timeIntervalMillis = Duration.between(MIN, MAX).toMillis();
        if (timeIntervalMillis <= 0) {
            return 0.0; // To avoid division by zero
        }

        Double timeIntervalSeconds = timeIntervalMillis / 1000.0;
        
        Double throughput = readings.size() / timeIntervalSeconds;
        Double roundedThroughput = BigDecimal.valueOf(throughput)
                                    .setScale(4, RoundingMode.HALF_UP)
                                    .doubleValue();
        return roundedThroughput;
    }
}