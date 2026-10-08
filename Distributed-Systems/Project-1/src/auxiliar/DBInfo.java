package auxiliar;

public class DBInfo {
    
    public String dbName;
    public String host;
    public String port;
    public String username;
    public String password;

    /**
     * Constructor for DBInfo class.
     * @param dbName The name of the database.
     * @param host The host of the database.
     * @param port The port of the database.
     * @param username The username for the database.
     * @param password The password for the database.
     */
    public DBInfo(String dbName, String host, String port, String username, String password) {
        this.dbName = dbName;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

}
