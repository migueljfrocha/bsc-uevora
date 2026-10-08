package pt.uevora.tweb.roomrent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import org.springframework.security.core.Authentication;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import pt.uevora.tweb.roomrent.model.Advertisement;
import pt.uevora.tweb.roomrent.model.User;
import pt.uevora.tweb.roomrent.service.AdvertisementService;
import pt.uevora.tweb.roomrent.service.UserService;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private final int PAGE_SIZE = 4;
    
    UserService userService;
    AdvertisementService adService;


    /**
     * Constructor for AdminController.
     * @param userService the service for managing users
     * @param adService the service for managing advertisements
     */
    public AdminController(UserService userService, AdvertisementService adService) {
        this.userService = userService;
        this.adService = adService;
        
    }
    
    
    /**
     * GetMapping for /admin
     * Displays the admin panel.
     * @param model the model to pass data to the view
     * @param authentication the authentication object containing user details
     * @return the admin panel view
     */
    @GetMapping
    public String adminPanel(Model model, Authentication authentication) {
        model.addAttribute("user", authentication != null ? authentication.getName() : null);
        return "admin/panel";
    }


    /**
     * GetMapping for /manage-ads
     * Displays the manage ads page with a list of advertisements.
     * @param page the page number for pagination
     * @param model the model to pass data to the view
     * @param authentication the authentication object containing user details
     * @return the manage ads view
     */
    @GetMapping("/manage-ads")
    public String manageAds(
        @RequestParam(defaultValue = "0") int page,
        Model model,
        Authentication authentication
    ) {

        String baseUrl = "/admin/manage-ads";
        Page<Advertisement> adsPage = adService.getAllAdvertisementsOrderByCreatedAtDesc(PageRequest.of(page, PAGE_SIZE));

        model.addAttribute("adsPage", adsPage);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("user", authentication != null ? authentication.getName() : null);
        
        return "admin/manage-ads";
    }

    
    /**
     * PostMapping to /activate-ad/{id}
     * Activate an advertisement by its ID.
     * @param id The ID of the advertisement to activate.
     * @param redirectAttributes Attributes for flash messages.
     * @return Redirect to the manage ads page.
     */
    @PostMapping("/activate-ad/{id}")
    public String activateAd(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adService.activateAdvertisement(id);
        redirectAttributes.addFlashAttribute("message", "Anúncio ativado com sucesso!");
        return "redirect:/admin/manage-ads";
    }

        
    /**
     * PostMapping to /deactivate-ad/{id}
     * Deactivate an advertisement by its ID.
     * @param id The ID of the advertisement to deactivate.
     * @param redirectAttributes Attributes for flash messages.
     * @return Redirect to the manage ads page.
     */
    @PostMapping("/deactivate-ad/{id}")
    public String deactivateAd(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adService.deactivateAdvertisement(id);
        redirectAttributes.addFlashAttribute("message", "Anúncio desativado com sucesso!");
        return "redirect:/admin/manage-ads";
    }


    /**
     * GetMapping for /manage-users
     * Displays the manage users page with a list of inactive users.
     * @param page the page number for pagination
     * @param model the model to pass data to the view
     * @param authentication the authentication object containing user details
     * @return the manage users view
     */
    @GetMapping("/manage-users")
    public String manageUsers(
        @RequestParam(defaultValue = "0") int page, 
        Model model, 
        Authentication authentication) 
    {
        String baseUrl = "/admin/manage-users";

        Page<User> usersPage = userService.getAllInactiveUsers(PageRequest.of(page, PAGE_SIZE));

        model.addAttribute("usersPage", usersPage);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("user", authentication != null ? authentication.getName() : null);

        return "admin/manage-users";
    }


    /**
     * PostMapping to /approve-user/{id}
     * Approve a user by their ID.
     * @param id The ID of the user to approve.
     * @param redirectAttributes Attributes for flash messages.
     * @return Redirect to the manage users page.
     */
    @PostMapping("/approve-user/{id}")
    public String approveUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.approveUser(id);
        redirectAttributes.addFlashAttribute("message", "Utilizador aprovado com sucesso!");
        return "redirect:/admin/manage-users";
    }
}
