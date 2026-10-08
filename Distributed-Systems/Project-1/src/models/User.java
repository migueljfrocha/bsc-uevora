package models;

import java.io.Serializable;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDateTime;

import auxiliar.status.UserStatus;


public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String[] editableAttributes = {"name", "email", "phone"};

    public int id;
    public String name;
    public String email;
    public String phone;
    public String operationalState;
    public String adminState;
    public LocalDateTime registerDate;

    /**
     * Constructor for User
     * @param id User ID
     * @param name User name
     * @param email User email
     * @param phone User phone
     * @param operationalState User operational state
     * @param adminState User admin state
     * @param registerDate User registration date
     * @return User object
     */
    public User(int id, String name, String email, String phone,
                String operationalState, String adminState, LocalDateTime registerDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.operationalState = operationalState;
        this.adminState = adminState;
        this.registerDate = registerDate;
    }


    /**
     * Default constructor for User
     */
    public User (){
        this.id = -1;
        this.name = "";
        this.email = "";
        this.phone = "";
        this.operationalState = UserStatus.INACTIVE;
        this.adminState = "rejected";
        this.registerDate = null;
    }

    /**
     * Create User object for user request
     * @param name User name
     * @param email User email
     * @param phone User phone
     * @return User object
     */
    public static User userRequest (String name, String email, String phone) {
        return new User(-1,
                        name,
                        email,
                        phone,
                        UserStatus.ACTIVE,
                        "rejected",
                        null);
    }

    /**
     * Create User object from ResultSet
     * @param rs ResultSet from database query
     * @return User object
     * @throws SQLException if SQL error occurs
     */ 
    public static User fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String nome = rs.getString("name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String opState = rs.getString("operational_state");
        String admState = rs.getString("admin_state");
        LocalDateTime reg = rs.getObject("created_at", LocalDateTime.class);
        return new User(id, nome, email, phone, opState, admState, reg);
    }


    // getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getTelephone() { return phone; }
    public String getOperationalState() { return operationalState; }
    public String getAdminState() { return adminState; }
    public LocalDateTime getRegisterDate() { return registerDate; }


    public String summary(){
        return id + " - " + name + " (" + email + ") [" + adminState + "]";
    }


    /**
     * Get array of attribute names for User entity
     * @return String[] of attribute names
     */
    public static boolean isEditableAttribute(String attribute) {
        for (String attr : User.editableAttributes) {
            if (attr.equals(attribute)) {
                return true;
            }
        }
        return false;
    }

    
    @Override
    public String toString() {
        return "User {\n" +
            "  id=" + id + ",\n" +
            "  name='" + name + "\',\n" +
            "  email='" + email + "\',\n" +
            "  phone='" + phone + "\',\n" +
            "  estadoOperacional='" + operationalState + "\',\n" +
            "  estadoAdmin='" + adminState + "\',\n" +
            "  registeredAt=" + registerDate +
            "\n}";
    }
}