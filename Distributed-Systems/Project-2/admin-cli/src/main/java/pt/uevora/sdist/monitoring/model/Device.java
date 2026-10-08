package pt.uevora.sdist.monitoring.model;

import org.json.JSONObject;

public class Device {
    private Long id;
    private String protocol;
    private String roomId;
    private String departmentId;
    private String floorId;
    private String buildingId;
    private Boolean state;
    private String createdAt;
    private static final String TABLE_ROW_FORMAT = "| %-3s | %-8s | %-9s | %-20s | %-7s | %-20s | %-8s |";


    /**
     * Constructor to create Device object from JSON data
     * @param json: JSON object containing device data
     */
    public Device(JSONObject json){
        this.id = json.getLong("id");
        this.roomId = json.getJSONObject("room").getString("roomId");
        this.floorId = json.getJSONObject("floor").getString("floorId");
        this.buildingId = json.getJSONObject("building").getString("buildingId");               
        this.departmentId = json.getJSONObject("department").getString("departmentId");
        this.state = json.getBoolean("state");
        this.createdAt = json.getString("createdAt");
        this.protocol = json.getString("protocol");
    }
        
    /** 
     * Prints the header for the device table
     */
    public static void printHeader(){
        printSeparator();
        System.out.println(String.format(TABLE_ROW_FORMAT,
                "ID", "Protocol", "Room", "Department", "Floor", "Building", "State"));
        printSeparator();
    }


    /**
     * Prints the device information in a formatted table row
     */
    public void printDevice() {
        String rowString = String.format(TABLE_ROW_FORMAT,
                id,
                protocol,
                roomId,
                departmentId,
                floorId,
                buildingId,
                state ? "Active" : "Inactive");
        System.out.println(rowString);
    }

    
    /**
     * Prints a separator line for the device table
     */
    public static void printSeparator(){
        String header = String.format(TABLE_ROW_FORMAT,
                "ID", "Protocol", "Room", "Department", "Floor", "Building", "State");
        String separator = new String(new char[header.length()]).replace("\0", "-");
        System.out.println(separator);
    }

}