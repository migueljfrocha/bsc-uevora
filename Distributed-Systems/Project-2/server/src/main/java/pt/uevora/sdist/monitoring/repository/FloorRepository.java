package pt.uevora.sdist.monitoring.repository;

import pt.uevora.sdist.monitoring.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorRepository extends JpaRepository<Floor, String> {}