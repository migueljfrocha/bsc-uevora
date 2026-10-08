package pt.uevora.sdist.monitoring.grpc;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import io.grpc.Server;
import io.grpc.ServerBuilder;


/**
 * Manages the lifecycle of the gRPC server within the Spring Boot application.
 * Needed to properly start and stop the gRPC server along with the Spring context.
 */
@Component
public class GrpcServerManager implements SmartLifecycle {

    @Value("${grpc.server.port}")
    private int grpcPort;
    
    private final GrpcMetricsServiceImpl grpcMetricsService;
    private volatile Server grpcServer;

    // Spring Boot Lifecycle management
    private volatile boolean running = false;
    private final int phase = Integer.MAX_VALUE;


    /**
     * Constructor for GrpcServerManager.
     * @param grpcMetricsService The GRPC Metrics Service implementation.
     */
    public GrpcServerManager(GrpcMetricsServiceImpl grpcMetricsService) {
        this.grpcMetricsService = grpcMetricsService;
    }


    /**
     * Starts the gRPC server.
     */
    @Override
    public void start(){
        try{
            grpcServer = ServerBuilder
                .forPort(grpcPort)
                .addService(grpcMetricsService)
                .build()
                .start();
            this.running = true;
        } catch(IOException e) {
            this.running = false;
        }
    }


    /**
     * Stops the gRPC server.
     */
    @Override
    public void stop() {
        if (grpcServer != null) {
            grpcServer.shutdown();
        }
        this.running = false;
    }


    /**
     * Checks if the gRPC server is running.
     * @return true if the server is running, false otherwise.
     */
    @Override
    public boolean isRunning() {
        return this.running;
    }


    /**
     * Indicates whether the gRPC server should start automatically with the Spring context.
     * @return true to start automatically, false otherwise.
     */
    @Override
    public boolean isAutoStartup() {
        return true;
    }


    /**
     * Gets the phase of the gRPC server in the Spring lifecycle.
     * @return The phase value.
     */
    @Override
    public int getPhase() {
        return this.phase;
    }


    /**
     * Stops the gRPC server with a callback.
     * @param callback The callback to execute after stopping.
     */
    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }
}