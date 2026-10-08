package pt.uevora.sdist.monitoring.controller;

import pt.uevora.sdist.monitoring.service.ReadingService;
import pt.uevora.sdist.monitoring.service.DataProcessor;
import pt.uevora.sdist.monitoring.model.AverageReading;
import pt.uevora.sdist.monitoring.model.Reading;
import pt.uevora.sdist.monitoring.model.ReadingDTO;
import pt.uevora.sdist.monitoring.model.Device.ProtocolType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metrics")
public class ReadingController {

    private final ReadingService readingService;
    private final DataProcessor dataProcessor;


    /**
     * Constructor for ReadingController
     * @param readingService The service handling reading operations
     * @param dataProcessor
     */
    public ReadingController(ReadingService readingService, DataProcessor dataProcessor) {
        this.readingService = readingService;
        this.dataProcessor = dataProcessor;
    }
    

    /**
     * Ingest a new reading 
     * @param readingDTO The reading data transfer object containing reading details
     * @return A ResponseEntity containing the result of the ingestion
     */
    @PostMapping("/ingest")
    public ResponseEntity<Map<String, Object>> ingestReading(
        @RequestBody ReadingDTO readingDTO
        ) {
            
        Long deviceId = readingDTO.getDeviceId();
        Double temperature = readingDTO.getTemperature();
        Double humidity = readingDTO.getHumidity();
        String timestampStr = readingDTO.getTimestamp();
            
        if (!dataProcessor.validateDevice(deviceId)) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "message", "Device not registered or inactive: " + deviceId
                ));
        }

        LocalDateTime timestamp;
        try {
            timestamp = dataProcessor.processTimestamp(timestampStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "message", "Invalid timestamp format: " + timestampStr
                ));
        }

        Boolean success = dataProcessor.saveReading(deviceId, temperature, humidity, timestamp, ProtocolType.REST);
        
        if (!success) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "message", "Failed to process data for device: " + deviceId
                ));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Data processed successfully for device: " + deviceId
        ));
    }


    /**
     * Retrieve average metrics based on the specified level and ID within an optional time range.
     * @param level The level of aggregation (e.g., sala, departamento, piso, edificio)
     * @param id The identifier for the specified level
     * @param from The start timestamp for the range (optional)
     * @param to The end timestamp for the range (optional)
     * @return A ResponseEntity containing the average metrics or an error message
     */
    @GetMapping("/average")
    public ResponseEntity<Map<String, Object>> getAverageMetrics(
        @RequestParam String level,
        @RequestParam String id,
        @RequestParam(required = false) String from,
        @RequestParam(required = false) String to
    ) {
        LocalDateTime fromTimestamp = null;
        LocalDateTime toTimestamp = null;

        try {
            if(from == null && to == null) {
                toTimestamp = LocalDateTime.now();
                fromTimestamp = toTimestamp.minusHours(24);
            } else {
                fromTimestamp = dataProcessor.processTimestamp(from);
                toTimestamp = dataProcessor.processTimestamp(to);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "message", "Invalid timestamp format in 'from' or 'to' parameter"
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "message", "Error processing request"
                ));
        }

        AverageReading averageMetrics = null;
        try {
            switch (level) {
                case "sala":
                    averageMetrics = readingService.getAverageReadingsByRoomId(id, fromTimestamp, toTimestamp);
                    break;

                case "departamento":
                    averageMetrics = readingService.getAverageReadingsByDepartmentId(id, fromTimestamp, toTimestamp);
                    break;

                case "piso":
                    averageMetrics = readingService.getAverageReadingsByFloorId(id, fromTimestamp, toTimestamp);
                    break;
                    
                case "edificio":
                    averageMetrics = readingService.getAverageReadingsByBuildingId(id, fromTimestamp, toTimestamp);
                    break;

                default:
                    return ResponseEntity.badRequest()
                        .body(Map.of(
                            "success", false,
                            "message", "Invalid level parameter. Must be one of: sala, departamento, piso, edificio"
                        ));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "message", e.getMessage()
                ));
        }

        
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Average metrics retrieved successfully",
                "data", averageMetrics
            ));
    }

    
    /**
     * Get raw metrics for a specific device within an optional time range.
     * @param deviceId The ID of the device
     * @param from The start timestamp for the range (optional)
     * @param to The end timestamp for the range (optional)
     * @return A ResponseEntity containing the raw metrics or an error message
     */
    @GetMapping("/raw")
    public ResponseEntity<Map<String, Object>> getRawMetrics(
        @RequestParam Long deviceId,
        @RequestParam(required = false) String from,
        @RequestParam(required = false) String to
    ) {
        LocalDateTime fromTimestamp = null;
        LocalDateTime toTimestamp = null;

 
        try {
            if(from == null && to == null) {
                toTimestamp = LocalDateTime.now();
                fromTimestamp = toTimestamp.minusHours(24);
            } else {
                fromTimestamp = dataProcessor.processTimestamp(from);
                toTimestamp = dataProcessor.processTimestamp(to);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "message", "Invalid timestamp format in 'from' or 'to' parameter"
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "message", "Error processing request"
                ));
        }

        List<Reading> rawMetrics = readingService.getRawReadingsByDeviceId(deviceId, fromTimestamp, toTimestamp);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Raw metrics retrieved successfully",
            "data", rawMetrics
        ));
    }

    
    /**
     * Get raw metrics by level and id.
     * @param level The level of the location (e.g., sala, departamento, piso, edificio)
     * @param id The ID of the location
     * @param from The start timestamp for the range (optional)
     * @param to The end timestamp for the range (optional)
     * @return A ResponseEntity containing the raw metrics or an error message
     */
    @GetMapping("/raw-level")
    public ResponseEntity<Map<String, Object>> getRawMetricsByLevel(
        @RequestParam String level,
        @RequestParam String id,
        @RequestParam(required = false) String from,
        @RequestParam(required = false) String to
    ) {
        LocalDateTime fromTimestamp = null;
        LocalDateTime toTimestamp = null;

        try {
            if(from == null && to == null) {
                toTimestamp = LocalDateTime.now();
                fromTimestamp = toTimestamp.minusHours(24);
            } else {
                fromTimestamp = dataProcessor.processTimestamp(from);
                toTimestamp = dataProcessor.processTimestamp(to);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "message", "Invalid timestamp format in 'from' or 'to' parameter"
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "message", "Error processing request"
                ));
        }

        List<Reading> rawMetrics = null;

        switch (level) {
            case "sala":
                rawMetrics = readingService.getRawReadingsByRoomId(id, fromTimestamp, toTimestamp);
                break;

            case "departamento":
                rawMetrics = readingService.getRawReadingsByDepartmentId(id, fromTimestamp, toTimestamp);
                break;
                
            case "piso":
                rawMetrics = readingService.getRawReadingsByFloorId(id, fromTimestamp, toTimestamp);
                break;

            case "edificio":
                rawMetrics = readingService.getRawReadingsByBuildingId(id, fromTimestamp, toTimestamp);
                break;

            default:
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "success", false,
                        "message", "Invalid level parameter. Must be one of: sala, departamento"
                    ));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Raw metrics retrieved successfully",
            "data", rawMetrics
        ));
    }



    //Server Statistics


    /**
     * Get total readings count, broken down by protocol type.
     * @return A ResponseEntity containing the total readings count
     */
    @GetMapping("/stats/total")
    public ResponseEntity<Map<String, Object>> getTotalReadingsCount() {
        long totalReadings = readingService.getTotalReadingsCount();

        long mqttReadings = readingService.getTotalReadingsCountByDeviceProtocol(ProtocolType.MQTT);
        long grpcReadings = readingService.getTotalReadingsCountByDeviceProtocol(ProtocolType.GRPC);
        long restReadings = readingService.getTotalReadingsCountByDeviceProtocol(ProtocolType.REST);
        
        Map<String, Long> count = Map.of(
            "total", totalReadings,
            "mqtt", mqttReadings,
            "grpc", grpcReadings,
            "rest", restReadings
        );

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Total readings count retrieved successfully",
            "data", count
        ));
    }
    

    // Performance Analysis


    /**
     * Get performance metrics for each protocol over a specified time interval.
     * 
     * The return of this method is a map with the following structure:
     * {
     *  <protocol>: {
     *      "latency": <average_latency_in_ms>,
     *      "throughputRPS": <throughput_in_requests_per_second>,
     *      "size": <Number of readings used>
     *  },
     *  ...
     * }
     * @param timeSeconds The amount of time in seconds to look back from the current time
     * @return ResponseEntity with performance metrics (latency and throughput) for each protocol
     */
    @GetMapping("/stats/performance/{time}")
    public ResponseEntity<Map<String, Object>> getPerformanceMetrics(
        @PathVariable("time") int timeSeconds
    ) {
        
        Map<String, Object> performanceMetrics = dataProcessor.analysePerformance(timeSeconds);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Performance metrics retrieved successfully",
            "data", performanceMetrics
        ));
    }
}
