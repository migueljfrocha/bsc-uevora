package pt.uevora.sdist.monitoring.clients;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;
import org.json.JSONArray;

import pt.uevora.sdist.monitoring.model.Device;
import pt.uevora.sdist.monitoring.model.DevicesStats;

import java.net.URI;
import java.io.FileInputStream;
import java.io.IOException;



public class DevicesClient {
    
    private final String deviceEndpoint;

    private final HttpClient httpClient;


    /**
     * Constructor for DevicesClient.
     * @param httpClient
     */
    public DevicesClient(HttpClient httpClient) {
        // Load configuration properties
        Properties props = loadProperties();

        this.deviceEndpoint = props.getProperty("rest.server.endpoint.devices");

        this.httpClient = httpClient;
    }
    

    /**
     * Loads configuration properties from application.properties file.
     * @return: Properties object containing configuration settings.
     */
    private Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("application.properties")) {
            props.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration properties.", e);
        }

        return props;
    }


    /**
     * Adds a new device with the specified parameters.
     * @param protocolType: Protocol type of the device
     * @param roomId: Room ID
     * @param floorId: Floor ID
     * @param buildingId: Building ID
     * @param departmentId: Department ID
     * @param state: State of the device (true for active, false for inactive)
     * @return: Device object representing the newly added device
     */
    public Device addDevice (
        String protocolType, 
        String roomId,
        String floorId,
        String buildingId,
        String departmentId,
        boolean state){

        // Create JSON Device Object
        JSONObject deviceJson = new JSONObject();
        deviceJson.put("protocol", protocolType);
        deviceJson.put("roomId", roomId);
        deviceJson.put("floorId", floorId);
        deviceJson.put("buildingId", buildingId);
        deviceJson.put("departmentId", departmentId);
        deviceJson.put("state", state);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(deviceEndpoint))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(deviceJson.toString()))
            .build();

        return singleDeviceRequest(request);
            
    }
    

    /**
     * Views a device by its ID.
     * @param deviceId: ID of the device to view
     * @return: Device object representing the requested device
     */
    public Device viewDevice(Long deviceId) {
        String deviceEndpoint = this.deviceEndpoint + "/" + deviceId;
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(deviceEndpoint))
            .header("Content-Type", "application/json")
            .GET()
            .build();
            
        return singleDeviceRequest(request);
    }


    /**
     * Updates a device with the specified parameters.
     * @param deviceId: ID of the device to update
     * @param protocolType: protocol type
     * @param roomId: roomId
     * @param floorId: floorId
     * @param buildingId: buildingId
     * @param departmentId: departmentId
     * @param state: state of the device (true for active, false for inactive)
     * @return: Device object representing the updated device
     */
    public Device updateDevice(
        Long deviceId,
        String protocolType, 
        String roomId,
        String floorId,
        String buildingId,
        String departmentId,
        Boolean state) {

        String deviceEndpoint = this.deviceEndpoint + "/" + deviceId;

        // Create JSON Device Object
        JSONObject deviceJson = new JSONObject();
        deviceJson.put("protocol", protocolType);
        deviceJson.put("roomId", roomId);
        deviceJson.put("floorId", floorId);
        deviceJson.put("buildingId", buildingId);
        deviceJson.put("departmentId", departmentId);
        deviceJson.put("state", state);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(deviceEndpoint))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(deviceJson.toString()))
            .build();


        return singleDeviceRequest(request);
    }


    /**
     * Removes a device by its ID.
     * @param deviceId: ID of the device to remove
     * @return: Message indicating the result of the removal operation
     */
    public String removeDevice(Long deviceId) {
        String deviceEndpoint = this.deviceEndpoint + "/" + deviceId;
    
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(deviceEndpoint))
            .header("Content-Type", "application/json")
            .DELETE()
            .build();
            
        try {
            HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int status = resp.statusCode();
            String body = resp.body();
            if (status >= 200 && status < 300) {
                JSONObject json = new JSONObject(body);
                String message = json.optString("message", "Failed to remove device.");
                return message;

            } else {
                JSONObject json = new JSONObject(body);
                String message = json.optString("message", "Unknown error.");
                throw new RuntimeException("Failed to make device request: " + message);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred while making device request.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Device request was interrupted.", e);
        }
    }


    /**
     * Lists all devices.
     * @return: List of Device objects representing all devices
     */
    public List<Device> listDevices() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(deviceEndpoint))
            .header("Content-Type", "application/json")
            .GET()
            .build();

        try {
            HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int status = resp.statusCode();
            String body = resp.body();
            if (status >= 200 && status < 300) {
                JSONObject json = new JSONObject(body);
                JSONArray data = json.getJSONArray("data");
                
                List<Device> devices = new ArrayList<>();
                
                for (int i = 0; i < data.length(); i++) {
                    JSONObject deviceJson = data.getJSONObject(i);
                    Device addedDevice = new Device(deviceJson);
                    devices.add(addedDevice);
                }

                return devices;
            } else {
                JSONObject json = new JSONObject(body);
                String message = json.optString("message", "Unknown error.");
                
                throw new RuntimeException("Failed to make device request: " + message);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred while making device request.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Device request was interrupted.", e);
        }
    }

    
    /**
     * Gets device statistics.
     * @return: DevicesStats object containing statistics
     */
    public DevicesStats getDeviceStatistics() {
        String statsEndpoint = this.deviceEndpoint + "/stats/total";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(statsEndpoint))
            .header("Content-Type", "application/json")
            .GET()
            .build();
            
        try {
            HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int status = resp.statusCode();
            String body = resp.body();
            if (status >= 200 && status < 300) {
                JSONObject json = new JSONObject(body);
                JSONObject data = json.getJSONObject("data");
            
                return new DevicesStats(data);
            } else {
                JSONObject json = new JSONObject(body);
                String message = json.optString("message", "Unknown error.");
                
                throw new RuntimeException("Failed to get stats: " + message);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred while making stats request.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Statistcics request was interrupted.", e);
        }
    }
    

    /**
     * Helper method to handle requests that return a single device.
     * @param request: HttpRequest object
     * @return: Device object representing the requested device
     */
    private Device singleDeviceRequest (HttpRequest request) {
        try {
            HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int status = resp.statusCode();
            String body = resp.body();
            if (status >= 200 && status < 300) {
                JSONObject json = new JSONObject(body);
                JSONObject data = json.getJSONObject("data");
                
                Device addedDevice = new Device(data);

                return addedDevice;
            } else {
                JSONObject json = new JSONObject(body);
                String message = json.optString("message", "Unknown error.");
                
                throw new RuntimeException("Failed to make device request: " + message);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred while making device request.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Device request was interrupted.", e);
        }
    }
}