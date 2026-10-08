package pt.uevora.sdist.monitoring.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;


/**
 * Manages the lifecycle of the MQTT connection within the Spring Boot application.
 * Handles connecting, subscribing, disconnecting, and unsubscribing from the MQTT broker.
 */
@Component
public class MqttConnectionManager implements SmartLifecycle {

    private final MqttClient mqttClient;
    private final MqttCallbackHandler callback;
    
    // Lifecycle state
    private volatile boolean running = false;
    private final int phase = Integer.MAX_VALUE;

    @Value("${mqtt.topic}")
    private String topic;

    @Value("${mqtt.qos}")
    private int qos;
    

    /**
     * Constructor for MqttConnectionManager.
     * @param mqttClient The MQTT client instance.
     * @param callback The MQTT callback handler.
     */
    public MqttConnectionManager(MqttClient mqttClient, MqttCallbackHandler callback) {
        this.mqttClient = mqttClient;
        this.callback = callback;
    }

    @Override
    public void start() {
        try{
            if (!mqttClient.isConnected()) {
                mqttClient.connect();
            }
        } catch (MqttException e) {
            this.running = false;
            throw new RuntimeException("Failed to connect to MQTT broker", e);
        }
        try {
            mqttClient.setCallback(callback);
            mqttClient.subscribe(topic, qos);
            this.running = true;
        } catch (MqttException e) {
            this.running = false;
            throw new RuntimeException("Failed to subscribe to topic: " + topic, e);
        }
    }

    /**
     * Stops the MQTT connection by unsubscribing from the topic and disconnecting from the broker.
     */
    @Override
    public void stop() {
        try {
            if (mqttClient.isConnected()) {
                mqttClient.unsubscribe(topic);
                mqttClient.disconnect();
            }
        } catch (MqttException e) {
            
        } finally {
            this.running = false;
        }
    }


    /**
     * Checks if the MQTT connection manager is running.
     */
    @Override
    public boolean isRunning() {
        return this.running;
    }


    /**
     * Gets the phase of the lifecycle.
     */
    @Override
    public int getPhase() {
        return this.phase;
    }


    /**
     * Indicates that the MQTT connection manager should start automatically.
     */
    @Override
    public boolean isAutoStartup() {
        return true;
    }
    

    /**
     * Stops the MQTT connection manager with a callback.
     * @param callback The callback to run after stopping
     */
    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }
}