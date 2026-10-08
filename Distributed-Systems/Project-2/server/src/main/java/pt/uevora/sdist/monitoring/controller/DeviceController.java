package pt.uevora.sdist.monitoring.controller;

import pt.uevora.sdist.monitoring.service.DeviceService;
import pt.uevora.sdist.monitoring.model.Device;
import pt.uevora.sdist.monitoring.model.DeviceDTO;

import java.util.Map;
import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    private final DeviceService deviceService;


    /**
     * Constructor for DeviceController
     * @param deviceService The device service to be used
     */
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }


    /**
     * Register a new device
     * @param deviceDTO The device data transfer object containing device details
     * @return A ResponseEntity containing the result of the registration
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> registerDevice(
        @RequestBody DeviceDTO deviceDTO
    ) {

        try {
            Device device = deviceService.createDevice(deviceDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "success", true,
                    "message", "Device registered successfully",
                    "data", device
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "message", "Failed to register device: " + e.getMessage()
                ));
        }
    }

    
    /**
     * Get all registered devices
     * @return A ResponseEntity containing the list of all registered devices
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRegisteredDevices() {
        List<Device> devices = deviceService.findAllDevices();
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Devices retrieved successfully",
            "data", devices
        ));
    }


    /**
     * Get device details by ID
     * @param id The ID of the device
     * @return A ResponseEntity containing the device details
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDeviceDetails(
        @PathVariable Long id
    ) {
        
        Device device = null;
        try{
            device = deviceService.findDeviceById(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Device retrieved successfully",
                "data", device
            ));
        } catch(IllegalArgumentException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "success", false,
                    "message", e.getMessage()
                ));
        }
    }
    
    
    /**
     * Update device information
     * @param id The ID of the device to be updated
     * @param updatedDevice The updated device data transfer object
     * @return A ResponseEntity containing the updated device information
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDeviceInfo(
        @PathVariable Long id, 
        @RequestBody DeviceDTO updatedDevice
    ) {
        try {
            Device device = deviceService.updateDevice(id, updatedDevice);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Device updated successfully",
                "data", device
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "success", false,
                    "message", e.getMessage()
                ));
        }
    }
    

    /**
     * Delete a device by ID
     * @param id The ID of the device to be deleted
     * @return A ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDevice(@PathVariable Long id) {
        if(deviceService.deleteDevice(id)) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Device deleted successfully"
            ));
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "success", false,
                    "message", "Device not found"
                ));
        }
    }



    /**
     * Get total count of devices and a breakdown by status and protocol
     * @return A ResponseEntity containing the total count and breakdowns
     */
    @GetMapping("/stats/total")
    public ResponseEntity<Map<String, Object>> getTotalDevicesCount() {

        long totalCount = deviceService.getTotalDevicesCount();
        long activeCount = deviceService.getActiveDevicesCount();
        long inactiveCount = deviceService.getInactiveDevicesCount();
        
        long mqttDevices = deviceService.getDevicesCountByProtocol(Device.ProtocolType.MQTT); 
        long grpcDevices = deviceService.getDevicesCountByProtocol(Device.ProtocolType.GRPC);
        long restDevices = deviceService.getDevicesCountByProtocol(Device.ProtocolType.REST);
        
        Map<String, Long> count = Map.of(
            "total", totalCount,
            "active", activeCount,
            "inactive", inactiveCount,
            "mqtt", mqttDevices,
            "grpc", grpcDevices,
            "rest", restDevices
        );

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Total devices count retrieved successfully",
            "data", count
        ));
    }
}