package pt.uevora.sdist.monitoring.repository;

import pt.uevora.sdist.monitoring.model.Reading;
import pt.uevora.sdist.monitoring.model.AverageReading;
import pt.uevora.sdist.monitoring.model.Device;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ReadingRepository extends JpaRepository<Reading, Long> {

    
    public List<Reading> findByDeviceId(
        Long deviceId
    );

    /**
     * Get average readings by device id within a time range
     */
    @Query(
        "SELECT AVG(r.temperature), AVG(r.humidity) " +
        "FROM Reading r " +
        "WHERE r.device.id = :deviceId " +
        "AND r.timestamp BETWEEN :start AND :end"
    )
    public AverageReading averageByDeviceIdAndTimestampBetween(
        @Param("deviceId") Long deviceId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    /**
     * Get average readings by room id within a time range
     */
    @Query(
        "SELECT AVG(r.temperature), AVG(r.humidity) " +
        "FROM Reading r " +
        "WHERE r.device.room.id = :roomId " +
        "AND r.timestamp BETWEEN :start AND :end"    
    )
    public AverageReading averageByRoomIdAndTimestampBetween(
        @Param("roomId") String roomId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    /**
     * Get average readings by department id within a time range
     */
    @Query(
        "SELECT AVG(r.temperature), AVG(r.humidity) " +
        "FROM Reading r " +
        "WHERE r.device.department.id = :departmentId " +
        "AND r.timestamp BETWEEN :start AND :end"    
    )
    public AverageReading averageByDepartmentIdAndTimestampBetween(
        @Param("departmentId") String departmentId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    /**
     * Get average readings by floor id within a time range
     */
    @Query(
        "SELECT AVG(r.temperature), AVG(r.humidity) " +
        "FROM Reading r " +
        "WHERE r.device.floor.id = :floorId " +
        "AND r.timestamp BETWEEN :start AND :end"    
    )
    public AverageReading averageByFloorIdAndTimestampBetween(
        @Param("floorId") String floorId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    /**
     * Get average readings by building id within a time range
     */
    @Query(
        "SELECT AVG(r.temperature), AVG(r.humidity) " +
        "FROM Reading r " +
        "WHERE r.device.building.id = :buildingId " +
        "AND r.timestamp BETWEEN :start AND :end"    
    )
    public AverageReading averageByBuildingIdAndTimestampBetween(
        @Param("buildingId") String buildingId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );


    /**
     * Get all readings by room id within a time range
     */
    @Query(
        "SELECT r " +
        "FROM Reading r " +
        "WHERE r.device.room.id = :roomId " +
        "AND r.timestamp BETWEEN :start AND :end"    
    )
    public List<Reading> findAllByRoomIdAndTimestampBetween(
        @Param("roomId") String roomId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    /**
     * Get all readings by department id within a time range
     */
    @Query(
        "SELECT r " +
        "FROM Reading r " +
        "WHERE r.device.department.id = :departmentId " +
        "AND r.timestamp BETWEEN :start AND :end"    
    )
    public List<Reading> findAllByDepartmentIdAndTimestampBetween(
        @Param("departmentId") String departmentId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );


    /**
     * Get all readings by floor id within a time range
     */
    @Query(
        "SELECT r " +
        "FROM Reading r " +
        "WHERE r.device.floor.id = :floorId " +
        "AND r.timestamp BETWEEN :start AND :end"    
    )
    public List<Reading> findAllByFloorIdAndTimestampBetween(
        @Param("floorId") String floorId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
    
    /**
     * Get all readings by building id within a time range
     */
    @Query(
        "SELECT r " +
        "FROM Reading r " +
        "WHERE r.device.building.id = :buildingId " +
        "AND r.timestamp BETWEEN :start AND :end"    
    )
    public List<Reading> findAllByBuildingIdAndTimestampBetween(
        @Param("buildingId") String buildingId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );


    /**
     * Get all readings by device id within a time range
     */
    @Query(
        "SELECT r " +
        "FROM Reading r " +
        "WHERE r.device.id = :deviceId " +
        "AND r.timestamp BETWEEN :start AND :end"    
    )
    public List<Reading> findAllByDeviceIdAndTimestampBetween(
        @Param("deviceId") Long deviceId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );


    // Server Statistics

    /**
     * Count readings by device protocol
     */
    @Query (
        "SELECT COUNT(r) FROM Reading r " +
        "WHERE r.device.protocol = :protocol"
    )
    public Long countByProtocol(
        @Param("protocol") Device.ProtocolType protocol
    );



    // Performane Analysis

    /**
     * Get all readings by device protocol
     */
    public List<Reading> findAllByDeviceProtocol(Device.ProtocolType protocol);

    
    /**
     * Get all readings by device protocol within a time range
     */
    @Query(
        "SELECT r " +
        "FROM Reading r " +
        "WHERE r.device.protocol = :protocol " +
        "AND r.arrivedTimestamp BETWEEN :start AND :end"    
    )
    public List<Reading> findAllByDeviceProtocolAndTimestampBetween(
        @Param("protocol") Device.ProtocolType protocol,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
}
