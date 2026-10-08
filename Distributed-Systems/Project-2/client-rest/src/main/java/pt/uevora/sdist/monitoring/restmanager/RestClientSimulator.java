package pt.uevora.sdist.monitoring.restmanager;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.FileInputStream;
import java.net.http.HttpClient;
import java.time.Duration;

public class RestClientSimulator {

    private final String ingestEndpoint;
    private final Map<Long, Sensor> sensors;

    private final ScheduledExecutorService scheduledExecutor;
    private final HttpClient httpClient;


    /**
     * Constructor to initialize the REST Client Simulator.
     */
    public RestClientSimulator() {
        // Load configuration properties
        Properties props = loadProperties();

        this.ingestEndpoint = props.getProperty("rest.ingest.endpoint");
    
        // Initialize HTTP Client    
        this.httpClient = HttpClient.newBuilder()
                                .connectTimeout(Duration.ofSeconds(10))
                                .build();

        // Table to hold active sensors
        this.sensors = new HashMap<>();

        // Scheduler for sensor readings
        this.scheduledExecutor = Executors.newScheduledThreadPool(4);
    }

    
    /**
     * Load configuration properties from a file.
     * @return Properties object containing configuration settings.
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
     * Add a new sensor to the simulator.
     * @param id Sensor ID
     * @param readingIntervalSeconds Interval between readings in seconds
     * @throws IllegalArgumentException if sensor with given ID already exists.
     */
    public void addSensor(Long id, int readingIntervalSeconds) {
        
        if (sensors.containsKey(id)){
            throw new IllegalArgumentException("Sensor with ID " + id + " already exists.");
        }
        Sensor sensor = new Sensor(
                id,
                readingIntervalSeconds,
                ingestEndpoint,
                httpClient,
                scheduledExecutor
        );
        sensors.put(id, sensor);
        sensor.start();
    }


    /**
     * Remove a sensor from the simulator.
     * @param id Sensor ID
     * @throws IllegalArgumentException if sensor with given ID does not exist.
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
     * List all active sensors.
     * @return String representation of active sensors.
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
     * Shutdown the simulator, stopping all sensors and scheduled tasks.
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

    }
}