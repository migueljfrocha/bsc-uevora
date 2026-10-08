package pt.uevora.sdist.monitoring.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pt.uevora.sdist.monitoring.service.DataProcessor;
import com.fasterxml.jackson.databind.json.JsonMapper;


/**
 * Configuration class for MQTT client setup.
 * Sets up the MQTT client with connection options and necessary beans.
 */
@Configuration
public class MqttConfig {

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;


    /**
     * Creates and configures the MQTT client.
     * Connects to the MQTT broker with specified options.
     * @return Configured MqttClient instance
     * @throws MqttException
     */
    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient(brokerUrl, clientId, new MemoryPersistence());

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

        client.connect(connOpts);
        return client;
    }

    /**
     * Creates the JSON mapper bean.
     * @return Configured JsonMapper instance.
     */
    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    /**
     * Creates the MQTT callback handler bean.
     * @param dataProcessor The data processor to handle incoming messages.
     * @param jsonMapper The JSON mapper for message serialization/deserialization.
     * @return The MQTT callback handler.
     */
    @Bean
    public MqttCallbackHandler mqttCallbackHandler(DataProcessor dataProcessor, JsonMapper jsonMapper) {
        return new MqttCallbackHandler(dataProcessor, jsonMapper);
    }
}