package pt.uevora.sdist.monitoring.model;

import org.json.JSONObject;

public class ReadingsStats {
    private static final String TABLE_ROW_FORMAT = "| %-20s | %-20s |";

    private long totalReadings;
    private long mqttReadings;
    private long restReadings;
    private long grpcReadings;

    
    /**
     * Constructor
     * @param json: JSON object containing the readings statistics
     */
    public ReadingsStats(JSONObject json) {
        this.totalReadings = json.optLong("total", 0);
        this.mqttReadings = json.optLong("mqtt", 0);
        this.restReadings = json.optLong("rest", 0);
        this.grpcReadings = json.optLong("grpc", 0);
    }

    
    /**
     * Print the table header
     */
    public void printHeader() {
        printSeparator();
        System.out.println(String.format(TABLE_ROW_FORMAT, "Reading Type", "Count"));
        printSeparator();
    }

    /**
     * Print the readings statistics in a table format
     */
    public void printStats() {
        System.out.println(String.format(TABLE_ROW_FORMAT, "MQTT Readings", this.mqttReadings));
        System.out.println(String.format(TABLE_ROW_FORMAT, "REST Readings", this.restReadings));
        System.out.println(String.format(TABLE_ROW_FORMAT, "gRPC Readings", this.grpcReadings));
        printSeparator();
        System.out.println(String.format(TABLE_ROW_FORMAT, "TOTAL", this.totalReadings));
        printSeparator();
    }

    /**
     * Print a separator line for the table
     */
    public void printSeparator() {
        String header = String.format(TABLE_ROW_FORMAT,
                "", "");
        String separator = new String(new char[header.length()]).replace("\0", "-");
        System.out.println(separator);
    }
}