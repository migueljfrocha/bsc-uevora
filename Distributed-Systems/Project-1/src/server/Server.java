package server;

import java.io.FileInputStream;

import java.util.Properties;

import auxiliar.*;

// Server class to initialize RMI and Socket server
public class Server {

    public static void main(String[] args) {

        // load properties from .properties file
        Properties props = new Properties();
        try (FileInputStream file = new FileInputStream("src/resources/config.properties")) {
            props.load(file);
        } catch (Exception e) {
            System.out.println("Failed to load config.properties: " + e.getMessage());
            return;
        }

        // Database Properties
        String dbName = props.getProperty("db.name");
        String host = props.getProperty("db.host");
        String port = props.getProperty("db.port");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        DBInfo dbInfo = new DBInfo(dbName, host, port, username, password);

        // RMI Properties
        String registryPort = props.getProperty("remote.port");
        String serviceName = props.getProperty("remote.serviceName");
        String rmiHost = props.getProperty("remote.host");

        // Server Socket Properties
        int serverPort = Integer.parseInt(props.getProperty("socket.port"));

        //For different workspaces might be needed to set the hostname explicitly (uncomment if you face issues)
        //System.setProperty("java.rmi.server.hostname", rmiHost);
        
        // Start RMI service
        new ServerRMIManager(registryPort, serviceName, rmiHost, dbInfo);

        // Start Server Socket
        ServerSocketManager socketServer = new ServerSocketManager(serverPort, dbInfo);
        Thread genClienThread = new Thread(socketServer);
        genClienThread.start();
    }
}