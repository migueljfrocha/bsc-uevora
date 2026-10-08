package pt.uevora.tweb.roomrent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import pt.uevora.tweb.roomrent.model.Advertisement;
import pt.uevora.tweb.roomrent.model.Advertisement.AdvertisementType;
import pt.uevora.tweb.roomrent.service.AdvertisementService;

import java.util.List;

@RequestMapping({"/", "/index"})
@Controller
public class HomeController {

    private final AdvertisementService advertisementService;

    
    /**
     * Constructor for HomeController.
     * @param advertisementService the service for managing advertisements
     */
    public HomeController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }
    
    
    /**
     * Handles GET requests to the home page.
     * @param model the model to pass data to the view
     * @param authentication the authentication object containing user details
     * @return the name of the view to render
     */
    @GetMapping
    public String index(Model model, Authentication authentication) {
        List<Advertisement> offerAds = advertisementService.get3MostRecentActiveAds(AdvertisementType.OFFER);
        List<Advertisement> searchAds = advertisementService.get3MostRecentActiveAds(AdvertisementType.SEARCH);
        
        model.addAttribute("offerAds", offerAds);
        model.addAttribute("searchAds", searchAds);
        model.addAttribute("user", authentication != null ? authentication.getName() : null);
        
        return "index";
    }    
} 
