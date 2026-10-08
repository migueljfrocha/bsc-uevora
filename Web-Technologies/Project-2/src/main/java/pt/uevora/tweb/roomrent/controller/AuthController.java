package pt.uevora.tweb.roomrent.controller;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import pt.uevora.tweb.roomrent.model.RegisterForm;
import pt.uevora.tweb.roomrent.model.User;
import pt.uevora.tweb.roomrent.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for AuthController.
     * @param userService the service for managing users
     * @param passwordEncoder the password encoder for encoding user passwords
     */
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * GetMapping for /login
     * Displays the login page.
     * @param model the model to pass data to the view
     * @param error the error message, if any
     * @param logout the logout message, if any
     * @return the login view
     */
    @GetMapping("/login")
	public String showLoginPage(
            Model model, 
            @RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {
		
        if (error != null) {
			model.addAttribute("error", "Credenciais inválidas.");
		}
		if (logout != null) {
			model.addAttribute("msg", "Logout efetuado com sucesso.");
		}
		return "auth/login";
	}


    /**
     * GetMapping for /register
     * Displays the registration page.
     * @param model the model to pass data to the view
     * @return the registration view
     */
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "auth/register";
    }

    
    /**
     * PostMapping for /register
     * Handles user registration.
     * @param form the registration form data
     * @param bindingResult the binding result for validation
     * @param model the model to pass data to the view
     * @return the registration view
     */
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registerForm") RegisterForm form,
            BindingResult bindingResult,
            Model model
    ){
        // Check if both passwords match
        if (! form.getPassword().equals(form.getConfirmationPassword())) {
            bindingResult.rejectValue("confirmationPassword", "error.registerForm", "As passwords não coincidem.");
        }

        // Check if username already exists
        if (userService.existsByUsername(form.getUsername())) {
            bindingResult.rejectValue("username", "error.registerForm", "Username já existe.");
        }

        // Check if email already exists
        if (userService.existsByEmail(form.getEmail())) {
            bindingResult.rejectValue("email", "error.registerForm", "Email já existe.");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        User newUser = new User(
            form.getUsername(),
            form.getName(),
            form.getEmail(),
            passwordEncoder.encode(form.getPassword())
        );
        userService.save(newUser);

        model.addAttribute("success", "Registo efetuado com sucesso!  Aguarde aprovação do administrador.");
        return "auth/register";
    }
    
}