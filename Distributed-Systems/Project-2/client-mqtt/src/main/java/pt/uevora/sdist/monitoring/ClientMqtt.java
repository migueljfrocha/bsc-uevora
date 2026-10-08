package pt.uevora.sdist.monitoring;

import pt.uevora.sdist.monitoring.mqttmanager.MqttClientSimulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.paho.client.mqttv3.MqttException;

public class ClientMqtt {


    /**
     * Prints the menu options to the console.
     */
    public static void printMenu() {
        System.out.println("\n ---- [ MQTT Client ] ----");
        System.out.println("1. Add Sensor");
        System.out.println("2. Remove Sensor");
        System.out.println("3. List Sensors");
        System.out.println("4. Exit");
        System.out.print("Option Number: ");
    }

    
    public static void main(String[] args) {
        MqttClientSimulator simulator = null;

        Boolean running = true;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            simulator = new MqttClientSimulator();
            while (running) {
                printMenu();
                String option = reader.readLine().trim();

                switch (option) {
                    case "1":
                        // Add Sensor
                        System.out.print("Sensor ID: ");
                        String idStr = reader.readLine().trim();
                        try {
                            Long id = Long.parseLong(idStr);
                            System.out.print("Interval in seconds (default 60s): ");
                            String intervalStr = reader.readLine().trim();
                            int interval = intervalStr.isEmpty() ? 60 : Integer.parseInt(intervalStr);

                            if (interval < 1) {
                                System.out.println("Interval must be at least 1 second.");
                                break;
                            }
                            simulator.addSensor(id, interval);
                            System.out.println("Sensor " + id + " added with interval " + interval + " seconds.");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID or interval.");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        } catch (Exception e) {
                            System.out.println("Error adding sensor: " + e.getMessage());
                        }
                
                        break;

                    case "2":
                        // Remove Sensor
                        System.out.print("Sensor ID to remove: ");
                        String removeIdStr = reader.readLine().trim();
                        try {
                            Long removeId = Long.parseLong(removeIdStr);
                            simulator.removeSensor(removeId);
                            System.out.println("Sensor " + removeId + " removed.");
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID.");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case "3":
                        // List Sensors
                        System.out.println(simulator.listSensors());
                        break;

                    case "4":
                        // Exit
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }

        }
        catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }catch(MqttException mqttException){
            System.err.println("MQTT Exception: " + mqttException.getMessage());
        }
        finally {
            System.out.println("Shutting down...");
            if (simulator != null) {
                simulator.shutdown();
            }
        }
    }
}