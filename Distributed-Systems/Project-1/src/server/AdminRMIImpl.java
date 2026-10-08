package server;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.sql.*;

import models.*;
import auxiliar.DBInfo;
import auxiliar.AdminRMIInterface;


public class AdminRMIImpl extends UnicastRemoteObject implements AdminRMIInterface{

    private DBInfo dbInfo;
    
    /**
     * Constructor for AdminRMIImpl class
     * @param dbInfo Database connection information
     * @throws RemoteException
     */
    public AdminRMIImpl(DBInfo dbInfo) throws RemoteException {
        super();
        this.dbInfo = dbInfo;
    }
    
    
    /**
     * Update the operational status of an entity in the DB
     * @param entity The entity name
     * @param status The new operational status
     * @param entityIdValue The ID value of the entity
     * @return true if successful, false otherwise
     * @throws RemoteException
     */
    public boolean updateEntityOperationalStatus(String entity, String status, int entityIdValue) throws RemoteException{
        
        ServerDBConnector db = new ServerDBConnector(dbInfo.host, dbInfo.dbName, dbInfo.port, dbInfo.username, dbInfo.password);
        db.connect();
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();
        
        String entityIDName = getEntityIDName(entity);
        
        if (conn == null || stmn == null || entityIDName == null) {
            db.disconnect();
            return false;
        }

        try {
            String query;
            int updated = 0;
            if (status != null) {
                String s = status;
                query = "UPDATE " +entity+" SET operational_state = '" + s + 
                        "' WHERE "+ entityIDName +" = " + entityIdValue;
                updated = stmn.executeUpdate(query);
            }
            return updated > 0;
        } catch (SQLException e) {
            System.out.println("[ADMIN] Error updating operational status: " + e.getMessage());
            return false;
        } finally {
            db.disconnect();
        }
        
    }


    /**
     * Update the admin status of an entity in the DB
     * @param entity The entity name
     * @param status The new admin status
     * @param entityIdValue The ID value of the entity
     * @return true if successful, false otherwise
     * @throws RemoteException
     */
    public boolean updateEntityAdminStatus(String entity, String status, int entityIdValue) throws RemoteException{
        ServerDBConnector db = new ServerDBConnector(dbInfo.host, dbInfo.dbName, dbInfo.port, dbInfo.username, dbInfo.password);
        db.connect();
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();
        
        String entityIDName = getEntityIDName(entity);
        
        if (conn == null || stmn == null || entityIDName == null) {
            db.disconnect();
            return false;
        }

        try {
            String query;
            int updated = 0;
            if (status != null) {
                String s = status;
                query = "UPDATE " +entity+" SET admin_state = '" + s + 
                        "' WHERE "+ entityIDName +" = " + entityIdValue;
                        
                updated = stmn.executeUpdate(query);
            }
            return updated > 0;
        } catch (SQLException e) {
            System.out.println("[ADMIN] Error updating admin status: " + e.getMessage());
            return false;
        } finally {
            db.disconnect();
        }    
    }

    /**
     * Delete an entity from the DB
     * @param entity The entity name
     * @param entityIdValue The ID value of the entity
     * @return true if successful, false otherwise
     * @throws RemoteException
     */
    public boolean deleteEntity(String entity, int entityIdValue) throws RemoteException{
        ServerDBConnector db = new ServerDBConnector(dbInfo.host, dbInfo.dbName, dbInfo.port, dbInfo.username, dbInfo.password);
        db.connect();
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String entityIDName = getEntityIDName(entity);
        
        if (conn == null || stmn == null || entityIDName == null) {
            db.disconnect();
            return false;
        }

        try {
            String query;
            int updated = 0;
            query = "DELETE FROM " + entity + 
                    " WHERE "+ entityIDName + " = " + entityIdValue;
                    
            updated = stmn.executeUpdate(query);
            return updated > 0;

        } catch (SQLException e) {
            System.out.println("[ADMIN] Error deleting entity: " + e.getMessage());
            return false;
        } finally {
            db.disconnect();
        }
        
    }

    /**
     * Add an entity to the DB
     * @param entity The entity to add
     * @return true if successful, false otherwise
     * @throws RemoteException
     */
    public boolean addEntity(Serializable entity) throws RemoteException{
        
        ServerDBConnector db = new ServerDBConnector(dbInfo.host, dbInfo.dbName, dbInfo.port, dbInfo.username, dbInfo.password);
        db.connect();
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String query;
        if (entity instanceof User) {
            User user = (User) entity;
            query = "INSERT INTO users (name, email, phone, operational_state, admin_state, created_at) " +
                "VALUES ('" + user.name + "', '" + user.email + "', '" + user.phone + 
                "', '" + user.operationalState + "', 'approved', NOW())";
        } else if (entity instanceof Apartment) {
            Apartment apartment = (Apartment) entity;
            query = "INSERT INTO apartments (name, location, price_per_night, type, operational_state, admin_state, owner_id) " +
                    "VALUES ('" + apartment.name + "', '" + apartment.location + "', " + apartment.pricePerStay + 
                    ", " + apartment.type + ", '" + apartment.operationalState + "', 'approved', " + apartment.ownerId + ")";
        } else if (entity instanceof Reservation){
            Reservation reservation = (Reservation) entity;
            query = "INSERT INTO reservations (apartment_id, renter_id, start_date, end_date, total_price, operational_state, admin_state, created_at) " +
                    "VALUES (" + reservation.apartmentId + ", " + reservation.renterId + ", '" + reservation.startDate + 
                    "', '" + reservation.endDate + "', " + reservation.totalValue + ", '" + reservation.operationalState + 
                    "', 'approved', NOW())";
        } else {
            return false;
        }

        try {
            int inserted = stmn.executeUpdate(query);
            return inserted > 0;
        } catch (SQLException e) {
            System.out.println("[ADMIN] Error adding entity: " + e.getMessage());
            return false;
        } finally {
            db.disconnect();
        }

    }

    
    /**
     * Edit an attribute of an entity in the DB
     * @param entity The entity name
     * @param entityIdValue The ID value of the entity
     * @param attributeToEdit The attribute to edit
     * @param newValue The new value for the attribute
     * @return true if successful, false otherwise
     * @throws RemoteException
     */
    public boolean editEntity(String entity, int entityIdValue, String attributeToEdit, String newValue) throws RemoteException{
        
        ServerDBConnector db = new ServerDBConnector(dbInfo.host, dbInfo.dbName, dbInfo.port, dbInfo.username, dbInfo.password);
        db.connect();
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        String entityIDName = getEntityIDName(entity);

        if (conn == null || stmn == null || entityIDName == null) {
            db.disconnect();
            return false;
        }

        try {
            String query;
            int updated = 0;
            query = "UPDATE "+ entity + " SET " + attributeToEdit + " = '" + newValue + 
                    "' WHERE " + entityIDName + "=" + entityIdValue;

            updated = stmn.executeUpdate(query);
            return updated > 0;

        } catch (SQLException e) {
            System.out.println("[ADMIN] Error editing entity: " + e.getMessage());
            return false;
        } finally {
            db.disconnect();
        }
    }


    /**
     * Get a List of Apartments from DB with given admin state filter
     * @param adminState The admin state to filter by
     * @return List of Apartments
     * @throws RemoteException
     */
    public ArrayList<Apartment> getApartmentsList(String adminState) throws RemoteException {
        ArrayList<Apartment> list = new ArrayList<>();

        ServerDBConnector db = new ServerDBConnector(dbInfo.host, dbInfo.dbName, dbInfo.port, dbInfo.username, dbInfo.password);
        db.connect();
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        if (conn == null || stmn == null || adminState == null) {
            db.disconnect();
            return list;
        }

        String query = "SELECT apartment_id, name, location, price_per_night, type, operational_state, admin_state, owner_id FROM apartments " + 
                       "WHERE admin_state='" + adminState + "' ORDER BY apartment_id ASC";

        try {
            ResultSet rs = stmn.executeQuery(query);
            
            while (rs.next()) {
                list.add(Apartment.fromResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("[ADMIN] Error retrieving apartments list: " + e.getMessage());
            return list;
        } finally {
            db.disconnect();
        }
        return list;
    }


    /**
     * Get a List of Users from DB with given admin state filter
     * @param adminState The admin state to filter by
     * @return List of Users
     * @throws RemoteException
     */
    public ArrayList<User> getUsersList(String adminState) throws RemoteException {
        ArrayList<User> list = new ArrayList<>();

        ServerDBConnector db = new ServerDBConnector(dbInfo.host, dbInfo.dbName, dbInfo.port, dbInfo.username, dbInfo.password);
        db.connect();
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        if (conn == null || stmn == null || adminState == null) {
            db.disconnect();
            return list;
        }


        String query = "SELECT user_id, name, email, phone, operational_state, admin_state, created_at FROM users " +
                       "WHERE admin_state='" + adminState + "'" + " ORDER BY user_id ASC";

        try {
            ResultSet rs = stmn.executeQuery(query);
            
            while (rs.next()) {
                list.add(User.fromResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("[ADMIN] Error retrieving users list: " + e.getMessage());
            return list;
        } finally {
            db.disconnect();
        }
        return list;
    }


    /**
     * Get a List of Reservations from DB with given admin state filter
     * @param adminState The admin state to filter by
     * @return List of Reservations
     * @throws RemoteException
     */
    public List<Reservation> getReservationsList(String adminState) throws RemoteException {
        ArrayList<Reservation> list = new ArrayList<>();

        ServerDBConnector db = new ServerDBConnector(dbInfo.host, dbInfo.dbName, dbInfo.port, dbInfo.username, dbInfo.password);
        db.connect();
        Connection conn = db.getConnection();
        Statement stmn = db.getStatement();

        if (conn == null || stmn == null || adminState == null) {
            db.disconnect();
            return list;
        }

        String query = "SELECT reservation_id, apartment_id, renter_id, start_date, end_date, total_price, operational_state, admin_state, created_at FROM reservations " + 
                       "WHERE admin_state='" + adminState + "' ORDER BY reservation_id ASC";

        try(ResultSet rs = stmn.executeQuery(query)) {

            while (rs.next()) {
                list.add(Reservation.fromResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("[ADMIN] Error retrieving reservations list: " + e.getMessage());
            return list;
        } finally {
            db.disconnect();
        }
        return list;
    }


    /**
     * Helper method to get the ID column name for a given entity
     * @param entity The entity name
     * @return The ID column name
     */
    private String getEntityIDName(String entity) {
        switch(entity){
            case "reservations": return "reservation_id";
            case "users": return "user_id";
            case "apartments": return "apartment_id";
            default: 
                return null;
        }
    }
}