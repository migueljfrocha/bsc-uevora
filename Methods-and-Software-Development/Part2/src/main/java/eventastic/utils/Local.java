package eventastic.utils;

public class Local {
    private String name;
    private String address;
    private String postalCode;
    private String city;
    private String country;

    
    // Constructor
    public Local(String name, String address, String postalCode, String city, String country) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }


    /**
     * toString method to represent the Local object as a formatted string.
     * Prints in the following format:
     *   Name
     *   Address
     *   PostalCode, City, Country
     * @return Formatted string representation of the Local object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  " + this.name + "\n");
        sb.append("  " + this.address + "\n");
        sb.append("  " + this.postalCode + ", ");
        sb.append(this.city + ", " + this.country);

        return sb.toString();
    }


    // Getters
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }


    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setCountry(String country) {
        this.country = country;
    }
}
