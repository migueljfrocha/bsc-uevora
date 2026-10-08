package pt.uevora.tweb.roomrent.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pt.uevora.tweb.roomrent.model.Status;
import pt.uevora.tweb.roomrent.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Constructor
     * @param userService The user service
     */
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * It is used by Spring Security to authenticate a user.
     * Load user by username for authentication.
     * @param username The username of the user
     * @return UserDetails object containing user information
     * @throws UsernameNotFoundException if the user is not found or not active
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        // Check if user exists
        if (user == null) {
            throw new UsernameNotFoundException("Utilizador não encontrado: " + username);
        }

        // Check if user is active
        if (user.getStatus() != Status.ACTIVE) {
            throw new UsernameNotFoundException("Conta ainda não aprovada pelo administrador.");
        }

        // Map user roles to GrantedAuthority list
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // Return UserDetails implementation of Spring Security
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            authorities
        );
    }
}