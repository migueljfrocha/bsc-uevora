package eventastic.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AdditionalOption {

    private String name;
    private String description;
    private BigDecimal price;
    private boolean isRequired;

    // Constructor
    public AdditionalOption(String name, String description, BigDecimal price, boolean isRequired) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.isRequired = isRequired;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("  - %s - %.2f EUR %s\n", 
            this.name, 
            this.price,
            this.isRequired ? " (REQUIRED)" : " (Optional)"));
        sb.append("    " + this.description + "\n");

        return sb.toString();
    }


    /**
     * Edits the additional option with new values.
     * @param description String addon description
     * @param price BigDecimal addon price
     */
    public void editAdditionalOption(String description, BigDecimal price) {
        this.description = description;
        this.price = price;
    }


    // Getters
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public boolean isRequired() {
        return isRequired;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public void setRequired(boolean required) {
        isRequired = required;
    }
}
