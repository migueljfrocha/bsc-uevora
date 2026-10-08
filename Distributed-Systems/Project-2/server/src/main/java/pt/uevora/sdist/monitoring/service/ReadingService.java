package pt.uevora.sdist.monitoring.service;

import pt.uevora.sdist.monitoring.model.*;
import pt.uevora.sdist.monitoring.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class ReadingService {
    
    private final ReadingRepository readingRepository;
    private final RoomRepository roomRepository;
    private final DepartmentRepository departmentRepository;
    private final FloorRepository floorRepository;
    private final BuildingRepository buildingRepository;

    private final DeviceService deviceService;

    /**
     * Constructor for ReadingService.
     * @param readingRepository The repository for Reading entities.
     * @param roomRepository The repository for Room entities.
     * @param departmentRepository The repository for Department entities.
     * @param floorRepository The repository for Floor entities.
     * @param buildingRepository The repository for Building entities.
     * @param deviceService The service for Device entities.
     */
    public ReadingService(ReadingRepository readingRepository, 
                          RoomRepository roomRepository, 
                          DepartmentRepository departmentRepository, 
                          FloorRepository floorRepository, 
                          BuildingRepository buildingRepository,
                          DeviceService deviceService) {
        this.readingRepository = readingRepository;
        this.roomRepository = roomRepository;
        this.departmentRepository = departmentRepository;
        this.floorRepository = floorRepository;
        this.buildingRepository = buildingRepository;
        this.deviceService = deviceService;
    }
    

    /**
     * Creates a new reading for a device.
     * @param deviceId The ID of the device.
     * @param temperature The temperature reading.
     * @param humidity The humidity reading.
     * @param timestamp The timestamp of the reading.
     * @param fromProtocol The protocol type of the device.
     * @return The created Reading entity.
     * @throws IllegalArgumentException if the device is inactive or uses a different protocol.
     */
    @Transactional
    public Reading createReading(
        Long deviceId,
        Double temperature,
        Double humidity,
        LocalDateTime timestamp,
        Device.ProtocolType fromProtocol
    ){
        // Validate device
        Device device = deviceService.findDeviceById(deviceId);
        if(!device.getState()){
            throw new IllegalArgumentException("Device with id " + deviceId + " is inactive.");
        }
        if(device.getProtocol() != fromProtocol){
            throw new IllegalArgumentException("Device with id " + deviceId + " uses a different protocol.");
        }

        // Create and save reading
        Reading reading = new Reading(
            device,
            temperature,
            humidity,
            timestamp
        );
        return readingRepository.save(reading);
    }


    /**
     * Gets average readings for a specific room within a time interval.
     * @param roomId The ID of the room.
     * @param start The start of the time interval.
     * @param end The end of the time interval.
     * @return The average readings for the room.
     * @throws IllegalArgumentException if the room does not exist.
     */
    public AverageReading getAverageReadingsByRoomId(
        String roomId,
        LocalDateTime start,
        LocalDateTime end
    ) throws IllegalArgumentException {

        if(!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }
        return readingRepository.averageByRoomIdAndTimestampBetween(
            roomId,
            start,
            end
        );
    }


    /**
     * Gets raw readings for a specific room within a time interval.
     * @param roomId The ID of the room.
     * @param start The start of the time interval.
     * @param end The end of the time interval.
     * @return The list of raw readings for the room.
     * @throws IllegalArgumentException if the room does not exist.
     */
    public List<Reading> getRawReadingsByRoomId(
        String roomId,
        LocalDateTime start,
        LocalDateTime end
    ) throws IllegalArgumentException {

        if(!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }
        return readingRepository.findAllByRoomIdAndTimestampBetween(
            roomId,
            start,
            end
        );
    }
    

    /**
     * Gets average readings for a specific department within a time interval.
     * @param departmentId The ID of the department.
     * @param start The start of the time interval.
     * @param end The end of the time interval.
     * @return The average readings for the department.
     * @throws IllegalArgumentException if the department does not exist.
     */
    public AverageReading getAverageReadingsByDepartmentId(
        String departmentId,
        LocalDateTime start,
        LocalDateTime end
    ) throws IllegalArgumentException {

        if(!departmentRepository.existsById(departmentId)) {
            throw new IllegalArgumentException("Department with id " + departmentId + " does not exist.");
        }
        return readingRepository.averageByDepartmentIdAndTimestampBetween(
            departmentId,
            start,
            end
        );
    }


    /**
     * Gets raw readings for a specific department within a time interval.
     * @param departmentId The ID of the department.
     * @param start The start of the time interval.
     * @param end The end of the time interval.
     * @return The list of raw readings for the department.
     * @throws IllegalArgumentException if the department does not exist.
     */
    public List<Reading> getRawReadingsByDepartmentId(
        String departmentId,
        LocalDateTime start,
        LocalDateTime end
    ) throws IllegalArgumentException {

        if(!departmentRepository.existsById(departmentId)) {
            throw new IllegalArgumentException("Department with id " + departmentId + " does not exist.");
        }
        return readingRepository.findAllByDepartmentIdAndTimestampBetween(
            departmentId,
            start,
            end
        );
    }


    /**
     * Gets average readings for a specific floor within a time interval.
     * @param floorId The ID of the floor.
     * @param start The start of the time interval.
     * @param end The end of the time interval.
     * @return The average readings for the floor.
     * @throws IllegalArgumentException if the floor does not exist.
     */
    public AverageReading getAverageReadingsByFloorId(
        String floorId,
        LocalDateTime start,
        LocalDateTime end
    ) throws IllegalArgumentException {

        if(!floorRepository.existsById(floorId)) {
            throw new IllegalArgumentException("Floor with id " + floorId + " does not exist.");
        }
        return readingRepository.averageByFloorIdAndTimestampBetween(
            floorId,
            start,
            end
        );
    }


    /**
     * Gets raw readings for a specific floor within a time interval.
     * @param floorId The ID of the floor.
     * @param start The start of the time interval.
     * @param end The end of the time interval.
     * @return The list of raw readings for the floor.
     * @throws IllegalArgumentException if the floor does not exist.
     */
    public List<Reading> getRawReadingsByFloorId(
        String floorId,
        LocalDateTime start,
        LocalDateTime end
    ) throws IllegalArgumentException {

        if(!floorRepository.existsById(floorId)) {
            throw new IllegalArgumentException("Floor with id " + floorId + " does not exist.");
        }
        return readingRepository.findAllByFloorIdAndTimestampBetween(
            floorId,
            start,
            end
        );
    }


    /**
     * Gets average readings for a specific building within a time interval.
     * @param buildingId The ID of the building.
     * @param start The start of the time interval.
     * @param end The end of the time interval.
     * @return The average readings for the building.
     * @throws IllegalArgumentException if the building does not exist.
     */
    public AverageReading getAverageReadingsByBuildingId(
        String buildingId,
        LocalDateTime start,
        LocalDateTime end
    ) throws IllegalArgumentException {

        if(!buildingRepository.existsById(buildingId)) {
            throw new IllegalArgumentException("Building with id " + buildingId + " does not exist.");
        }
        return readingRepository.averageByBuildingIdAndTimestampBetween(
            buildingId,
            start,
            end
        );
    }


    /**
     * Gets raw readings for a specific building within a time interval.
     * @param buildingId The ID of the building.
     * @param start The start of the time interval.
     * @param end The end of the time interval.
     * @return The list of raw readings for the building.
     * @throws IllegalArgumentException if the building does not exist.
     */
    public List<Reading> getRawReadingsByBuildingId(
        String buildingId,
        LocalDateTime start,
        LocalDateTime end
    ) throws IllegalArgumentException {

        if(!buildingRepository.existsById(buildingId)) {
            throw new IllegalArgumentException("Building with id " + buildingId + " does not exist.");
        }
        return readingRepository.findAllByBuildingIdAndTimestampBetween(
            buildingId,
            start,
            end
        );
    }


    /**
     * Gets average readings for a specific device within a time interval.
     * @param deviceId The ID of the device.
     * @param start The start of the time interval.
     * @param end The end of the time interval.
     * @return The average readings for the device.
     * @throws IllegalArgumentException if the device does not exist.
     */
    public AverageReading getAverageReadingsByDevice(
        Long deviceId,
        LocalDateTime start,
        LocalDateTime end
    ) throws IllegalArgumentException {

        deviceService.findDeviceById(deviceId);
        
        return readingRepository.averageByDeviceIdAndTimestampBetween(
            deviceId,
            start,
            end
        );
    }


    /**
     * Gets raw readings for a specific device within a time interval.
     * @param deviceId The ID of the device.
     * @param start The start of the time interval.
     * @param end The end of the time interval.
     * @return The list of raw readings for the device.
     * @throws IllegalArgumentException if the device does not exist.
     */
    public List<Reading> getRawReadingsByDeviceId(
        Long deviceId,
        LocalDateTime start,
        LocalDateTime end
    ) throws IllegalArgumentException {

        deviceService.findDeviceById(deviceId);

        return readingRepository.findAllByDeviceIdAndTimestampBetween(
            deviceId,
            start,
            end
        );
    }


    // Server Statistics


    /**
     * Gets the total count of readings in the system.
     * @return The total number of readings.
     */
    public long getTotalReadingsCount() {
        return readingRepository.count();
    }
    
    /**
     * Gets the total count of readings for a specific device protocol.
     * @param protocol The protocol type of the device.
     * @return The total number of readings for the specified protocol.
     */
    public long getTotalReadingsCountByDeviceProtocol(Device.ProtocolType protocol) {
        return readingRepository.countByProtocol(protocol);
    }

    
    // Performance Analysis

    /**
     * Gets all readings for a specific device protocol.
     * @param protocol The protocol type of the device.
     * @return The list of readings for the specified protocol.
     */
    public List<Reading> getAllReadingsByDeviceProtocol(Device.ProtocolType protocol) {
        return readingRepository.findAllByDeviceProtocol(protocol);
    }

    /**
     * Gets all readings for a specific device protocol within a time interval.
     * @param protocol The protocol type of the device.
     * @param start The start of the time interval.
     * @param end The end of the time interval.
     * @return The list of readings for the specified protocol within the time interval.
     */
    public List<Reading> getAllReadingsByDeviceProtocolAndTimeInterval(
        Device.ProtocolType protocol,
        LocalDateTime start,
        LocalDateTime end
    ) {
        return readingRepository.findAllByDeviceProtocolAndTimestampBetween(
            protocol,
            start,
            end
        );
    }
}
