package pt.uevora.sdist.monitoring.mqttmanager;

import pt.uevora.sdist.monitoring.model.Publication;

import java.util.concurrent.BlockingQueue;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class CommunicationWorker implements Runnable{
    
    private final MqttClient mqttClient;
    private final BlockingQueue<Publication> pubQueue;
    private final int qos;
    // Flag to control the running state of the worker (visible across threads)
    private volatile boolean running;

    public CommunicationWorker(MqttClient mqttClient, BlockingQueue<Publication> pubQueue, int qos) {
        this.mqttClient = mqttClient;
        this.pubQueue = pubQueue;
        this.running = true;
        this.qos = qos;
    }
    

    /**
     * The main run loop of the CommunicationWorker.
     * Continuously takes publications from the queue and publishes them to the MQTT broker.
     * Handles interruptions by stopping the worker and preserving the interrupt status.
     * Handles MQTT exceptions and other exceptions by logging error messages.
     */
    @Override
    public void run() {
        while (running) {
            try {
                // Take a publication from the queue, will block if empty
                Publication pub = pubQueue.take(); 
                MqttMessage message = new MqttMessage(pub.getContent());
                message.setQos(qos);
                mqttClient.publish(pub.getTopic(), message);
            } catch (InterruptedException ie) {
                // Thread was interrupted while waiting on queue. Interrupting again to preserve the interrupt status.
                Thread.currentThread().interrupt();
                running = false;
            } catch (MqttException me) {
                System.err.println("MqttException publishing message: " + me.getMessage());
            } catch (Exception e) {
                System.err.println("Error publishing message: " + e.getMessage());
            }
        }
    }


    /**
     * Stops the CommunicationWorker by setting the running flag to false.
     */
    public void stop() {
        this.running = false;
    }
}
