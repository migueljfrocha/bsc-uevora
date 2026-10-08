package pt.uevora.sdist.monitoring.grpcmanager;

import pt.uevora.sdist.monitoring.utils.SensorDataGenerator;

import pt.uevora.sdist.monitoring.proto.MetricsProto.SensorData;
import pt.uevora.sdist.monitoring.proto.MetricsServiceGrpc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Sensor {

    private final SensorDataGenerator dataGenerator;
    private final Long id;
    private final int secondsPerReading; 

    private final ScheduledExecutorService scheduledExecutor;
    private final MetricsServiceGrpc.MetricsServiceBlockingStub grpcStub;
    private ScheduledFuture<?> scheduledFuture;    


    /**
     * Constructor for Sensor class.
     * @param id Unique identifier for the sensor.
     * @param secondsPerReading Interval between readings in seconds.
     * @param grpcStub gRPC stub for sending data.
     * @param scheduledExecutor Executor service for scheduling tasks.
     */
    public Sensor(
                Long id,
                int secondsPerReading, 
                MetricsServiceGrpc.MetricsServiceBlockingStub grpcStub,
                ScheduledExecutorService scheduledExecutor) {
                    
        this.id = id;
        this.secondsPerReading = secondsPerReading;
        this.grpcStub = grpcStub;
        this.scheduledExecutor = scheduledExecutor;

        this.dataGenerator = new SensorDataGenerator(secondsPerReading);
    }


    /**
     * Starts the sensor to periodically send readings.
     */
    public void start() {
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            // Schedule periodic reading publication
            scheduledFuture = scheduledExecutor.scheduleAtFixedRate(
                () -> sendReading(),
                0, 
                secondsPerReading, 
                TimeUnit.SECONDS
            );
        }
    }


    /**
     * Sends a single sensor reading to the gRPC server.
     */
    public void sendReading(){
        
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

            SensorData request = SensorData.newBuilder()
                    .setDeviceId(this.id)
                    .setTemperature(roundedTemp)
                    .setHumidity(roundedHum)
                    .setTimestamp(timestamp)
                    .build();

            grpcStub.sendData(request);
        } catch(Exception e){
            System.err.println("Error publishing sensor " + id + " reading: " + e.getMessage());
        }
        
    }

    /**
     * Stops the sensor from sending further readings.
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