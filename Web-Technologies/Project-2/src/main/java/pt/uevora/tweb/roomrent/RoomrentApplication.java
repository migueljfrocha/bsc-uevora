package pt.uevora.tweb.roomrent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import pt.uevora.tweb.roomrent.model.Status;
import pt.uevora.tweb.roomrent.model.User;
import pt.uevora.tweb.roomrent.repositories.UserRepository;


@SpringBootApplication
public class RoomrentApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomrentApplication.class, args);
	}
	

    // Create a CommandLineRunner bean to initialize defaul admin user
    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create default admin user if not exists
            if (userRepository.findByUsername("admin") == null) {
                User admin = new User(
                    "admin",
                    "Administrador",
                    "admin@roomrent.pt",
                    passwordEncoder.encode("admin123")
                );
                admin.setRole(User.Role.ADMIN);
                admin.setStatus(Status.ACTIVE);
                userRepository.save(admin);
                System.out.println("Admin user created: (user/password) admin / admin123");
            }
        };
    }
}
