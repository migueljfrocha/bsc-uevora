package pt.uevora.sdist.monitoring.mqttmanager;

import pt.uevora.sdist.monitoring.model.Publication;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.io.FileInputStream;

public class MqttClientSimulator {

    private final String brokerURL;
    private final String clientId;
    private final String topicPrefix;
    private final int qos;
    
    private final Map<Long, Sensor> sensors;

    private final ScheduledExecutorService scheduledExecutor;
    private final BlockingQueue<Publication> mqttQueue;

    private final MqttClient mqttPublisher;
    private final CommunicationWorker pubWorker;
    private final Thread mqttWorkerThread;

    
    
    /**
     * Initializes the MqttClientSimulator by loading configuration properties,
     * setting up the MQTT client, and starting the communication worker thread.
     * 
     * @throws MqttException if there is an error initializing the MQTT client.
     */
    public MqttClientSimulator() throws MqttException {
        // Load configuration properties
        Properties props = loadProperties();

        this.brokerURL = props.getProperty("mqtt.broker.url");
        this.clientId = props.getProperty("mqtt.client.id");
        this.topicPrefix = props.getProperty("mqtt.topic");
        this.qos = Integer.parseInt(props.getProperty("mqtt.qos"));

        String username = props.getProperty("mqtt.username");
        String password = props.getProperty("mqtt.password");
        
        // MQTT Publisher Client
        this.mqttPublisher = new MqttClient(
                brokerURL,
                clientId,
                new MemoryPersistence()
        );

        MqttConnectOptions connOpts = setupMqttConnectOptions(username, password);
        mqttPublisher.connect(connOpts);

        // Table to hold active sensors
        this.sensors = new HashMap<>();

        // Scheduler for sensor readings
        this.scheduledExecutor = Executors.newScheduledThreadPool(4);

        // MQTT publish Queue
        this.mqttQueue = new LinkedBlockingQueue<>();

        // MQTT Worker Thread for publishing messages
        this.pubWorker = new CommunicationWorker(mqttPublisher, mqttQueue, qos);
        this.mqttWorkerThread = new Thread(pubWorker, "mqtt-publisher-worker");
        this.mqttWorkerThread.start();
    }


    /**
     * Loads configuration properties from the application.properties file.
     * @return Properties object containing the loaded properties.
     * @throws RuntimeException if there is an error loading the properties file.
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
     * Sets up MQTT connection options, as well as username and password for the broker if provided.
     * @param username MQTT broker username.
     * @param password MQTT broker password.
     * @return Configured MqttConnectOptions object.
     */
    private MqttConnectOptions setupMqttConnectOptions(String username, String password) {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setAutomaticReconnect(true);
        connOpts.setConnectionTimeout(10);
        connOpts.setKeepAliveInterval(60);
        
        if (username != null && !username.isBlank()) {
            connOpts.setUserName(username);
            if (password != null) {
                connOpts.setPassword(password.toCharArray());
            }
        }

        return connOpts;
    }

    
    /**
     * Adds a new sensor with the specified ID and reading interval.
     * @param id Unique identifier for the sensor.
     * @param readingIntervalSeconds Interval in seconds between sensor readings.
     * @throws IllegalArgumentException if a sensor with the given ID already exists.
     */
    public void addSensor(Long id, int readingIntervalSeconds) {

        if (sensors.containsKey(id)){
            throw new IllegalArgumentException("Sensor with ID " + id + " already exists.");
        }
        Sensor sensor = new Sensor(
                id,
                readingIntervalSeconds,
                topicPrefix + id, //sensors/{id}
                mqttQueue,
                scheduledExecutor
        );
        sensors.put(id, sensor);
        sensor.start();
    }

    
    /**
     * Removes the sensor with the specified ID.
     * @param id Unique identifier of the sensor to be removed.
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
     * Lists all active sensors with their IDs and reading intervals.
     * @return A formatted string containing all active sensors.
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
     * Shuts down the MqttClientSimulator by stopping all sensors, the scheduled executor, and the MQTT publisher worker.
     * Also disconnects and closes the MQTT client.
     */
    public void shutdown() {

        // Stop Sensors
        for (Sensor sensor : sensors.values()) {
            sensor.stop();
        }
        sensors.clear();

        // Stop Scheduled Executor
        scheduledExecutor.shutdown();

        // Stop MQTT Publisher Worker
        pubWorker.stop();
        mqttWorkerThread.interrupt();
        try {
            mqttWorkerThread.join();
        } catch (InterruptedException e) {
            // Preserve interrupt status in case of interruption
            Thread.currentThread().interrupt();
        }

        // Disconnect MQTT Client
        try {
            if (mqttPublisher != null && mqttPublisher.isConnected()) {
                mqttPublisher.disconnect();
            }
            if (mqttPublisher != null) {
                mqttPublisher.close();
            }
        } catch (MqttException e) {
            System.err.println("Error closing MQTT client: " + e.getMessage());
        }
    }
}