package pt.uevora.tweb.roomrent.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name = "advertisements")

    public class Advertisement {

    public enum AdvertisementType {
        OFFER,
        SEARCH
    }

    public enum Gender{
        MALE, 
        FEMALE, 
        INDIFFERENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ad_type", nullable = false)
    private AdvertisementType adType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "advertiser_id", nullable = false)
    private User advertiser;

    @Column(name = "contact", nullable = false, length = 100)
    private String contact;

    @Column(name = "room_type", nullable = false, length = 10)
    private String roomType;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 16)
    private Gender gender;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private Status status = Status.INACTIVE;

    // Constructors
    protected Advertisement() {}

    public Advertisement(
                AdvertisementType adType, 
                User advertiser, 
                String contact, 
                String roomType, 
                Gender gender, 
                BigDecimal price, 
                String location, 
                String description) 
    {
        this.adType = adType;
        this.advertiser = advertiser;
        this.contact = contact;
        this.roomType = roomType;
        this.gender = gender;
        this.price = price;
        this.location = location;
        this.description = description;
    }
    

    // Getters
    public Long getId() {
        return this.id;
    }
    public AdvertisementType getAdType() {
        return this.adType;
    }
    public User getAdvertiser() {
        return this.advertiser;
    }
    public String getContact() {
        return this.contact;
    }
    public String getRoomType() {
        return this.roomType;
    }
    public Gender getGender() {
        return this.gender;
    }
    public BigDecimal getPrice() {
        return this.price;
    }
    public String getLocation() {
        return this.location;
    }
    public String getDescription() {
        return this.description;
    }
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }  
    public Status getStatus() {
        return this.status;
    }
    

    // Setters
    public void setAdType(AdvertisementType adType) {
        this.adType = adType;
    }
    public void setAdvertiser(User advertiser) {
        this.advertiser = advertiser;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

}
