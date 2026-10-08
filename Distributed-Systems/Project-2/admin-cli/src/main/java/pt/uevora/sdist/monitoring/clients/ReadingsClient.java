package pt.uevora.sdist.monitoring.clients;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;
import org.json.JSONArray;

import pt.uevora.sdist.monitoring.model.PerformanceStats;
import pt.uevora.sdist.monitoring.model.Reading;
import pt.uevora.sdist.monitoring.model.ReadingsStats;

public class ReadingsClient {


    private final String avgReadingsEndpoint;
    private final String rawReadingsEndpoint;
    private final String rawLevelReadingsEndpoint;
    private final String readingsEndpoint;
    private final HttpClient httpClient;



    /**
     * Constructor for ReadingsClient.
     * @param httpClient
     */
    public ReadingsClient(HttpClient httpClient) {
        // Load configuration properties
        Properties props = loadProperties();

        this.avgReadingsEndpoint = props.getProperty("rest.server.endpoint.readings.avg");
        this.rawReadingsEndpoint = props.getProperty("rest.server.endpoint.readings.raw");
        this.rawLevelReadingsEndpoint = props.getProperty("rest.server.endpoint.readings.raw-level");
        this.readingsEndpoint = props.getProperty("rest.server.endpoint.readings");

        this.httpClient = httpClient;
    }
 

    /**
     * Loads configuration properties from application.properties file.
     * @return Properties object containing configuration settings.
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
     * Gets average readings for a specified level and time range.
     * @param level: sala|departamento|piso|edificio
     * @param levelId: id of the specified level
     * @param from: start date/time (ISO 8601 format)
     * @param to: end date/time (ISO 8601 format)
     * @return: Reading object containing average temperature and humidity
     */
    public Reading getAvgReadings(String level, String levelId, String from, String to) {
        
        String url;
        if (!from.isEmpty() && !to.isEmpty()){
            // /api/metrics/average?level={sala|departamento|piso|edificio}&id={id}&from={date}&to={date}
            url = String.format(
                "%s?level=%s&id=%s&from=%s&to=%s",
                avgReadingsEndpoint,
                level,
                levelId,
                from,
                to
            );
        } else {
            // /api/metrics/average?level={sala|departamento|piso|edificio}&id={id}
            url = String.format(
                "%s?level=%s&id=%s",
                avgReadingsEndpoint,
                level,
                levelId
            );
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
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

                Double temperature = data.optDouble("avgTemperature", Double.NaN);
                Double humidity = data.optDouble("avgHumidity", Double.NaN);

                if(temperature.isNaN() || humidity.isNaN()){
                    throw new RuntimeException("Average readings data is incomplete.");
                }

                Double roundedTemp = BigDecimal.valueOf(temperature)
                                        .setScale(6, RoundingMode.HALF_UP)
                                        .doubleValue();
                Double roundedHum = BigDecimal.valueOf(humidity)
                                        .setScale(6, RoundingMode.HALF_UP)
                                        .doubleValue();

                Reading reading = new Reading(roundedTemp, roundedHum);

                return reading;
            } else {
                JSONObject json = new JSONObject(body);
                String message = json.optString("message", "Unknown error.");
                throw new RuntimeException("Failed to make reading request: " + message);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred while making reading request.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Reading request was interrupted.", e);
        }
    }


    /**
     * Gets raw readings for a specified device and time range.
     * @param deviceId: ID of the device
     * @param from: start date/time (ISO 8601 format)
     * @param to: end date/time (ISO 8601 format)
     * @return: List of Reading objects containing raw readings
     */
    public List<Reading> getRawReadings(Long deviceId, String from, String to) {
        
        String url;
        if (!from.isEmpty() && !to.isEmpty()){
            // /api/metrics/raw?deviceId={id}&from={date}&to={date}
            url = String.format(
                "%s?deviceId=%s&from=%s&to=%s",
                rawReadingsEndpoint,
                deviceId,
                from,
                to
            );
        } else {
            // /api/metrics/raw?deviceId={id}
            url = String.format(
                "%s?deviceId=%s",
                rawReadingsEndpoint,
                deviceId
            );
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .GET()
            .build();

        Long idDevice = 0L;
        try {
            HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int status = resp.statusCode();
            String body = resp.body();
            if (status >= 200 && status < 300) {
                JSONObject json = new JSONObject(body);
                JSONArray data = json.getJSONArray("data");

                List<Reading> readings = new ArrayList<>();
                
                if(data.length() > 0){
                    JSONObject deviceJson = data.getJSONObject(0);
                    idDevice = deviceJson.getJSONObject("device").getLong("id");
                }

                for (int i = 0; i < data.length(); i++) {
                    JSONObject deviceJson = data.getJSONObject(i);
                    Reading addedReading = new Reading(idDevice, deviceJson);
                    readings.add(addedReading);
                }

                return readings;
            } else {
                JSONObject json = new JSONObject(body);
                String message = json.optString("message", "Unknown error.");
                
                throw new RuntimeException("Failed to make reading request: " + message);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred while making reading request.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Reading request was interrupted.", e);
        }
    }


    /**
     * Gets level raw readings for a specified level and time range.
     * @param level: level type (sala|departamento|piso|edificio)
     * @param levelId: ID of the level
     * @param from: start date/time (ISO 8601 format)
     * @param to: end date/time (ISO 8601 format)
     * @return: List of Reading objects containing raw readings
     */
    public List<Reading> getRawLevelReadings(String level, String levelId, String from, String to) {

        String url;
        if (!from.isEmpty() && !to.isEmpty()){
            // /api/metrics/raw-level?deviceId={id}&from={date}&to={date}
            url = String.format(
                "%s?level=%s&id=%s&from=%s&to=%s",
                rawLevelReadingsEndpoint,
                level,
                levelId,
                from,
                to
            );   
        } else {
            // /api/metrics/raw-level?deviceId={id}
            url = String.format(
                "%s?level=%s&id=%s",
                rawLevelReadingsEndpoint,
                level,
                levelId
            );   
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
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

                List<Reading> readings = new ArrayList<>();

                for (int i = 0; i < data.length(); i++) {
                    JSONObject deviceJson = data.getJSONObject(i);
                    Reading addedReading = new Reading(deviceJson);
                    readings.add(addedReading);
                }

                return readings;
            } else {
                JSONObject json = new JSONObject(body);
                String message = json.optString("message", "Unknown error.");
                
                throw new RuntimeException("Failed to make reading request: " + message);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred while making reading request.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Reading request was interrupted.", e);
        }
    }


    /**
     * Gets readings statistics.
     * @return: ReadingsStats object containing statistics
     */
    public ReadingsStats getReadingsStatistics() {
        String statsEndpoint = this.readingsEndpoint + "/stats/total";
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

                return new ReadingsStats(data);
                
            } else {
                JSONObject json = new JSONObject(body);
                String message = json.optString("message", "Unknown error.");
                
                throw new RuntimeException("Failed to get stats: " + message);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred while making stats request.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Statistics request was interrupted.", e);
        }
    }


    /**
     * Gets performance statistics.
     * @param time: time interval in seconds
     * @return: PerformanceStats object containing performance statistics
     */
        public PerformanceStats getPerformanceStatistics(Long time) {

        String statsEndpoint = this.readingsEndpoint + "/stats/performance/" + time;
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

                return new PerformanceStats(data);
                
            } else {
                JSONObject json = new JSONObject(body);
                String message = json.optString("message", "Unknown error.");
                
                throw new RuntimeException("Failed to get performance statistics: " + message);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred while making performance statistics request.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Performance statistics request was interrupted.", e);
        }
    }
    
}