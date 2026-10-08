package pt.uevora.sdist.monitoring.grpcmanager;

import pt.uevora.sdist.monitoring.proto.MetricsProto.SensorData;
import pt.uevora.sdist.monitoring.proto.MetricsServiceGrpc;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.FileInputStream;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class GrpcClientSimulator {

    private final String serverAddress;
    private final String serverPort;
    
    private final Map<Long, Sensor> sensors;

    private final ScheduledExecutorService scheduledExecutor;

    private ManagedChannel channel;
    private MetricsServiceGrpc.MetricsServiceBlockingStub grpcStub;
    

    /**
     * Constructor to initialize the gRPC client simulator.
     */
    public GrpcClientSimulator() {
        // Load configuration properties
        Properties props = loadProperties();

        this.serverAddress = props.getProperty("grpc.server.address");
        this.serverPort = props.getProperty("grpc.server.port");
    
        
        // Initialize gRPC Channel and Stub
        this.channel = ManagedChannelBuilder
                        .forAddress(serverAddress, Integer.parseInt(serverPort))
                        .usePlaintext()
                        .build();

        this.grpcStub = MetricsServiceGrpc.newBlockingStub(channel);

        // Table to hold active sensors
        this.sensors = new HashMap<>();

        // Scheduler for sensor readings
        this.scheduledExecutor = Executors.newScheduledThreadPool(4);

    }


    /**
     * Load configuration properties from application.properties file.
     * @return Properties object containing configuration
     * @throws RuntimeException if properties file cannot be loaded.
     */
    private Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("application.properties")) {
            props.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration properties.", e);
        }
        return props;
    }

    
    /**
     * Add a new sensor with the given ID and reading interval.
     * @param id: Sensor ID
     * @param readingIntervalSeconds: Interval in seconds between sensor readings
     * @throws IllegalArgumentException if a sensor with the given ID already exists.
     */
    public void addSensor(Long id, int readingIntervalSeconds) {

        if (sensors.containsKey(id)){
            throw new IllegalArgumentException("Sensor with ID " + id + " already exists.");
        }
        Sensor sensor = new Sensor(
                id,
                readingIntervalSeconds,
                grpcStub,
                scheduledExecutor
        );
        sensors.put(id, sensor);
        sensor.start();
    }


    /**
     * Remove an existing sensor by its ID.
     * @param id: Sensor ID
     * @throws IllegalArgumentException if no sensor with the given ID exists.
     */
    public void removeSensor(Long id) {
        Sensor sensor = sensors.remove(id);

        if (sensor != null) {
            sensor.stop();
        } else {
            throw new IllegalArgumentException("Sensor with ID " + id + " does not exist.");
        }
    }


    /**
     * List all active sensors with their details.
     * @return String representation of active sensors
     */
    public String listSensors() {
        if (sensors.isEmpty()) {
            return "No active sensors.";
        }
        StringBuilder sb = new StringBuilder("Active Sensors:\n");
        sensors.forEach((id, sensor) -> {
            sb.append("ID: ").append(id)
              .append(" | Interval: ").append(sensor.getSecondsPerReading()).append(" seconds\n");
        });

        sb.append("Total Sensors: ").append(sensors.size());

        return sb.toString();
    }

    
    /**
     * Shutdown the gRPC client simulator, stopping all sensors and disconnecting the gRPC client.
     */
    public void shutdown() {

        // Stop Sensors
        for (Sensor sensor : sensors.values()) {
            sensor.stop();
        }
        sensors.clear();

        // Stop Scheduled Executor
        scheduledExecutor.shutdown();
        try {
            if (!scheduledExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduledExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
            scheduledExecutor.shutdownNow();
        }

        // Disconnect Grpc Client
        if (!channel.isShutdown()) {
            channel.shutdown();
            try {
                if (!channel.awaitTermination(5, TimeUnit.SECONDS)) {
                    channel.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                channel.shutdownNow();
            }
        }
    }
}