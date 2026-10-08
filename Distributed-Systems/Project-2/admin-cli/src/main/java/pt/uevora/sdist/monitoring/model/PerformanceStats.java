package pt.uevora.sdist.monitoring.model;

import org.json.JSONObject;

public class PerformanceStats {

    private static final String TABLE_ROW_FORMAT = "| %-8s | %-5s | %-15s | %-15s |";

    private double mqttLatency;
    private double mqttThroughput;
    private long mqttNumReadings;
    
    private double restLatency;
    private double restThroughput;
    private long restNumReadings;
    
    private double grpcLatency;
    private double grpcThroughput;
    private long grpcNumReadings;

    /**
     * Constructor to create PerformanceStats object from JSON data
     * @param data: JSON object containing performance statistics
     */
    public PerformanceStats(JSONObject data) {
        JSONObject mqttStats = data.getJSONObject("mqtt");
        this.mqttLatency = mqttStats.getDouble("latency");
        this.mqttThroughput = mqttStats.getDouble("throughputRPS");
        this.mqttNumReadings = mqttStats.getLong("size");
        
        JSONObject restStats = data.getJSONObject("rest");
        this.restLatency = restStats.getDouble("latency");
        this.restThroughput = restStats.getDouble("throughputRPS");
        this.restNumReadings = restStats.getLong("size");
        
        JSONObject grpcStats = data.getJSONObject("grpc");
        this.grpcLatency = grpcStats.getDouble("latency");
        this.grpcThroughput = grpcStats.getDouble("throughputRPS");
        this.grpcNumReadings = grpcStats.getLong("size");
    }


    /** 
     * Prints the header for the performance statistics table
     */
    public void printHeader() {
        printSeparator();
        System.out.println(String.format(TABLE_ROW_FORMAT, "Protocol", "count", "Latency(ms)", "Throughput(RPS)"));
        printSeparator();    
    }

    
    /** 
     * Prints the performance statistics in a formatted table
     */
    public void printStats() {
        System.out.println(String.format(TABLE_ROW_FORMAT, "MQTT", this.mqttNumReadings, this.mqttLatency, this.mqttThroughput));
        System.out.println(String.format(TABLE_ROW_FORMAT, "REST", this.restNumReadings, this.restLatency, this.restThroughput));
        System.out.println(String.format(TABLE_ROW_FORMAT, "gRPC", this.grpcNumReadings, this.grpcLatency, this.grpcThroughput));
        printSeparator();
    }

    
    /** 
     * Prints a separator line for the table
     */
    public void printSeparator() {
        String header = String.format(TABLE_ROW_FORMAT,
                "", "", "", "");
        String separator = new String(new char[header.length()]).replace("\0", "-");
        System.out.println(separator);
    }
    
}
