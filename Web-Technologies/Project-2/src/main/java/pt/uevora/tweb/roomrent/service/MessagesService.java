package pt.uevora.tweb.roomrent.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import pt.uevora.tweb.roomrent.model.Advertisement;
import pt.uevora.tweb.roomrent.model.Message;
import pt.uevora.tweb.roomrent.model.User;
import pt.uevora.tweb.roomrent.repositories.MessagesRepository;


@Service
public class MessagesService {
    private final MessagesRepository messagesRepository;
    
    /**
     * Constructor
     * @param messagesRepository The messages repository
     */
    public MessagesService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }


    /**
     * Send a message
     * Save a message to the database
     * @param message The message to send
     * @return The sent message
     */
    @Transactional
    public Message sendMessage(Message message) {
        return messagesRepository.save(message);
    }

    /**
     * Get messages received by a user with pagination.
     * @param user The user who received the messages
     * @param pageable Pagination information
     * @return A page of messages received by the user
     */
    public Page<Message> getMessagesReceivedByUser(User user, Pageable pageable) {
        return messagesRepository.findAllByUserToOrderBySentAtDesc(user, pageable);
    }

    /**
     * Get messages of an advertisement with pagination.
     * @param user The user who sent the messages
     * @param pageable Pagination information
     * @return A page of messages sent by the user
    */
    public Page<Message> getMessagesByAdvertisement(Advertisement ad, Pageable pageable) {
        return messagesRepository.findAllByAdvertisement(ad, pageable);
    }
}
