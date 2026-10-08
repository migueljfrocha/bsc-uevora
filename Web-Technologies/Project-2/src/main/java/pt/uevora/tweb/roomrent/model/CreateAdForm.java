package pt.uevora.tweb.roomrent.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pt.uevora.tweb.roomrent.model.Advertisement.AdvertisementType;
import pt.uevora.tweb.roomrent.model.Advertisement.Gender;


public class CreateAdForm {

    @NotNull(message = "Tipo de anúncio é obrigatório")
    private AdvertisementType type;
    
    @NotBlank(message = "Contacto é obrigatório")
    @Size(max = 100, message = "Contacto não pode exceder 100 caracteres")
    private String contact;
    
    @NotBlank(message = "Tipo de quarto é obrigatório")
    @Size(max = 10, message = "Tipo de quarto não pode exceder 10 caracteres")
    private String roomType;
    
    @NotNull(message = "Género é obrigatório")
    private Gender gender;
    
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser positivo")
    @DecimalMax(value = "9999999.99", inclusive = false, message = "Preço excede o valor máximo permitido")
    private BigDecimal price;
    
    @NotBlank(message = "Localização é obrigatória")
    @Size(max = 100, message = "Localização não pode exceder 100 caracteres")
    private String location;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 1000, message = "Descrição deve ter entre 10 e 1000 caracteres")
    private String description;

    // Constructor
    public CreateAdForm() {}

    // Getters
    public AdvertisementType getType() {
        return type;
    }
    public String getContact() {
        return contact;
    }
    public String getRoomType() {
        return roomType;
    }
    public Gender getGender() {
        return gender;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public String getLocation() {
        return location;
    }
    public String getDescription() {
        return description;
    }


    // Setters 
    public void setType(AdvertisementType type) {
        this.type = type;
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

}