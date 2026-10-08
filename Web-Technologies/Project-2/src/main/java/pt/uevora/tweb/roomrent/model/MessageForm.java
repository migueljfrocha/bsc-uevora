package pt.uevora.tweb.roomrent.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MessageForm {

    @NotBlank(message = "O conteúdo da mensagem é obrigatório")
    @Size(min = 10, message = "A mensagem deve ter pelo menos 10 caracteres")
    @Size(max = 300, message = "A mensagem deve ter no máximo 300 caracteres")
    private String content;

    @NotNull
    @Min(value = 1)
    private Long advertisementId;

    public MessageForm() {}
    
    public MessageForm(long id) {
        this.advertisementId = id;

    }

    public MessageForm(Long advertisementId, String content) {
        this.advertisementId = advertisementId;
        this.content = content;
    }


    public Long getAdvertisementId() {
        return advertisementId;
    }

    public String getContent() {
        return content;
    }

    
    public void setContent(String content) {
        this.content = content;
    }
    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }
}
