package pt.uevora.sdist.monitoring.model;

public class AverageReading {
    private final Double avgTemperature;
    private final Double avgHumidity;

    public AverageReading(Double avgTemperature, Double avgHumidity) {
        this.avgTemperature = avgTemperature;
        this.avgHumidity = avgHumidity;
    }

    public Double getAvgTemperature() { 
        return avgTemperature; 
    }
    public Double getAvgHumidity() { 
        return avgHumidity; 
    }
}