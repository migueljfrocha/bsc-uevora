package adminClient;

import auxiliar.*;

import java.rmi.Naming;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class AdminClient {


    public static void main(String[] args) {
        // Load configuration properties
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("src/resources/config.properties")) {
            props.load(fis);
        } catch (Exception e) {
            System.out.println("Failed to load config.properties: " + e.getMessage());
            return;
        }
        
        String host = props.getProperty("remote.host");
        String port = props.getProperty("remote.port");
        String service = props.getProperty("remote.serviceName");

        // RMI URL
        String url = "rmi://" + host + ":" + port + "/" + service;
        try(BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            AdminRMIInterface admin = (AdminRMIInterface) Naming.lookup(url);
            AdminClientManager adminCli = new AdminClientManager(admin, in);
            adminCli.adminPanel();
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}