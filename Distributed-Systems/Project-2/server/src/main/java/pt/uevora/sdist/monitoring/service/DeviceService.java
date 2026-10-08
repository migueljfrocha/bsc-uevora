package pt.uevora.sdist.monitoring.service;

import pt.uevora.sdist.monitoring.model.*;
import pt.uevora.sdist.monitoring.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;
    private final DepartmentRepository departmentRepository;
    private final FloorRepository floorRepository;
    private final BuildingRepository buildingRepository;

    /**
     * Constructor for DeviceService
     * @param deviceRepository
     * @param roomRepository
     * @param departmentRepository
     * @param floorRepository
     * @param buildingRepository
     */
    public DeviceService(DeviceRepository deviceRepository,
                         RoomRepository roomRepository,
                         DepartmentRepository departmentRepository,
                         FloorRepository floorRepository,
                         BuildingRepository buildingRepository) {
        this.deviceRepository = deviceRepository;
        this.roomRepository = roomRepository;
        this.departmentRepository = departmentRepository;
        this.floorRepository = floorRepository;
        this.buildingRepository = buildingRepository;
    }

    /**
     * Creates a new Device based on the provided DeviceDTO and saves it.
     * If the associated Room, Department, Floor, or Building do not exist, they will be created.
     * @param deviceDTO The DTO containing the details of the device to be created.
     * @return The created Device entity.
     */
    @Transactional
    public Device createDevice(DeviceDTO deviceDTO) {
        
        Device.ProtocolType protocol = deviceDTO.getProtocol();
        String roomId = deviceDTO.getRoomId();
        String departmentId = deviceDTO.getDepartmentId();
        String floorId = deviceDTO.getFloorId();
        String buildingId = deviceDTO.getBuildingId();
        Boolean state = deviceDTO.getState();
        
        Room room;
        if (roomRepository.existsById(roomId)) {
            room = roomRepository.getReferenceById(roomId);
        } else {
            room = roomRepository.save(new Room(roomId));
        }

        Department department;
        if (departmentRepository.existsById(departmentId)) {
            department = departmentRepository.getReferenceById(departmentId);
        } else {
            department = departmentRepository.save(new Department(departmentId));
        }

        Floor floor;
        if (floorRepository.existsById(floorId)) {
            floor = floorRepository.getReferenceById(floorId);
        } else {
            floor = floorRepository.save(new Floor(floorId));
        }

        Building building;
        if (buildingRepository.existsById(buildingId)) {
            building = buildingRepository.getReferenceById(buildingId);
        } else {
            building = buildingRepository.save(new Building(buildingId));
        }

        Device device = new Device(protocol, room, department, floor, building, state);
        
        return deviceRepository.save(device);
    }


    /**
     * Updates the location of an existing Device.
     * @param deviceId Device ID
     * @param newRoomId Room ID
     * @param newDepartmentId Department ID
     * @param newFloorId Floor ID
     * @param newBuildingId Building ID
     * @return The updated Device entity.
     * @throws IllegalArgumentException
     */
    @Transactional
    public Device updateDeviceLocation (
        Long deviceId, 
        String newRoomId, 
        String newDepartmentId, 
        String newFloorId,
        String newBuildingId) throws IllegalArgumentException{

        Device device = findDeviceById(deviceId);

        if (!newRoomId.trim().isEmpty() && newRoomId != null) {
            Room newRoom = roomRepository
                        .findById(newRoomId)
                        .orElseGet(
                            () -> roomRepository.save(new Room (newRoomId))
                        );
            device.setRoom(newRoom);
        }

        if (!newDepartmentId.trim().isEmpty() && newDepartmentId != null) {
            Department newDepartment = departmentRepository
                        .findById(newDepartmentId)
                        .orElseGet(
                            () -> departmentRepository.save(new Department (newDepartmentId))
                        );
            device.setDepartment(newDepartment);
        }

        if (!newFloorId.trim().isEmpty() && newFloorId != null) {
            Floor newFloor = floorRepository
                        .findById(newFloorId)
                        .orElseGet(
                            () -> floorRepository.save(new Floor (newFloorId))
                        );
            device.setFloor(newFloor);
        }
        
        if (!newBuildingId.trim().isEmpty() && newBuildingId != null) {
            Building newBuilding = buildingRepository
                        .findById(newBuildingId)
                        .orElseGet(
                            () -> buildingRepository.save(new Building (newBuildingId))
                        );
            device.setBuilding(newBuilding);
        }

        return deviceRepository.save(device);
    }
    
    /**
     * Updates the protocol of an existing Device.
     * @param deviceId Device ID
     * @param newProtocol New Protocol Type
     * @return The updated Device entity.
     */
    @Transactional
    public Device updateDeviceProtocol(
        Long deviceId, 
        Device.ProtocolType newProtocol) {
            
        Device device = findDeviceById(deviceId);

        device.setProtocol(newProtocol);
        
        return deviceRepository.save(device);
    }
    
    /**
     * Updates the state of an existing Device.
     * @param deviceId Device ID
     * @param newState New State
     * @return The updated Device entity.
     */
    @Transactional
    public Device updateDeviceState(
        Long deviceId, 
        Boolean newState) {

        Device device = findDeviceById(deviceId);
        device.setState(newState);
        
        return deviceRepository.save(device);
    }


    /**
     * Updates an existing Device based on the provided DeviceDTO.
     * Only non-null fields in the DeviceDTO will be updated.
     * @param deviceId The ID of the device to be updated.
     * @param deviceDTO The DTO containing the updated details of the device.
     * @return The updated Device entity.
     */
    @Transactional
    public Device updateDevice(Long deviceId, DeviceDTO deviceDTO) {

        Device.ProtocolType protocol = deviceDTO.getProtocol();
        Boolean state = deviceDTO.getState();
        String roomId = deviceDTO.getRoomId();
        String departmentId = deviceDTO.getDepartmentId();
        String floorId = deviceDTO.getFloorId();
        String buildingId = deviceDTO.getBuildingId();


        Device device = findDeviceById(deviceId);


        if (state != null) {
            device.setState(state);
        }

        if (protocol != null) {
            device.setProtocol(protocol);
        }

        if (!roomId.trim().isEmpty() && roomId != null) {
            Room newRoom = roomRepository
                    .findById(roomId)
                    .orElseGet(
                        () -> roomRepository.save(new Room(roomId))
                    );
            device.setRoom(newRoom);
        }

        if (!departmentId.trim().isEmpty() && departmentId != null) {
            Department newDepartment = departmentRepository
                    .findById(departmentId)
                    .orElseGet(
                        () -> departmentRepository.save(new Department(departmentId))
                    );
            device.setDepartment(newDepartment);
        }

        if (!floorId.trim().isEmpty() && floorId != null) {
            Floor newFloor = floorRepository.findById(floorId)
                    .orElseGet(
                        () -> floorRepository.save(new Floor(floorId))
                    );
            device.setFloor(newFloor);
        }

        if (!buildingId.trim().isEmpty() && buildingId != null) {
            Building newBuilding = buildingRepository.findById(buildingId)
                    .orElseGet(
                        () -> buildingRepository.save(new Building(buildingId))
                    );
            device.setBuilding(newBuilding);
        }

        return deviceRepository.save(device);
    }


    /**
     * Deletes a Device by its ID.
     * @param deviceId The ID of the device to be deleted.
     * @return true if the device was deleted, false if the device was not found.
     */
    @Transactional
    public Boolean deleteDevice(Long deviceId) {

        if (deviceRepository.existsById(deviceId)) {
            deviceRepository.deleteById(deviceId);
            return true;
        }
        
        return false;
    }


    /**
     * Checks if a Device is active.
     * @param deviceId The ID of the device to be checked.
     * @return true if the device is active, false otherwise.
     */
    public Boolean isDeviceActive(Long deviceId) {
        return deviceRepository
                    .findByIdAndState(deviceId, true)
                    .isPresent();
    }


    /**
     * Finds a Device by its ID.
     * @param deviceId The ID of the device to be found.
     * @return The Device entity.
     * @throws IllegalArgumentException if the device is not found.
     */
    public Device findDeviceById(Long deviceId) throws IllegalArgumentException {
        return deviceRepository
                    .findById(deviceId) 
                    .orElseThrow(() -> new IllegalArgumentException("Device not found: " + deviceId));
    }
    

    /**
     * Finds all Devices.
     * @return A list of all Device entities.
     */
    public List<Device> findAllDevices() {
        return deviceRepository.findAll();
    }


    /**
     * Finds all Devices by their state.
     * @param state The state of the devices to be found (true for active, false for inactive).
     * @return A list of Device entities with the specified state.
     */
    public List<Device> findAllDevicesByState(Boolean state) {
        return deviceRepository.findByState(state);
    }


    /**
     * Finds all Devices by their protocol type.
     * @param protocol The protocol type of the devices to be found.
     * @return A list of Device entities with the specified protocol type.
     */
    public List<Device> findAllDevicesByProtocol(Device.ProtocolType protocol) {
        return deviceRepository.findByProtocol(protocol);
    }


    /**
     * Finds all Devices by Building ID.
     * @param buildingId The ID of the building.
     * @return A list of Device entities located in the specified building.
     */
    public List<Device> findAllDevicesByBuilding(String buildingId) {
        Building building = buildingRepository
                            .findById(buildingId)
                            .orElseThrow(() -> new IllegalArgumentException("Building not found: " + buildingId));
        return deviceRepository.findByBuilding(building);
    }


    /**
     * Finds all Devices by Department ID.
     * @param departmentId The ID of the department.
     * @return A list of Device entities located in the specified department.
     */
    public List<Device> findAllDevicesByDepartment(String departmentId) {
        Department department = departmentRepository
                            .findById(departmentId)
                            .orElseThrow(() -> new IllegalArgumentException("Department not found: " + departmentId));
        return deviceRepository.findByDepartment(department);
    }


    /**
     * Finds all Devices by Floor ID.
     * @param floorId The ID of the floor.
     * @return A list of Device entities located on the specified floor.
     */
    public List<Device> findAllDevicesByFloor(String floorId) {
        Floor floor = floorRepository
                            .findById(floorId)
                            .orElseThrow(() -> new IllegalArgumentException("Floor not found: " + floorId));
        return deviceRepository.findByFloor(floor);
    }


    /**
     * Finds all Devices by Room ID.
     * @param roomId The ID of the room.
     * @return A list of Device entities located in the specified room.
     */
    public List<Device> findAllDevicesByRoom(String roomId) {
        Room room = roomRepository
                            .findById(roomId)
                            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        return deviceRepository.findByRoom(room);
    }


    // Server Statistics

    /**
     * Gets the total count of Devices.
     * @return The total number of devices.
     */
    public long getTotalDevicesCount() {
        return deviceRepository.count();
    }

    /**
     * Gets the count of active Devices.
     * @return The number of active devices.
     */
    public long getActiveDevicesCount() {
        return deviceRepository.countByState(true);
    }
    
    /**
     * Gets the count of inactive Devices.
     * @return The number of inactive devices.
     */
    public long getInactiveDevicesCount() {
        return deviceRepository.countByState(false);
    }

    /**
     * Gets the count of Devices by Protocol Type.
     * @param protocol The protocol type.
     * @return The number of devices with the specified protocol type.
     */
    public long getDevicesCountByProtocol(Device.ProtocolType protocol) {
        return deviceRepository.countByProtocol(protocol);
    }
}