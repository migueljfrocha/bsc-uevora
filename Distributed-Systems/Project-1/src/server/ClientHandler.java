package server;

import java.util.ArrayList;

import java.net.Socket;
import java.net.SocketException;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import auxiliar.DBInfo;
import auxiliar.ClientRequest;
import auxiliar.RequestType;
import auxiliar.status.*;
import auxiliar.Filters.Filter;
import models.User;
import models.Apartment;
import models.Reservation;


public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private DBInfo dbInfo;
    private int userId = -1;


    public ClientHandler(Socket clientSocket, DBInfo dbInfo) {
        this.clientSocket = clientSocket;
        this.dbInfo = dbInfo;
    }
    
    /**
     * Thread run method to handle client requests
     */
    public void run() {

        ServerDBConnector db = new ServerDBConnector(
            dbInfo.host, dbInfo.dbName, dbInfo.port, dbInfo.username, dbInfo.password
            );

        try(
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            ){
                
            db.connect();
            serveClient(oos, ois, db);
            
        } catch (Exception e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally{
            disableUserAccount(db);
            db.disconnect();
            System.out.println("Client disconnected: " + clientSocket.getRemoteSocketAddress());
            try{ 
                clientSocket.close(); 
            } catch (IOException ignored) {

            }
        }
    }

    /**
     * Method to serve client requests
     * @param oos ObjectOutputStream to send responses to the client
     * @param ois ObjectInputStream to receive requests from the client
     * @param db ServerDBConnector instance for database operations
     * @throws Exception
     */
    private void serveClient (ObjectOutputStream oos, ObjectInputStream ois, ServerDBConnector db) throws Exception{
        while (true) {
            Object receivedObj;
            try {
                receivedObj = ois.readObject();
            } catch (EOFException | SocketException e) {
                // client closed connection
                System.out.println("Client [" + clientSocket.getRemoteSocketAddress() + "] disconnected.");
                break;
            }

            if (!(receivedObj instanceof ClientRequest)) {
                oos.writeObject("Invalid request");
                oos.flush();
                continue;
            }

            Serializable returnValue = processClientRequest(
                                            (ClientRequest) receivedObj,
                                            db);

            if (returnValue != null) {
                oos.writeObject(returnValue);
            }

            oos.flush();
        }
    }


    /**
     * Processes a client request and returns the appropriate response.
     * 
     * @param req: ClientRequest object containing the request type and data.
     * @param db: ServerDBConnector instance for database operations.
     * @return Serializable object containing the response data.
     */
    private Serializable processClientRequest (ClientRequest req, ServerDBConnector db) {
        RequestType type = req.getRequestType();
        Serializable data = req.getRequestData();
        Serializable returnValue = null;

        switch(type){
            case LOGIN_USER:
                returnValue = authenticateUser((String) data, db );
                break;
            case REGISTER_USER:
                returnValue = registerUser((User) data, db);
                break;
            case REGISTER_APARTMENT:
                returnValue = registerApartment((Apartment) data, db);
                break;
            case CREATE_RESERVATION:
                returnValue = createReservation((Reservation) data, db);
                break;
                
            case LIST_APARTMENTS:
                ArrayList<Filter> aptfilters = castToFilterList(data);
                returnValue = listApartmentsByFilter(aptfilters, db);
                break;
            case LIST_USERS:
                ArrayList<Filter> userfilters = castToFilterList(data);
                returnValue = listUsersByFilter(userfilters, db);
                break;
            case LIST_RESERVATIONS:
                ArrayList<Filter> resfilters = castToFilterList(data);
                returnValue = listReservationsByFilter(resfilters, db);
                break;

            case GET_ENTITY_STATUS:
                returnValue = getEntityStatus((String) data, db);
                break;
            case GET_ENTITY_HISTORY:
                returnValue = getEntityHistory((String) data, db);
                break;
            
            case CANCEL_RESERVATION:
                returnValue = cancelReservation((int) data, db);
                break;
            case CONFIRM_RESERVATION:
                returnValue = confirmReservation((int) data, db);
                break;
            case DISABLE_ACCOUNT:
                returnValue = disableUserAccount(db);
                break;
            case PUT_APARTMENT_MAINTENANCE:
                returnValue = putApartmentInMaintenance((int) data, db);
                break;
            default:
                return null;
        }

        return returnValue;

    }


    /**
     * Inserts a new user into the database with 'rejected' admin state.
     * 
     * @param newUser: User object containing user details.
     * @param db: ServerDBConnector for database connection.
     * @return User object if registration and authentication were successful, error message otherwise.
     */
    private Serializable registerUser(User newUser, ServerDBConnector db) {
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String query = "INSERT INTO users (name, email, phone, operational_state, admin_state, created_at) " +
                       "VALUES ('" + newUser.name + "', '" + newUser.email + "', '" + newUser.phone + 
                       "', '" + newUser.operationalState + "', 'rejected', NOW())";
        try {
            int rowsUpdated = stmn.executeUpdate(query);
            if(rowsUpdated <= 0){
                return "Registration failed";
            }
            return "User registered successfully. Awaiting admin approval.";
        } catch (SQLException e) {
            return "Registration failed";
        }
    }


    /**
     * Inserts a new apartment into the database with 'rejected' admin state.
     * @param newApartment: Apartment object containing apartment details.
     * @param db: ServerDBConnector for database connection.
     * @return String confirmation message.
     */
    private Serializable registerApartment(Apartment newApartment, ServerDBConnector db) {
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String query = "INSERT INTO apartments (name, location, price_per_night, type, operational_state, admin_state, owner_id) " +
                       "VALUES ('" + newApartment.name + "', '" + newApartment.location + "', " + newApartment.pricePerStay + 
                       ", " + newApartment.type + ", '" + newApartment.operationalState + "', 'rejected', " + this.userId + ")";
        try {
            int rowsCreated = stmn.executeUpdate(query);
            if(rowsCreated > 0){
                return "Apartment registered successfully.";
            } else {
                return "Apartment registration failed.";
            }
        } catch (SQLException e) {
            return "Apartment registration failed.";
        }
    }


    /**
     * Inserts a new reservation into the database with 'rejected' admin state.
     * @param newReservation: Reservation object containing reservation details.
     * @param db: ServerDBConnector for database connection.
     * @return Reservation object if creation was successful, error message otherwise.
     */
    private Serializable createReservation(Reservation newReservation, ServerDBConnector db) {
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        if (!isApartmentApproved(newReservation.apartmentId, db)) {
            return "Reservation creation failed: Apartment has not been approved.";
        }
        
        // Check date availability
        if (!areDatesAvailable(newReservation.startDate, newReservation.endDate, newReservation.apartmentId, db)) {
            return "Reservation creation failed: Dates not available.";
        }

        // Calculate total price
        newReservation.totalValue = calculateTotalPrice(newReservation.apartmentId, newReservation.startDate, newReservation.endDate, db);
        if (newReservation.totalValue.compareTo(BigDecimal.ZERO) <= 0) {
            return "Reservation creation failed: Invalid total price.";
        }


        String query = "INSERT INTO reservations (apartment_id, renter_id, start_date, end_date, total_price, operational_state, admin_state, created_at) " +
                       "VALUES (" + newReservation.apartmentId + ", " + this.userId + ", '" + newReservation.startDate + 
                       "', '" + newReservation.endDate + "', " + newReservation.totalValue + ", '" + newReservation.operationalState + 
                       "', 'rejected', NOW())";

        int rowsCreated = -1;
        try {
            rowsCreated = stmn.executeUpdate(query);
        } catch (SQLException e) {
            return "Reservation creation failed.";
        }

        if (rowsCreated > 0) {
        // Get the last reservation done by this user
            String query1 = "SELECT reservation_id, apartment_id, renter_id, start_date, end_date, total_price, operational_state, admin_state, created_at " +
                            "FROM reservations WHERE renter_id = " + this.userId + " ORDER BY created_at DESC LIMIT 1";
            try (ResultSet rs = stmn.executeQuery(query1)) {
                if (rs.next()) {
                    Reservation createdReservation = Reservation.fromResultSet(rs);
                    return createdReservation;
                // If it could not retrieve the reservation
                } else {
                    return "Reservation could not be retrieved after creation.";
                }
            } catch (SQLException e) {
                System.out.println("Reservation created but could not be retrieved.\nMessage: " + e.getMessage());
                return "Reservation could not be retrieved after creation.";
            }
        // Failed to create the reservation.
        } else {
            return "Reservation could not be created.";
        }
    }
    

    /**
     * Lists apartments based on provided filters.
     * @param filters: ArrayList of Filter objects to apply.
     * @param db: ServerDBConnector for database connection.
     * @return ArrayList of Apartment objects matching the filters.
     */
    private ArrayList<Apartment> listApartmentsByFilter(ArrayList<Filter> filters, ServerDBConnector db) {
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        ArrayList<Apartment> apartments = new ArrayList<>();

        StringBuilder query = 
            new StringBuilder("SELECT apartment_id, name, location, price_per_night, type, operational_state, admin_state, owner_id FROM apartments " +
                                   "WHERE admin_state='approved'");

        for (int i = 0; i < filters.size(); i++) {
            Filter filter = filters.get(i);
            
            // Append AND
            query.append(" AND ");
            
            // Append filter conditions to the query
            query.append(filter.getField()).append(" "); // Field name
            query.append(filter.getOperator()).append(" '"); // Operator
            query.append(filter.getValue()).append("'"); // Value
    
        }
        query.append(";");

        try (ResultSet rs = stmn.executeQuery(query.toString())) {
            while (rs.next()) {
                apartments.add(Apartment.fromResultSet(rs));
            }
            return apartments;
        } catch (SQLException e) {
            return new ArrayList<Apartment>();
        }
    }


    /**
     * Lists users based on provided filters.
     * @param filters: ArrayList of Filter objects to apply.
     * @param db: ServerDBConnector for database connection.
     * @return ArrayList of User objects matching the filters.
     */
    private ArrayList<User> listUsersByFilter(ArrayList<Filter> filters, ServerDBConnector db) {
        Connection con = db.getConnection();
        Statement stmn = db.getStatement();

        ArrayList<User> users = new ArrayList<>();

        StringBuilder query = 
            new StringBuilder("SELECT user_id, name, email, phone, operational_state, admin_state, created_at FROM users WHERE admin_state='approved'");

        for (int i = 0; i < filters.size(); i++) {
            Filter filter = filters.get(i);

            // Append AND
            query.append(" AND ");

            // Append filter conditions to the query
            query.append(filter.getField()).append(" "); // Field name
            query.append(filter.getOperator()).append(" '"); // Operator
            query.append(filter.getValue()).append("'"); // Value

        }
        query.append(";");

        try (ResultSet rs = stmn.executeQuery(query.toString())) {
            while (rs.next()) {
                users.add(User.fromResultSet(rs));
            }
            return users;
        } catch (SQLException e) {
            return new ArrayList<User>();
        }
    }


    /**
     * Lists reservations made by the user based on provided filters.
     * @param filters: ArrayList of Filter objects to apply.
     * @param db: ServerDBConnector for database connection.
     * @return ArrayList of Reservation objects matching the filters.
     */
    private ArrayList<Reservation> listReservationsByFilter(ArrayList<Filter> filters, ServerDBConnector db) {
        Connection con = db.getConnection();
        Statement stmn = db.getStatement();

        ArrayList<Reservation> reservations = new ArrayList<>();

        StringBuilder query = 
            new StringBuilder("SELECT reservation_id, apartment_id, renter_id, start_date, end_date, total_price, operational_state, admin_state, created_at " + 
                              "FROM reservations WHERE renter_id = " + this.userId);
                              
        for (int i = 0; i < filters.size(); i++) {

            // Append AND  
            query.append(" AND ");

            Filter filter = filters.get(i);

            query.append(filter.getField()).append(" "); // Field name
            query.append(filter.getOperator()).append(" '"); // Operator
            query.append(filter.getValue()).append("'"); // Value

        }
        query.append(";");

        try (ResultSet rs = stmn.executeQuery(query.toString())) {
            while (rs.next()) {
                reservations.add(Reservation.fromResultSet(rs));
            }
            return reservations;
        } catch (SQLException e) {
            return new ArrayList<Reservation>();
        }
    }


    /**
     * Retrieves the operational and admin status of apartments owned by the user or reservations made by the user.
     * @param entityType: String indicating whether to retrieve 'apartments' or 'reservations' status.
     * @param db: ServerDBConnector for database connection.
     * @return ArrayList of status strings.
     */
    private Serializable getEntityStatus(String entityType, ServerDBConnector db) {
        
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        if (entityType.equals("apartments")) {

            String query = "SELECT apartment_id, name, location, price_per_night, type, operational_state, admin_state, owner_id " + 
                           "FROM apartments WHERE owner_id = " + this.userId;
            
            try (ResultSet rs = stmn.executeQuery(query)) {
                ArrayList<String> statuses = new ArrayList<>();

                while (rs.next()) {
                    Apartment apt = Apartment.fromResultSet(rs);
                    statuses.add(apt.myStatus());
                }
                return (Serializable) statuses;

            } catch (SQLException e) {;
                return "Error retrieving apartment statuses.";
            }


        } else if (entityType.equals("reservations")) {

            String query = "SELECT reservation_id, apartment_id, renter_id, start_date, end_date, total_price, operational_state, admin_state, created_at " +
                           "FROM reservations WHERE renter_id = " + this.userId;

            try (ResultSet rs = stmn.executeQuery(query)) {
                ArrayList<String> statuses = new ArrayList<>();

                while (rs.next()) {
                    Reservation res = Reservation.fromResultSet(rs);
                    statuses.add(res.myStatus());
                }

                return (Serializable) statuses;

            } catch (SQLException e) {
                return "Error retrieving reservation statuses.";
            }
        }

        return "Error: Invalid entity type.";
    }


    /**
     * Retrieves the history of reservations for apartments owned by the user or reservations made by the user.
     * @param entityType: String indicating whether to retrieve 'apartments' or 'reservations' history.
     * @param db: ServerDBConnector for database connection.
     * @return ArrayList of Reservation objects representing the history.
     */
    private Serializable getEntityHistory(String entityType, ServerDBConnector db) {
        
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        // All reservations on the users apartments
        if (entityType.equals("apartments")) {

            String query = "SELECT r.reservation_id, r.apartment_id, r.renter_id, r.start_date, r.end_date, r.total_price, r.operational_state, r.admin_state, r.created_at " +
                           "FROM reservations AS r JOIN apartments AS a ON r.apartment_id = a.apartment_id " +
                           "WHERE a.owner_id = " + this.userId;

            try (ResultSet rs = stmn.executeQuery(query)) {
                ArrayList<Reservation> reservations = new ArrayList<>();

                while (rs.next()) {
                    reservations.add(Reservation.fromResultSet(rs));
                }

                return reservations;

            } catch (SQLException e) {
                return "Error retrieving apartment reservation history.";
            }
        }
        // All reservations made by the user
        else if (entityType.equals("reservations")) {

            String query = "SELECT reservation_id, apartment_id, renter_id, start_date, end_date, total_price, operational_state, admin_state, created_at " +
                           "FROM reservations WHERE renter_id = " + this.userId;

            try (ResultSet rs = stmn.executeQuery(query)) {
                ArrayList<Reservation> reservations = new ArrayList<>();

                while (rs.next()) {
                    reservations.add(Reservation.fromResultSet(rs));
                }

                return reservations;

            } catch (SQLException e) {
                return "Error retrieving user reservation history.";
            }
        }

        return "Error: Invalid entity type.";
    }


    /**
     * Cancels a reservation by updating its operational state to 'cancelled'.
     * @param reservationId: int ID of the reservation to be cancelled.
     * @param db: ServerDBConnector for database connection.
     * @return String confirmation message.
     */
    private Serializable cancelReservation(int reservationId, ServerDBConnector db) {
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String query = "UPDATE reservations SET operational_state = '" + ReservationStatus.CANCELLED + 
                       "' WHERE reservation_id = " + reservationId + " AND renter_id = " + this.userId;

        try {
            int rowsUpdated = stmn.executeUpdate(query);
            if (rowsUpdated > 0) {
                return "Reservation id=" + reservationId + " cancelled.";
            }
            else {
                return "Failed to cancel reservation id=" + reservationId + ".";
            }
        } catch (SQLException e) {
            return "Error occurred while cancelling reservation.";
        }
    }

/**
 * Confirms a reservation on my apartment by updating its operational state to 'confirmed'.
 * @param reservationId: int ID of the reservation to be confirmed.
 * @param db: ServerDBConnector for database connection.
 * @return String confirmation message.
 */
    private Serializable confirmReservation(int reservationId, ServerDBConnector db) {
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();


        String query = "UPDATE reservations SET operational_state = '" + ReservationStatus.CONFIRMED + 
                       "' WHERE (SELECT a.owner_id FROM apartments AS a JOIN reservations AS r ON a.apartment_id = r.apartment_id " +
                       "WHERE r.reservation_id = " + reservationId + ") = " + this.userId;

        try {
            int rowsUpdated = stmn.executeUpdate(query);
            if (rowsUpdated > 0) {
                return "Reservation id=" + reservationId + " confirmed.";
            }
            else {
                return "Failed to confirm reservation id=" + reservationId + ".";
            }
        } catch (SQLException e) {
            return "Error occurred while confirming reservation.";
        }
    }


/**
 * Disables a user account by updating its status to 'inactive'.
 * @param db: ServerDBConnector for database connection.
 * @return String confirmation message.
*/
    private Serializable disableUserAccount(ServerDBConnector db) {
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String query = "UPDATE users SET operational_state = '" + UserStatus.SUSPENDED + "' WHERE user_id = " + this.userId;
        try {
            int rowsUpdated = stmn.executeUpdate(query);
            if (rowsUpdated > 0) {
                return "User id=" + this.userId + " account disabled.";
            } else {
                return "Failed to disable user id=" + this.userId + " account.";
            }
        } catch (SQLException e) {
            return "Error occurred while disabling user account.";
        }
    }


/**
 * Puts an apartment into maintenance by updating its operational state.
 * @param aptToMaintain: int ID of the apartment to be put into maintenance.
 * @param db    string.replace("'", "''");: ServerDBConnector for database connection.
 * @return String confirmation message.
 */
    private Serializable putApartmentInMaintenance(int aptToMaintainId, ServerDBConnector db) {
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String query = "UPDATE apartments SET operational_state = '" + ApartmentStatus.MAINTENANCE + 
                       "' WHERE apartment_id = " + aptToMaintainId + " AND owner_id = " + this.userId;

        try {
            int rowsUpdated = stmn.executeUpdate(query);
            if (rowsUpdated > 0) {
                return "Apartment id=" + aptToMaintainId + " is now under maintenance.";
            } else {
                return "Failed to put apartment id=" + aptToMaintainId + " under maintenance.";
            }
        } catch (SQLException e) {
            return "Error occurred while putting apartment under maintenance.";
        }
    }


/**
 * Authenticates a user by email.
 * @param email: String email of the user to authenticate.
 * @param db: ServerDBConnector for database connection.
 * @return User object if authentication is successful, error message string otherwise.
 */
    private Serializable dbAuthenticateByEmail(String email, ServerDBConnector db) {
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String query = "SELECT user_id, name, email, phone, operational_state, admin_state, created_at " +
                       "FROM users WHERE email = '" + email + "'";

        try (ResultSet rs = stmn.executeQuery(query)) {

            if (rs.next()) {
                User user = User.fromResultSet(rs);
                // Check if the user is inactive and activate if so
                if (user.operationalState.equals(UserStatus.INACTIVE)) {
                    String query1 = "UPDATE users SET operational_state = '" + UserStatus.ACTIVE + "' WHERE user_id = " + user.id;
                    stmn.executeUpdate(query1);
                // Check if the user is suspended 
                } else if (user.operationalState.equals(UserStatus.SUSPENDED)) {
                    return "Login failed: User is suspended";
                }
                
                return user;
                
            } else {
                return "Login failed: User not found";
            }

        } catch (SQLException e) {
            return "Login Failed";
        }
    }


/**
 * Authenticates a user and updates the handler's state if successful.
 * @param email: Email of the user to authenticate.
 * @param db: ServerDBConnector for database connection.
 * @return User object if authentication is successful, error message string otherwise.
 */
    private Serializable authenticateUser(String email, ServerDBConnector db) {

        Serializable returnValue = dbAuthenticateByEmail(email, db);
        User user;

        if (returnValue instanceof User) {
            user = (User) returnValue;
            userId = user.id;

            if (user.adminState.equals("rejected")) {
                return "Login failed: User has not been approved by admin. Try again later.";
            }
            else {
                return user;
            }

        } else {
            return returnValue;
        }

    }


/**
 * Checks if the given date range is available for a specific apartment.
 * @param startDate: Start date of the desired reservation.
 * @param endDate: End date of the desired reservation.
 * @param apartmentId: ID of the apartment to check availability for.
 * @param db: ServerDBConnector for database connection.
 * @return true if the dates are available, false otherwise.
 */
    private boolean areDatesAvailable (LocalDate startDate, LocalDate endDate, int apartmentId, ServerDBConnector db){
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String query =
            "SELECT COUNT(*) AS count FROM reservations " +
            "WHERE apartment_id = " + apartmentId + " " +
            "AND operational_state IN ('" + ReservationStatus.PENDING + "', '" + ReservationStatus.CONFIRMED + "') " +
            "AND start_date < '" + endDate + "' " +     // existing starts before new ends
            "AND end_date > '" + startDate + "'";       // existing ends after new starts

        try (ResultSet rs = stmn.executeQuery(query)) {
            if (rs.next()) {
                int count = rs.getInt("count");
                return count == 0; // Dates are available if no overlapping reservations
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Calculates the total price for a reservation based on apartment price and duration.
     * 
     * @param apartmentId The ID of the apartment.
     * @param startDate The start date of the reservation.
     * @param endDate The end date of the reservation.
     * @param db The database connection.
     * @return The total price for the reservation.
     */
    private BigDecimal calculateTotalPrice(int apartmentId, LocalDate startDate, LocalDate endDate, ServerDBConnector db) {
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String query = "SELECT price_per_night FROM apartments WHERE apartment_id = " + apartmentId;

        try (ResultSet rs = stmn.executeQuery(query)) {
            if (rs.next()) {
                BigDecimal pricePerNight = rs.getBigDecimal("price_per_night");

                long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
                return pricePerNight.multiply(BigDecimal.valueOf(daysBetween));
            } else {
                return BigDecimal.ZERO; // Apartment not found
            }
        } catch (SQLException e) {
            return BigDecimal.ZERO; // Error occurred
        }
    }

    /**
     * Checks if an apartment has been approved by an admin.
     * @param apartmentId The ID of the apartment to check.
     * @param db The database connection.
     * @return true if the apartment is approved, false otherwise.
     */
    private boolean isApartmentApproved(int apartmentId, ServerDBConnector db) {
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String query = "SELECT admin_state FROM apartments "+
                       "WHERE apartment_id = " + apartmentId;

        try (ResultSet rs = stmn.executeQuery(query)) {
            if (rs.next()) {
                String adminState = rs.getString("admin_state");
                return adminState.equals("approved");
            } else {
                return false; // Apartment not found
            }
        } catch (SQLException e) {
            return false; // Error occurred
        }
    }


    /**
     * Casts a Serializable object to an ArrayList of Filter objects.
     * 
     * @param data Serializable object to be cast.
     * @return ArrayList of Filter objects.
     */
    private ArrayList<Filter> castToFilterList(Serializable data) {

        ArrayList<Filter> filters = new ArrayList<>();
        ArrayList<?> raw = filters;
        if (data instanceof ArrayList) {
            raw = (ArrayList<?>) data;
            for (Object o : raw) {
                if (o instanceof Filter) {
                    filters.add((Filter) o);
                }
            }
        }
        if (filters.size() != raw.size()){
            System.out.println("[" + clientSocket.getRemoteSocketAddress() + "]: Could not cast all filters");
        }

        return filters;
        
    }
}
