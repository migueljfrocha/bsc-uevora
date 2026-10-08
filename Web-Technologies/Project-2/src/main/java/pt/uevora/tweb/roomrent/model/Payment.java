package pt.uevora.tweb.roomrent.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ad_payment")
public class Payment {

    public enum PaymentStatus {
        PENDING, 
        REJECTED, 
        PAID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;

    @Column(name = "mb_entity", nullable = false)
    private String mbEntity;

    @Column(name = "mb_reference", nullable = false)
    private String mbReference;

    @Column(name = "mb_amount", nullable = false)
    private BigDecimal mbAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;


    protected Payment() {}
    
    public Payment(Advertisement advertisement, String mbEntity, String mbReference, BigDecimal mbAmount) {
        this.advertisement = advertisement;
        this.mbEntity = mbEntity;
        this.mbReference = mbReference;
        this.mbAmount = mbAmount;
    }

    // Getters
    public Long getPaymentId() {
        return paymentId;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public String getMbEntity() {
        return mbEntity;
    }

    public String getMbReference() {
        return mbReference;
    }
    
    public BigDecimal getMbAmount() {
        return mbAmount;
    }
    public LocalDateTime getPaidAt() {
        return paidAt;
    }
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
}
