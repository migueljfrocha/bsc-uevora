package pt.uevora.sdist.monitoring.restmanager;

import pt.uevora.sdist.monitoring.utils.SensorDataGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;


public class Sensor {

    private static final int MAX_ATTEMPTS = 5;
    private static final int backOffMillis = 1000;

    private final SensorDataGenerator dataGenerator;
    private final String ingestEndpoint;
    private final Long id;
    private final int secondsPerReading; 

    private final HttpClient httpClient;

    private final ScheduledExecutorService scheduledExecutor;
    private ScheduledFuture<?> scheduledFuture;  


    /**
     * Constructor for Sensor.
     * @param id Sensor ID
     * @param secondsPerReading Interval in seconds between sensor readings
     * @param ingestEndpoint REST endpoint for data ingestion
     * @param httpClient HTTP client for sending requests
     * @param scheduledExecutor Executor for scheduling sensor readings
     */
    public Sensor(
                Long id,
                int secondsPerReading,
                String ingestEndpoint,
                HttpClient httpClient,
                ScheduledExecutorService scheduledExecutor) {
                    
        this.id = id;
        this.secondsPerReading = secondsPerReading;
        this.ingestEndpoint = ingestEndpoint;
        this.httpClient = httpClient;
        this.scheduledExecutor = scheduledExecutor;

        this.dataGenerator = new SensorDataGenerator(secondsPerReading);
    }


    /**
     * Starts the sensor to periodically publish readings.
     */
    public void start() {
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            // Schedule periodic reading publication
            scheduledFuture = scheduledExecutor.scheduleAtFixedRate(
                () -> makeRequest(),
                0, 
                secondsPerReading, 
                TimeUnit.SECONDS
            );
        }
    }

    /**
     * Generates sensor data and sends it to the ingest endpoint to be processed.
     */
    public void makeRequest(){
        
        try{
            Double temperature = dataGenerator.getNextTemperature();
            Double humidity = dataGenerator.getNextHumidity();
            String timestamp = LocalDateTime.now().toString();
            
            Double roundedTemp = BigDecimal.valueOf(temperature)
                                    .setScale(4, RoundingMode.HALF_UP)
                                    .doubleValue();
            Double roundedHum = BigDecimal.valueOf(humidity)
                                    .setScale(4, RoundingMode.HALF_UP)
                                    .doubleValue();

            JSONObject json = new JSONObject();
            json.put("deviceId", id);
            json.put("temperature", roundedTemp);
            json.put("humidity", roundedHum);
            json.put("timestamp", timestamp.toString());


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ingestEndpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();
            
            sendRequest(request);

        } catch(Exception e){
            System.err.println("Error publishing sensor " + id + " reading: " + e.getMessage());
        }
        
    }

    /**
     * Sends an HTTP request with retry logic (linear backoff).
     * @param request The HTTP request to send
     */
    private void sendRequest(HttpRequest request) {
        boolean success = false;
        int attempt = 0;
            do {
                try {
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    int status = response.statusCode();
                    if (status >= 200 && status < 300) {
                        success = true;
                    } else {
                        if (attempt < MAX_ATTEMPTS) {
                            Thread.sleep(backOffMillis * (attempt + 1));
                        }
                    }
                } catch (Exception e) {
                    if (attempt < MAX_ATTEMPTS) {
                        try { 
                            Thread.sleep(backOffMillis * (attempt + 1)); 
                        } catch (InterruptedException ie) { 
                            Thread.currentThread().interrupt(); 
                            break; 
                        }
                    }
                }
                attempt++;

            } while (!success && attempt < MAX_ATTEMPTS);
        }
    

    /**     
     * Stops the sensor from publishing readings.
     */
    public void stop() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    // Getters

    public Long getId() {
        return id;
    }

    public int getSecondsPerReading() {
        return secondsPerReading;
    }


}