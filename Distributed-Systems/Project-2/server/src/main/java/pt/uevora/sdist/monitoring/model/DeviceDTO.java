package pt.uevora.sdist.monitoring.model;

public class DeviceDTO {
    private Long id;
    private Device.ProtocolType protocol;
    private String roomId;
    private String departmentId;
    private String floorId;
    private String buildingId;
    private Boolean state;

    public DeviceDTO() {
    }

    public DeviceDTO(Device.ProtocolType protocol, String roomId, String departmentId, String floorId, String buildingId, Boolean state) {
        this.protocol = protocol;
        this.roomId = roomId;
        this.departmentId = departmentId;
        this.floorId = floorId;
        this.buildingId = buildingId;
        this.state = state;
    }

    public DeviceDTO(Long id, Device.ProtocolType protocol, String roomId, String departmentId, String floorId, String buildingId, Boolean state) {
        this.id = id;
        this.protocol = protocol;
        this.roomId = roomId;
        this.departmentId = departmentId;
        this.floorId = floorId;
        this.buildingId = buildingId;
        this.state = state;
    }
    
    // Getters
    public Long getId() {
        return id;
    }

    public Device.ProtocolType getProtocol() {
        return protocol;
    }

    public String getRoomId() {
        return roomId;
    }    
    
    public String getDepartmentId() {
        return departmentId;
    }
    public String getFloorId() {
        return floorId;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public Boolean getState() {
        return state;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setProtocol(Device.ProtocolType protocol) {
        this.protocol = protocol;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }
    public void setState(Boolean state) {
        this.state = state;
    }
}