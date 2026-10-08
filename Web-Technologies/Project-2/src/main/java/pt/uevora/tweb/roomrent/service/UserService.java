package pt.uevora.tweb.roomrent.service;

import pt.uevora.tweb.roomrent.model.Status;
import pt.uevora.tweb.roomrent.model.User;
import pt.uevora.tweb.roomrent.repositories.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    
    /**
     * Constructor
     * @param userRepository The user repository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    /**
     * Get all active users with pagination
     * @param pageable Pagination information
     * @return A page of active users
     */
    public Page<User> getAllActiveUsers(Pageable pageable) {
        return userRepository.findByStatus(Status.ACTIVE, pageable);
    }

    
    /**
     * Get all inactive users with pagination
     * @param pageable Pagination information
     * @return A page of inactive users
     */
    public Page<User> getAllInactiveUsers(Pageable pageable) {
        return userRepository.findByStatus(Status.INACTIVE, pageable);
    }


    /**
     * Find a user by username
     * @param username the username to search for
     * @return the user with the given username, or null if not found
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    /**
     * Find a user by email
     * @param email the email to search for
     * @return the user with the given email, or null if not found
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    
    /**
     * Save a user
     * @param user the user to save
     * @return the saved user
     */
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Find a user by ID
     * @param id the ID of the user
     * @return an Optional containing the user if found, or empty if not found
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Check if a user exists by username
     * @param username the username to check
     * @return true if a user with the given username exists, false otherwise
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if a user exists by email
     * @param email the email to check
     * @return true if a user with the given email exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Approve a user by setting their status to ACTIVE
     * @param userId the ID of the user to approve
     * @throws IllegalArgumentException if the user ID is invalid
     */
    @Transactional
    public void approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
    }
}