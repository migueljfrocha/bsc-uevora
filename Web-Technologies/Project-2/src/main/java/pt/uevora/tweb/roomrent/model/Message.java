package pt.uevora.tweb.roomrent.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long messageId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id_from", nullable = false)
    private User userFrom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id_to", nullable = false)
    private User userTo;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();


    protected Message() {}

    
    public Message(User userFrom, User userTo, Advertisement advertisement, String content) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.advertisement = advertisement;
        this.content = content;
    }

    
    // Getters
    public Long getMessageId() {
        return messageId;
    }
    public User getUserFrom() {
        return userFrom;
    }
    public User getUserTo() {
        return userTo;
    }
    public Advertisement getAdvertisement() {
        return advertisement;
    }
    public String getContent() {
        return content;
    }
    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
