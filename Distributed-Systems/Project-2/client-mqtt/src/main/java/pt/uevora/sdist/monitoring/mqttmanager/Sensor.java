package pt.uevora.sdist.monitoring.mqttmanager;

import pt.uevora.sdist.monitoring.utils.SensorDataGenerator;
import pt.uevora.sdist.monitoring.model.Publication;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Sensor {

    private final SensorDataGenerator dataGenerator;
    private final Long id;
    private final String topic;
    private final int secondsPerReading; 

    private final ScheduledExecutorService scheduledExecutor;
    private final BlockingQueue<Publication> mqttQueue;
    private ScheduledFuture<?> scheduledFuture;
    

    /**
     * Constructs a Sensor instance.
     * @param id Sensor ID
     * @param secondsPerReading Interval between readings in seconds
     * @param topic MQTT topic for publishing readings
     * @param mqttQueue BlockingQueue to publish messages to
     * @param scheduledExecutor ScheduledExecutorService for scheduling periodic tasks
     */
    public Sensor(
                Long id,
                int secondsPerReading, 
                String topic,
                BlockingQueue<Publication> mqttQueue,
                ScheduledExecutorService scheduledExecutor) {
                    
        this.id = id;
        this.secondsPerReading = secondsPerReading;
        this.topic = topic;
        this.mqttQueue = mqttQueue;
        this.scheduledExecutor = scheduledExecutor;

        this.dataGenerator = new SensorDataGenerator(secondsPerReading);
    }


    /**
     * Starts the periodic publication of readings.
     */
    public void start() {
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            // Schedule periodic reading publication
            scheduledFuture = scheduledExecutor.scheduleAtFixedRate(
                () -> publishReading(),
                0, 
                secondsPerReading, 
                TimeUnit.SECONDS
            );
        }
    }

    
    /**
     * Publishes a sensor reading to the MQTT queue.
     * Rounds temperature and humidity to 4 decimal places.
     */
    public void publishReading(){
        
        try{
            Double temperature = dataGenerator.getNextTemperature();
            Double humidity = dataGenerator.getNextHumidity();
            LocalDateTime timestamp = LocalDateTime.now();
            
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

            byte[] message = json.toString().getBytes();

            mqttQueue.offer(
                new Publication(
                    topic,
                    message
                )
            );
        } catch(Exception e){
            System.err.println("Error publishing sensor " + id + " reading: " + e.getMessage());
        }
        
    }

    /**
     * Stops the periodic publication of readings.
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