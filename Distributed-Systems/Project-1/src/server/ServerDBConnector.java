package server;

import java.sql.*;

public class ServerDBConnector {
    private String PG_HOST;
    private String PG_DB;
    private String PORT;
    private String USER;
    private String PWD;

    private Connection con = null;
    private Statement stmt = null;

    
    /**
     * Constructor for ServerDBConnector
     * @param host: String containing the PostgreSQL host address
     * @param db: String containing the database name
     * @param port: String containing the port number
     * @param user: String containing the username
     * @param pw: String containing the password
     */
    public ServerDBConnector(String host, String db, String port, String user, String pw) {
        PG_HOST = host;
        PG_DB = db;
        PORT = port;
        USER = user;
        PWD = pw;
    }


    /**
     * Method to connect to the PostgreSQL database
     */
    public void connect(){
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://"+PG_HOST+":"+PORT+"/"+PG_DB, USER, PWD);
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println("Problems setting the connection");
        }
    }

    
    /**
     * Method to disconnect from the PostgreSQL database
     */
    public void disconnect() {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("Error closing statement: " + e.getMessage());
            }
            stmt = null;
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
            con = null;
        }
    }

// Getters
    /**
     * Method to get the Connection object
     * @return Connection object
     */
    public Connection getConnection(){
        // Ensure connection is valid 
        try {
            if (con != null && con.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.out.println("Error checking connection status: " + e.getMessage());
            return null;
        }

        return con;
    }

    
    /**
     * Method to get the Statement object
     * @return Statement object
     */
    public Statement getStatement(){
        // Ensure the statement is valid before returning
        try {
            if (stmt != null && stmt.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.out.println("Error checking statement status: " + e.getMessage());
            return null;
        }

        return stmt;
    }
}
