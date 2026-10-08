package eventastic.users;

public abstract class User {
    
    private String name;
    private String email;
    private String phoneNumber;
    private String address; 


    // Constructor
    public User(String name, String email, String phoneNumber, String address) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }


    // Setters
    public void setName (String name){
        this.name = name;
    }
    public void setEmail (String email){
        this.email = email;
    }
    public void setPhoneNumber (String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public void setAddress (String address){
        this.address = address;
    }


    // Getters
    public String getName() {
        return this.name;
    }
    public String getEmail() {
        return this.email;
    }
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public String getAddress() {
        return this.address;
    }
}