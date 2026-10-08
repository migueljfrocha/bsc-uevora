package pt.uevora.tweb.roomrent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import pt.uevora.tweb.roomrent.model.*;
import pt.uevora.tweb.roomrent.model.Advertisement.AdvertisementType;

import pt.uevora.tweb.roomrent.service.UserService;
import pt.uevora.tweb.roomrent.service.AdvertisementService;
import pt.uevora.tweb.roomrent.service.PaymentsService;

@RequestMapping("/ads")
@Controller
public class AdController {

    private final int PAGE_SIZE = 4;
    
    private final AdvertisementService advertisementService;
    private final UserService userService;
    private final PaymentsService paymentsService;

    
    /**
     * Constructor for AdController.
     * @param advertisementService the service for managing advertisements
     * @param userService the service for managing users
     * @param paymentsService the service for managing payments
     */
    public AdController(AdvertisementService advertisementService, UserService userService, PaymentsService paymentsService) {
        this.advertisementService = advertisementService;
        this.userService = userService;
        this.paymentsService = paymentsService;
    }
    

    /**
     * GetMapping for /ads/offer
     * Displays a paginated list of offer advertisements with optional filtering by location or owner.
     * @param model the model to hold attributes for the view
     * @param authentication the authentication object representing the current user
     * @param filter the type of filter to apply (location or owner)
     * @param value the value to filter by (location name or owner name)
     * @param page the page number for pagination (default is 0)
     * @return the name of the view to render the list of offers
     */
    @GetMapping("/offer")
    public String showOffers(Model model,
                             Authentication authentication,
                             @RequestParam(required = false) String filter, // location or owner
                             @RequestParam(required = false) String value, // filter value (owner_name or location)
                             @RequestParam(defaultValue = "0") int page)
    {
        int size = PAGE_SIZE;
        
        String baseUrl = "/ads/offer";
        Page<Advertisement> adsPage;

        if (filter != null && value != null && !value.trim().isEmpty()) {
            String nValue = value.trim();
            
            if(filter.equals("location")) {
                adsPage = advertisementService.getActiveAdsByLocation(nValue, AdvertisementType.OFFER, PageRequest.of(page, size));
            } else if(filter.equals("owner")) {
                adsPage = advertisementService.getActiveAdsByAdvertiserName(nValue, AdvertisementType.OFFER, PageRequest.of(page, size));
            } else {
                return "redirect:/ads/offer";
            }

            // Append filter parameters to model for pagination link concatenation on the view (Thymeleaf)
            model.addAttribute("filter", filter);
            model.addAttribute("value", nValue);
        } else {
            adsPage = advertisementService.getActiveAdsByType(AdvertisementType.OFFER, PageRequest.of(page, size));
        }

        model.addAttribute("adsPage", adsPage);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("type", "Ofertas");
        model.addAttribute("user", authentication != null ? authentication.getName() : null);
        return "ads/list";
    }


    /**
     * GetMapping for /ads/seeking
     * Displays a paginated list of seeking advertisements with optional filtering by location or owner.
     * @param model the model to hold attributes for the view
     * @param authentication the authentication object representing the current user
     * @param filter the type of filter to apply (location or owner)
     * @param value the value to filter by (location name or owner name)
     * @param page the page number for pagination (default is 0)
     * @return the name of the view to render the list of seekings
     */
    @GetMapping("/seeking")
    public String showSeekings(Model model,
                               Authentication authentication,
                               @RequestParam(required = false) String filter, // location or owner
                               @RequestParam(required = false) String value, // filter value (owner_name or location)
                               @RequestParam(defaultValue = "0") int page)
    {
        int size = PAGE_SIZE; // Ads per page
        String baseUrl = "/ads/seeking";
        Page<Advertisement> adsPage;

        if(filter != null && value != null && !value.trim().isEmpty()) {
            String nValue = value.trim();
            
            if(filter.equals("location")) {
                adsPage = advertisementService.getActiveAdsByLocation(nValue, AdvertisementType.SEARCH, PageRequest.of(page, size));
            } else if(filter.equals("owner")) {
                adsPage = advertisementService.getActiveAdsByAdvertiserName(nValue, AdvertisementType.SEARCH, PageRequest.of(page, size));
            } else {
                return "redirect:/ads/seeking";
            }

            // Append filter parameters to model for pagination link concatenation on the view (Thymeleaf)
            model.addAttribute("filter", filter);
            model.addAttribute("value", nValue);
        }else{
            adsPage = advertisementService.getActiveAdsByType(AdvertisementType.SEARCH, PageRequest.of(page, size));
        }

        model.addAttribute("adsPage", adsPage);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("type", "Procuras");
        model.addAttribute("user", authentication != null ? authentication.getName() : null);
        return "ads/list";
    }
    
    /**
     * GetMapping for /ads/{id}
     * Displays the details of a specific advertisement.
     * @param id the ID of the advertisement to display
     * @param model the model to hold attributes for the view
     * @param authentication the authentication object representing the current user
     * @return the name of the view to render the advertisement details
     */
    @GetMapping("/{id}")
    public String showAdDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Advertisement ad;
        try {
            ad = advertisementService.getAdById(id);
            model.addAttribute("ad", ad);
            model.addAttribute("messageForm", new MessageForm(ad.getId()));
        } catch (IllegalArgumentException e) {
            return "redirect:/index";
        }

        model.addAttribute("isAuthenticated", authentication != null);
        model.addAttribute("user", authentication != null ? authentication.getName() : null);
        return "ads/details";
    }


    /**
     * GetMapping for /ads/create
     * Displays the form to create a new advertisement.
     * @param model the model to hold attributes for the view
     * @param authentication the authentication object representing the current user
     * @return the name of the view to render the create advertisement form
     */
    @GetMapping("/create")
    public String showCreateAdForm(Model model, Authentication authentication) {
        model.addAttribute("adForm", new CreateAdForm());
        model.addAttribute("user", authentication != null ? authentication.getName() : null);
        return "ads/create";
    }


    /**
     * PostMapping for /ads/create
     * Handles the submission of the create advertisement form.
     * @param adForm the form object containing advertisement details
     * @param bindingResult the binding result for form validation
     * @param model the model to hold attributes for the view
     * @param redirectAttributes the redirect attributes to pass data on redirect
     * @param authentication the authentication object representing the current user
     * @return the name of the view to render after form submission
     */
    @PostMapping("/create")
    public String handleCreateAdForm(
        @Valid @ModelAttribute("adForm") CreateAdForm adForm,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes,
        Authentication authentication
    ) {        

        
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", authentication != null ? authentication.getName() : null);
            return "ads/create";
        }

        String username = authentication.getName();
        
        // Ensure user is authenticated
        if (username == null) {
            return "redirect:/login";
        }

        User currentUser = userService.findByUsername(username);

        // Create and save the advertisement
        Advertisement ad = new Advertisement(
            adForm.getType(),
            currentUser,
            adForm.getContact(),
            adForm.getRoomType(),
            adForm.getGender(),
            adForm.getPrice(),
            adForm.getLocation(),
            adForm.getDescription()
        );

        advertisementService.saveAdvertisement(ad);

        Random rd = new Random();
        // Random amount between 10 and 100
        double amount = 10 + (100 - 10) * rd.nextDouble(); 
        
        MbPayment mbPayment;
        try {
            mbPayment = paymentsService.fetchPaymentsFromAPI(amount);
        } catch (RuntimeException e) {
            model.addAttribute("apiError", "Erro ao obter informações de pagamento. Por favor, tente novamente mais tarde.");
            model.addAttribute("user", authentication != null ? authentication.getName() : null);
            return "ads/create";
        }


        BigDecimal mbAmount = new BigDecimal(mbPayment.getAmount());
        Payment payRecord = new Payment(ad, mbPayment.getEntity(), mbPayment.getMbReference(), mbAmount);
        Payment payment = paymentsService.savePayment(payRecord);
        
        redirectAttributes.addFlashAttribute("payment", payment);
        redirectAttributes.addFlashAttribute("ad", ad);
        redirectAttributes.addFlashAttribute("user", authentication != null ? authentication.getName() : null);

        return "redirect:/ads/payment";
    }

    
    /**
     * GetMapping for /ads/payment
     * Displays the payment page after creating an advertisement.
     * @param model the model to hold attributes for the view
     * @param authentication the authentication object representing the current user
     * @return the name of the view to render the payment page
     */
    @GetMapping("/payment")
    public String paymentPage(Model model, Authentication authentication) {
        // On reload, if no payment or ad in model, redirect to home to prevent duplicate submissions
        if (!model.containsAttribute("payment") || !model.containsAttribute("ad")) {
            return "redirect:/";
        }
        model.addAttribute("user", authentication != null ? authentication.getName() : null);
        return "ads/payment";
    }
}