package pt.uevora.sdist.monitoring.repository;

import pt.uevora.sdist.monitoring.model.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    // Find device by ID and state (active/inactive)
    Optional<Device> findByIdAndState(Long id, boolean state);
    
    // Find devices by state (active/inactive)
    List<Device> findByState(boolean state);
    
    // Find devices by protocol type
    List<Device> findByProtocol(Device.ProtocolType protocol);
    
    // Find devices by building
    List<Device> findByBuilding(Building building);
    
    // Find devices by department
    List<Device> findByDepartment(Department department);
    
    // Find devices by floor
    List<Device> findByFloor(Floor floor);
    
    // Find devices by room
    List<Device> findByRoom(Room room);
    
    
    // Server Statistics 

    // Total number of devices
    Long countByState(boolean state);

    // Number of devices by protocol type
    Long countByProtocol(Device.ProtocolType protocol);
}