package generalClient;

import java.util.Properties;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;


import java.net.Socket;


public class GeneralClient {
    public static void main(String[] args) {

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("src/resources/config.properties")) {
            props.load(fis);
        } catch (Exception e) {
            System.out.println("Failed to load config.properties: " + e.getMessage());
            return;
        }
        
        String host = props.getProperty("socket.host");
        int port = Integer.parseInt(props.getProperty("socket.port"));
        
        
        try(
            Socket sock = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream ois  = new ObjectInputStream(sock.getInputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            ){
                
            GeneralClientManager genClient = new GeneralClientManager(sock, oos, ois, in);

            genClient.authPanel();
            genClient.clientPanel();

        }catch(Exception e){
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

}