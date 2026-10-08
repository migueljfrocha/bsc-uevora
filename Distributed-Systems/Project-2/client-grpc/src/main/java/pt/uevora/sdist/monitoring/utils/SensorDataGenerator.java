package pt.uevora.sdist.monitoring.utils;

import java.util.Random;

public class SensorDataGenerator {
    private final int secondsPerReading; 

    private final Random rd;

    private Double lastTemperature;
    private Double lastHumidity;

    
    /**
     * Constructor for SensorDataGenerator to simulate sensor data.
     * @param secondsPerReading: Interval between readings in seconds
     */
    public SensorDataGenerator (int secondsPerReading) {
        this.secondsPerReading = secondsPerReading;

        this.rd = new Random();
        this.lastTemperature = 15.0 + rd.nextDouble() * 15.0;
        this.lastHumidity = 30.0 + rd.nextDouble() * 50.0;
    }

    
    /**
     * Generates the next temperature reading based on the last reading. 
     * Uses the reading interval to calculate the variation.
     * The temperature variation ranges from -0.05 to +0.05 degrees Celsius per minute.
     * @return Double: Next temperature reading
     */
    public Double getNextTemperature() {
        Double temperatureVariation;
        Double temperatureAfterVariation;
        
        do{

            Double randomFactor = (rd.nextDouble() * 0.1) - 0.05;
            
            temperatureVariation = this.secondsPerReading * randomFactor / 60;
            temperatureAfterVariation = this.lastTemperature + temperatureVariation;

        } while(temperatureAfterVariation < 15.0 || temperatureAfterVariation > 30.0);
    
        this.lastTemperature = temperatureAfterVariation;

        return temperatureAfterVariation;
    }


    /**
     * Generates the next humidity reading based on the last reading.
     * Uses the reading interval to calculate the variation.
     * The humidity variation ranges from -0.2% to +0.2% per minute.
     * @return Double: Next humidity reading
     */
    public Double getNextHumidity() {
        Double humVariation;
        Double humidityAfterVariation;
        
        do{

            Double randomFactor = (rd.nextDouble() * 0.4) - 0.2;
            
            humVariation = this.secondsPerReading * randomFactor / 60;
            humidityAfterVariation = this.lastHumidity + humVariation;

        } while(humidityAfterVariation < 30.0 || humidityAfterVariation > 80.0);
    
        this.lastHumidity = humidityAfterVariation;

        return humidityAfterVariation;
    }
}