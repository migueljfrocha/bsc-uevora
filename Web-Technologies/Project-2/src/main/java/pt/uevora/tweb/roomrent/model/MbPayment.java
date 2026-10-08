package pt.uevora.tweb.roomrent.model;

public class MbPayment {

    private String mbEntity;
    private String mbReference;
    private Double mbAmount;


    public MbPayment(String entity, String mbReference, Double amount) {
        this.mbEntity = entity;
        this.mbReference = mbReference;
        this.mbAmount = amount;
    }


    //Getters
    public String getEntity() {
        return mbEntity;
    }
    public String getMbReference() {
        return mbReference;
    }
    public Double getAmount() {
        return mbAmount;
    }


    // Setters
    public void setEntity(String entity) {
        this.mbEntity = entity;
    }
    public void setMbReference(String reference) {
        this.mbReference = reference;
    }
    public void setAmount(Double amount) {
        this.mbAmount = amount;
    }
}