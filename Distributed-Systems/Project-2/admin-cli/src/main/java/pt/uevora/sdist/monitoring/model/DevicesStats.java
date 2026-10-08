package pt.uevora.sdist.monitoring.model;

import org.json.JSONObject;

public class DevicesStats {
    private static final String TABLE_ROW_FORMAT = "| %-20s | %-20s |";

    private long totalDevices;
    private long activeDevices;
    private long inactiveDevices;
    private long mqttDevices;
    private long grpcDevices;
    private long restDevices;


    /**
     * Constructor to create DevicesStats object from JSON data
     * @param data: JSON object containing devices statistics data
     */
    public DevicesStats(JSONObject data) {
        this.totalDevices = data.optLong("total", 0);
        this.activeDevices = data.optLong("active", 0);
        this.inactiveDevices = data.optLong("inactive", 0);

        this.mqttDevices = data.optLong("mqtt", 0);
        this.restDevices = data.optLong("rest", 0);
        this.grpcDevices = data.optLong("grpc", 0);
    }


    /** 
     * Prints the header for the devices statistics table
     */
    public void printHeader() {
        printSeparator();
        System.out.println(String.format(TABLE_ROW_FORMAT, "Statistic", "Value"));
        printSeparator();    
    }

    
    /** 
     * Prints the devices statistics in a formatted table
     */
    public void printStats() {
        System.out.println(String.format(TABLE_ROW_FORMAT, "Active Devices", this.activeDevices));
        System.out.println(String.format(TABLE_ROW_FORMAT, "Inactive Devices", this.inactiveDevices));
        printSeparator();
        System.out.println(String.format(TABLE_ROW_FORMAT, "MQTT Devices", this.mqttDevices));
        System.out.println(String.format(TABLE_ROW_FORMAT, "REST Devices", this.restDevices));
        System.out.println(String.format(TABLE_ROW_FORMAT, "gRPC Devices", this.grpcDevices));
        printSeparator();
        System.out.println(String.format(TABLE_ROW_FORMAT, "TOTAL", this.totalDevices));
        printSeparator();
    }

    
    /** 
     * Prints a separator line for the table
     */
    public void printSeparator() {
        String header = String.format(TABLE_ROW_FORMAT,
                "", "");
        String separator = new String(new char[header.length()]).replace("\0", "-");
        System.out.println(separator);
    }
    
}
