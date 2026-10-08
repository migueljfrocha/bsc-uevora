package pt.uevora.tweb.roomrent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import pt.uevora.tweb.roomrent.model.*;
import pt.uevora.tweb.roomrent.service.*;

@RequestMapping("/messages")
@Controller
public class MessagesController {

    private final int PAGE_SIZE = 4;

    private final UserService userService;
    private final AdvertisementService advertisementService;
    private final MessagesService messageService;

    
    /**
     * Constructor for MessagesController.
     * @param userService the service for managing users
     * @param advertisementService the service for managing advertisements
     * @param messageService the service for managing messages
     */
    public MessagesController(
        UserService userService,
        AdvertisementService advertisementService,
        MessagesService messageService) 
    {
        this.userService = userService;
        this.advertisementService = advertisementService;
        this.messageService = messageService;
    }
    

    /**
     * PostMapping to /messages
     * Send a message related to an advertisement.
     * @param messageForm the form containing message details
     * @param bindingResult the result of binding the form
     * @param redirectAttributes attributes for redirect scenarios
     * @param authentication the authentication object
     * @return the redirect URL
     */
    @PostMapping
    public String sendMessage (
        @Valid @ModelAttribute("messageForm") MessageForm messageForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Authentication authentication
    ){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao enviar mensagem, deve ter entre 10 e 300 caracteres.");
            return "redirect:/ads/" + messageForm.getAdvertisementId();
        }

        String username = authentication.getName();
        // Ensure user is authenticated
        if (username == null) {
            return "redirect:/login";
        }

        long adId = messageForm.getAdvertisementId();

        User sender = userService.findByUsername(username);
        Advertisement ad = advertisementService.getAdById(adId);
        User recipient = ad.getAdvertiser();
        
        if (sender.getUserId().equals(recipient.getUserId())) {
            redirectAttributes.addFlashAttribute("error", "Não pode enviar mensagem para si próprio.");
            return "redirect:/ads/" + adId;
        }

        Message message = new Message(sender, recipient, ad, messageForm.getContent());
        messageService.sendMessage(message);

        redirectAttributes.addFlashAttribute("success", "Mensagem enviada com sucesso!");
        return "redirect:/ads/" + adId;
    }

    
    /**
     * View inbox with received messages
     * @param page the page number
     * @param model the model to hold attributes
     * @param authentication the authentication object
     * @return the inbox view
     */
    @GetMapping("/inbox")
    public String viewInbox(
        @RequestParam(defaultValue = "0") int page,
        Model model, 
        Authentication authentication) 
    {
        String username = authentication.getName();
        // Ensure user is authenticated
        if (username == null) {
            return "redirect:/login";
        }
        User currentUser = userService.findByUsername(username);
        Page<Message> messagesPage = messageService.getMessagesReceivedByUser(currentUser, PageRequest.of(page, PAGE_SIZE));
     
        model.addAttribute("messages", messagesPage);
        model.addAttribute("user", username);
        model.addAttribute("baseUrl", "/messages/inbox");
        
        return "messages/inbox";
    }
}