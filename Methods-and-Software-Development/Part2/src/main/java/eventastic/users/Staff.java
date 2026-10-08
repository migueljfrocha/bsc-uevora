package eventastic.users;

public abstract class Staff extends User {
    int id;
    // String password; // Not implemented

    public Staff(int id, String name, String email, String phoneNumber, String address) {
        super(name, email, phoneNumber, address);
        this.id = id;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }


    // Getters
    public int getId() {
        return id;
    }
}