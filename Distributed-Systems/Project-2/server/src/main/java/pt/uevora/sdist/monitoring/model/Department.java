package pt.uevora.sdist.monitoring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
public class Department {
    @Id
    @Column(name = "department_id", nullable = false, updatable = false)
    private String departmentId;

    public Department(String departmentId) {
        this.departmentId = departmentId;
    }
    
    protected Department() {}

    // Getter
    public String getDepartmentId() {
        return departmentId;
    }
}