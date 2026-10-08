package pt.uevora.sdist.monitoring.model;

public class Publication {
    private final String topic;
    private final byte[] content;

    /**
     * Constructs a Publication instance for MQTT publishing with the Worker thread.
     * @param topic MQTT topic
     * @param content Message content as byte array
     */
    public Publication(String topic, byte[] content) {
        this.topic = topic;
        this.content = content;
    }

    // Getters

    public String getTopic() {
        return topic;
    }

    public byte[] getContent() {
        return content;
    }
}
