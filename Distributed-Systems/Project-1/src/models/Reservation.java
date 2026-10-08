package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalDate;

import auxiliar.status.ReservationStatus;


public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String[] editableAttributes = {"renter_id", "total_price"};

    public int id;
    public int apartmentId;
    public int renterId;
    public LocalDate startDate;
    public LocalDate endDate;
    public BigDecimal totalValue;
    public String operationalState;
    public String adminState;
    public LocalDateTime registerDate;

    /**
     * Constructor for Reservation
     * @param id The reservation ID
     * @param apartmentId The apartment ID
     * @param renterId The renter ID
     * @param startDate The start date of the reservation
     * @param endDate The end date of the reservation
     * @param totalValue The total value of the reservation
     * @param operationalState The operational state of the reservation
     * @param adminState The administrative state of the reservation
     * @param registerDate The registration date of the reservation
     */
    public Reservation(int id, int apartmentId, int renterId, LocalDate startDate, LocalDate endDate,
                       BigDecimal totalValue, String operationalState, String adminState, LocalDateTime registerDate) {
        this.id = id;
        this.apartmentId = apartmentId;
        this.renterId = renterId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalValue = totalValue;
        this.operationalState = operationalState;
        this.adminState = adminState;
        this.registerDate = registerDate;
    }

    /**
     * Create a Reservation object for a reservation request
     * @param apartmentId The apartment ID
     * @param renterId The renter ID
     * @param startDate The start date of the reservation
     * @param endDate The end date of the reservation
     * @return A Reservation object with default values for a new request
     */
    public static Reservation reservationRequest (int apartmentId, int renterId, LocalDate startDate, LocalDate endDate) {
        return new Reservation(-1,
                               apartmentId,
                               renterId,
                               startDate,
                               endDate,
                               BigDecimal.ZERO,
                               ReservationStatus.PENDING,
                               "rejected",
                               null);
    }

    /**
     * Create a Reservation object from a ResultSet
     * @param rs The ResultSet containing reservation data
     * @return A Reservation object
     * @throws SQLException If a database access error occurs
     */
    public static Reservation fromResultSet(ResultSet rs) throws SQLException {
        return new Reservation(
            rs.getInt("reservation_id"),
            rs.getInt("apartment_id"),
            rs.getInt("renter_id"),
            rs.getObject("start_date", LocalDate.class),
            rs.getObject("end_date", LocalDate.class),
            rs.getBigDecimal("total_price"),
            rs.getString("operational_state"),
            rs.getString("admin_state"),
            rs.getObject("created_at", LocalDateTime.class)
        );
    }


    /**
     * Check if an attribute is editable
     * @param attribute The attribute name to check
     * @return true if editable, false otherwise
     */
    public static boolean isEditableAttribute(String attribute) {
        for (String attr : editableAttributes) {
            if (attr.equals(attribute)) {
                return true;
            }
        }
        return false;
    }


    // getters
    public int getId() { return id; }
    public int getApartmentId() { return apartmentId; }
    public int getRenterId() { return renterId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public BigDecimal getTotalValue() { return totalValue; }
    public String getOperacionalState() { return operationalState; }
    public String getAdminState() { return adminState; }
    public LocalDateTime getRegisterDate() { return registerDate; }

    /**
     * A brief summary of the reservation
     * @return Formatted string with reservation summary
     */
    public String summary(){
        return id + " - Apartment:" + apartmentId + " Renter: " + renterId + ") [" + adminState + "]";
    }

    /**
     * Detailed status of the reservation
     * @return Formatted string with reservation status
     */
    public String myStatus(){
        return "Reservation " + id + " for Apartment " + apartmentId +
               "\nOperational State: " + operationalState +
               "\nAdmin State: " + adminState;
    }

    @Override
    public String toString() {
        return "Reservation{\n" +
            "  id=" + id + ",\n" +
            "  apartmentId=" + apartmentId + ",\n" +
            "  renterId=" + renterId + ",\n" +
            "  start=" + startDate + ",\n" +
            "  end=" + endDate + ",\n" +
            "  total=" + totalValue + ",\n" +
            "  operationalState='" + operationalState + '\'' + ",\n" +
            "  adminState='" + adminState + '\'' + ",\n" +
            "  createdAt=" + registerDate +
            "\n}";
    }
    
}
