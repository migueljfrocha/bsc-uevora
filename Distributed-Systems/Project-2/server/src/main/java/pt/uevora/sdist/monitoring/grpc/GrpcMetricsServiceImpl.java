package pt.uevora.sdist.monitoring.grpc;

import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import pt.uevora.sdist.monitoring.service.DataProcessor;
import pt.uevora.sdist.monitoring.proto.MetricsProto.SensorData;
import pt.uevora.sdist.monitoring.proto.MetricsProto.MetricResponse;
import pt.uevora.sdist.monitoring.proto.MetricsServiceGrpc;

import pt.uevora.sdist.monitoring.model.Device.ProtocolType;

import java.time.LocalDateTime;

@Service
public class GrpcMetricsServiceImpl extends MetricsServiceGrpc.MetricsServiceImplBase {

    private final DataProcessor dataProcessor;

    /**
     * Constructor for GrpcMetricsServiceImpl.
     * GRPC service for handling incoming sensor data.
     * @param dataProcessor The DataProcessor instance.
     */
    public GrpcMetricsServiceImpl(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }


    /**
     * Handles incoming sensor data via gRPC.
     * The method validates the device, processes the timestamp, and saves the reading.
     * @param request The incoming sensor data request
     * @param responseObserver The response observer to send responses back to the client
     */
    @Override
    public void sendData(SensorData request, StreamObserver<MetricResponse> responseObserver) {

        if (!dataProcessor.validateDevice(request.getDeviceId())) {
            sendResponse(responseObserver, false, "Device not registered or inactive: " + request.getDeviceId());
            return;
        }

        LocalDateTime timestamp;
        String tsString = request.getTimestamp();
        
        try {
            timestamp = dataProcessor.processTimestamp(tsString);
        } catch (IllegalArgumentException e) {
            sendResponse(responseObserver, false, "Invalid timestamp format: " + tsString);
            return;
        }

        try{
            dataProcessor.saveReading(
                request.getDeviceId(),
                request.getTemperature(),
                request.getHumidity(),
                timestamp,
                ProtocolType.GRPC
            );
            sendResponse(responseObserver, true, "Data processed successfully for device: " + request.getDeviceId());
            
        } catch(IllegalArgumentException e){
            sendResponse(responseObserver, false, "Invalid data: " + e.getMessage());
        } catch(Exception e){
            sendResponse(responseObserver, false, "Failed to process data for device: " + request.getDeviceId());
        }
    }


    /**
     * Sends a response back to the gRPC client.
     * @param responseObserver The response observer to send responses back to the client
     * @param success Indicates if the operation was successful
     * @param message The message to be sent back to the client
     */
    private void sendResponse(StreamObserver<MetricResponse> responseObserver, Boolean success, String message) {
        MetricResponse response = MetricResponse.newBuilder()
                .setSuccess(success)
                .setMessage(message)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}