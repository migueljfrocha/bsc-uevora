package pt.uevora.sdist.monitoring.model;

import org.json.JSONObject;

public class Reading {
    private static final String AVERAGE_TABLE_ROW_FORMAT = "| %-11s | %-11s |";
    private static final String RAW_TABLE_ROW_FORMAT = "| %-5s | %-9s | %-11s | %-11s | %-30s |";
    
    private Long id;
    private Long deviceId;
    private Double temperature;
    private Double humidity;
    private String timestamp;


    /**
     * Constructor for average readings
     * @param temperature
     * @param humidity
     */
    public Reading(Double temperature, Double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }


    /**
     * Constructor for raw data readings
     * @param deviceId: ID of the device
     * @param json: JSON object containing reading data
     */
    public Reading(Long deviceId, JSONObject json){
        this.id = json.getLong("id");
        this.deviceId = deviceId;
        this.temperature = json.getDouble("temperature");
        this.humidity = json.getDouble("humidity");               
        this.timestamp = json.getString("timestamp");
    }

    
    /**
     * Constructor for raw data readings per level
     * @param json: JSON object containing reading data
     */
    public Reading(JSONObject json){
        this.id = json.getLong("id");
        this.deviceId = json.getJSONObject("device").getLong("id");
        this.temperature = json.getDouble("temperature");
        this.humidity = json.getDouble("humidity");               
        this.timestamp = json.getString("timestamp");
    }


    /** 
     * Prints the header for the raw data readings table
     */
    public static void printRawHeader(){
        printRawSeparator();
        System.out.println(String.format(RAW_TABLE_ROW_FORMAT,
                "ID", "Device ID", "Temperature", "Humidity", "Timestamp"));
        printRawSeparator();
    }


    /**
     * Prints the header for the average readings table
     */
    public static void printAvgHeader(){
        printAvgSeparator();
        System.out.println(String.format(AVERAGE_TABLE_ROW_FORMAT, "Temperature", "Humidity"));
        printAvgSeparator();
    }
    

    /** 
     * Prints the raw data reading in a formatted table row
     */
    public void printRawReadings() {
        String rowString = String.format(RAW_TABLE_ROW_FORMAT,
                id,
                deviceId,
                temperature,
                humidity,
                timestamp);
        System.out.println(rowString);
    }

    
    /**
     * Prints the average reading in a formatted table row
     */
    public void printAvgReadings() {
        System.out.println(String.format(AVERAGE_TABLE_ROW_FORMAT, temperature, humidity));
    }

    
    /**
     * Prints the separator for the raw data readings table
     */
    public static void printRawSeparator(){
        String header = String.format(RAW_TABLE_ROW_FORMAT,
                "ID", "Device ID", "Temperature", "Humidity", "Timestamp");
        String separator = new String(new char[header.length()]).replace("\0", "-");
        System.out.println(separator);
    }


    /**
     * Prints the separator line for the average readings table
     */
    public static void printAvgSeparator(){
        String header = String.format(AVERAGE_TABLE_ROW_FORMAT,
                "Temperature", "Humidity");
        String separator = new String(new char[header.length()]).replace("\0", "-");
        System.out.println(separator);
    }
}