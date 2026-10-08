package eventastic.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private int id;
    private BigDecimal amount;
    private String IBAN;
    private LocalDateTime timestamp;
    private String transferDescription;
    
    public Payment(int id, BigDecimal amount, String IBAN, LocalDateTime timestamp, String description) {
        this.id = id;
        this.amount = amount;
        this.IBAN = IBAN;
        this.timestamp = timestamp;
        this.transferDescription = description;
    }


    /**
     * toString method for printing the Payment details.
     * Prints in the following format:
     * ------------- Payment Details -------------
     * Amount: [amount] EUR
     * IBAN: [IBAN]
     * Add the following description to your transfer:
     *    [transferDescription]
     * -------------------------------------------
     * @return A formatted string representing the Payment details.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------------- Payment Details -------------\n");
        sb.append(String.format("Amount: %.2f EUR\n", amount));
        sb.append("IBAN: " + IBAN + "\n");
        sb.append("Add the following description to your transfer:\n\t" + transferDescription + "\n");
        sb.append("-------------------------------------------\n");
        return sb.toString();
    }

    
    // Getters
    public int getId() {
        return id;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public String getIBAN() {
        return IBAN;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
