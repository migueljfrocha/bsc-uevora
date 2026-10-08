package pt.uevora.tweb.roomrent.repositories;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pt.uevora.tweb.roomrent.model.Status;
import pt.uevora.tweb.roomrent.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    // Find user by username
    User findByUsername(String username);

    // Find user by email
    User findByEmail(String email);

    // Get all users with pagination
    Page<User> findAll(Pageable pageable);

    // Get all users by status with pagination
    Page<User> findByStatus(Status status, Pageable pageable);
    
    // Check if username exists
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);
}
