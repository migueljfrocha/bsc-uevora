package pt.uevora.sdist.monitoring.mqtt;

import pt.uevora.sdist.monitoring.service.DataProcessor;
import pt.uevora.sdist.monitoring.model.Device.ProtocolType;

import com.fasterxml.jackson.databind.json.JsonMapper;
import java.time.LocalDateTime;
import org.eclipse.paho.client.mqttv3.*;




public class MqttCallbackHandler implements MqttCallback {

    private final DataProcessor dataProcessor;
    private final JsonMapper objectMapper;

    /**
     * Constructor
     * @param dataProcessor The data processor service
     * @param objectMapper A JSON object mapper
     */
    public MqttCallbackHandler(DataProcessor dataProcessor, JsonMapper objectMapper) {
        this.dataProcessor = dataProcessor;
        this.objectMapper = objectMapper;
    }


    /**
     * Handle incoming MQTT messages
     * Handles incoming MQTT messages by validating the device, processing the timestamp,
     * and saving the reading to the database.
     * @param topic The topic of the message
     * @param message The MQTT message
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {

        MqttReadingMessage reading;
        try {
            String payload = new String(message.getPayload());
            reading = objectMapper.readValue(payload, MqttReadingMessage.class);

            if (!dataProcessor.validateDevice(reading.getDeviceId())) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        LocalDateTime timestamp;
        String tsString = reading.getTimestamp();
        
        try {
            timestamp = dataProcessor.processTimestamp(tsString);
        } catch (IllegalArgumentException e) {
            return;
        }

        try {
            dataProcessor.saveReading(
                reading.getDeviceId(),
                reading.getTemperature(),
                reading.getHumidity(),
                timestamp,
                ProtocolType.MQTT
            );
        } catch (Exception e) {
            return;
        }
    }


    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}