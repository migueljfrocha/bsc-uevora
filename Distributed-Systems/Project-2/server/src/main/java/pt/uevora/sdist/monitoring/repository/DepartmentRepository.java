package pt.uevora.sdist.monitoring.repository;

import pt.uevora.sdist.monitoring.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {}