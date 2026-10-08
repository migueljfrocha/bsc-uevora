package pt.uevora.sdist.monitoring.repository;

import pt.uevora.sdist.monitoring.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {}