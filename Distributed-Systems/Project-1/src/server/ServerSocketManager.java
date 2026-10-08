package server;

import java.net.*;
import java.io.IOException;

import auxiliar.DBInfo;


class ServerSocketManager implements Runnable {
    
    ServerSocket sc = null;
    DBInfo dbInfo;


    /**
     * Constructor for ServerSocketManager
     * Opens a server socket for accepting client connections on the specified port
     * @param serverPort
     * @param dbInfo
     */
    public ServerSocketManager(int serverPort, DBInfo dbInfo){

        this.dbInfo = dbInfo;

        try {
            sc = new ServerSocket(serverPort);
            System.out.println("ServerSocket created on port: " + serverPort);

        } catch (Exception e) {
            System.out.println("Problems creating the ServerSocket");
            System.exit(1);
        }
    }


    /**
     * Run method for the Runnable interface
     */
    public void run() {
        serveClient();        
    }


    /**
     * Method to serve clients. 
     * This method runs indefinitely, accepting client connections and spawning a new thread for each client.
     */
    public void serveClient(){
        while (true) {
            try {
                Socket clientSocket = sc.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, dbInfo);
                Thread thread = new Thread(clientHandler);
                thread.start();

            } catch (IOException e) {
                System.out.println("Error accepting client connection.\nMessage: " + e.getMessage());
            } catch (Exception genEx) {
                System.out.println("Error in server socket manager.\nMessage: " + genEx.getMessage());
            }
        }
    }
}