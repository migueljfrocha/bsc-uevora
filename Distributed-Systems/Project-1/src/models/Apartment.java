package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import auxiliar.status.ApartmentStatus;


public class Apartment implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String[] editableAttributes = {"name", "location", "price_per_night", "type"};

    public int id;
    public String name;
    public String location;
    public BigDecimal pricePerStay;
    public int type;
    public String operationalState;
    public String adminState;
    public int ownerId;
    
    /**
     * Constructor for Apartment class
     * @param id ID of the apartment
     * @param name Name of the apartment
     * @param location Location of the apartment
     * @param pricePerStay Price per stay of the apartment
     * @param type Type of the apartment
     * @param operationalState Operational state of the apartment
     * @param adminState Admin state of the apartment
     * @param ownerId Owner ID of the apartment
     */
    public Apartment(int id, String name, String location, BigDecimal pricePerStay,
                     int type, String operationalState, String adminState, int ownerId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.pricePerStay = pricePerStay;
        this.type = type;
        this.operationalState = operationalState;
        this.adminState = adminState;
        this.ownerId = ownerId;
    }

    /**
     * Create an Apartment object for a new apartment request
     * @param name Name of the apartment
     * @param location Location of the apartment
     * @param pricePerStay Price per stay of the apartment
     * @param type Type of the apartment
     * @return Apartment object
     */
    public static Apartment apartmentRequest(String name, String location, BigDecimal pricePerStay,
                                             int type) {
        return new Apartment(-1, 
                                name, 
                                location, 
                                pricePerStay, 
                                type, 
                                ApartmentStatus.AVAILABLE, 
                                "rejected",
                                -1);
    }

    
    /**
     * Create an Apartment object from a ResultSet
     * @param rs ResultSet from a database query
     * @return Apartment object
     * @throws SQLException if a database access error occurs
     */
    public static Apartment fromResultSet(ResultSet rs) throws SQLException {
        return new Apartment(
            rs.getInt("apartment_id"),
            rs.getString("name"),
            rs.getString("location"),
            rs.getBigDecimal("price_per_night"),
            rs.getInt("type"),
            rs.getString("operational_state"),
            rs.getString("admin_state"),
            rs.getInt("owner_id")
        );
    }


    /**
     * Check if an attribute is editable
     * @param attribute Attribute name
     * @return true if editable, false otherwise
     */
    public static boolean isEditableAttribute(String attribute) {
        for (String att : Apartment.editableAttributes) {
            if (att.equals(attribute)) {
                return true;
            }
        }
        return false;
    }

    // getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public BigDecimal getPricePerStay() { return pricePerStay; }
    public int getType() { return type; }
    public String getOperacionalState() { return operationalState; }
    public String getAdminState() { return adminState; }
    public int getOwnerId() { return ownerId; }

    /**
     * Get a brief summary of the apartment
     * @return Summary string
     */
    public String summary() {
        return id + " - " + name + " (" + location + ") [" + adminState + "]";
    }

    /**
     * Get the status of the apartment
     * @return Status string
     */
    public String myStatus(){
        return "Apartment " + id +
               "\nOperational State: " + operationalState +
               "\nAdmin State: " + adminState;
    }


    @Override
    public String toString() {
        return "Apartment{\n" +
               "  id=" + id + ",\n" +
               "  name='" + name + '\'' + ",\n" +
               "  location='" + location + '\'' + ",\n" +
               "  pricePerStay=" + pricePerStay + ",\n" +
               "  type=T" + type + ",\n" +
               "  operationalState='" + operationalState + '\'' + ",\n" +
               "  adminState='" + adminState + '\'' + ",\n" +
               "  ownerId=" + ownerId +
               "\n}";
    }

}
