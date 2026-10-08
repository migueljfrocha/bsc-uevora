package pt.uevora.sdist.monitoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import pt.uevora.sdist.monitoring.clients.DevicesClient;
import pt.uevora.sdist.monitoring.clients.ReadingsClient;
import pt.uevora.sdist.monitoring.model.Device;
import pt.uevora.sdist.monitoring.model.DevicesStats;
import pt.uevora.sdist.monitoring.model.PerformanceStats;
import pt.uevora.sdist.monitoring.model.Reading;
import pt.uevora.sdist.monitoring.model.ReadingsStats;


public class AdminCLI {

    /**
     * Device Management Menu
     * @param reader: BufferedReader for user input
     * @param devicesClient: DevicesClient to interact with the server
     */
    public static void deviceMenu (BufferedReader reader, DevicesClient devicesClient) {
        Boolean running = true;

        Long id;
        String protocolType;
        String roomId;
        String floorId;
        String buildingId;
        String departmentId;
        Boolean state;
        
        while (running) {
            try {
                printDeviceManagementMenu();
                String deviceInput = reader.readLine();
                switch (deviceInput) {
                    case "1":
                        // List Devices
                        List<Device> devices = devicesClient.listDevices();
                        Device.printHeader();
                        for (Device device : devices) {
                            device.printDevice();
                        }
                        Device.printSeparator();
                        break;
                    case "2":
                        // Add Device
                        System.out.print("Device Protocol (MQTT, GRPC, REST): ");
                        protocolType = reader.readLine();
                        System.out.print("Room ID: ");
                        roomId = reader.readLine();
                        System.out.print("Floor ID: ");
                        floorId = reader.readLine();
                        System.out.print("Building ID: ");
                        buildingId = reader.readLine();
                        System.out.print("Department ID: ");
                        departmentId = reader.readLine();
                        System.out.print("State (true/false): ");
                        state = Boolean.parseBoolean(reader.readLine());

                        Device newDevice = devicesClient.addDevice(
                                                    protocolType, 
                                                    roomId, 
                                                    floorId, 
                                                    buildingId, 
                                                    departmentId, 
                                                    state);

                        Device.printHeader();
                        newDevice.printDevice();
                        Device.printSeparator();
                        break;
                    case "3":
                        // Update Device
                        System.out.print("Device ID: ");
                        id = Long.parseLong(reader.readLine());
                        System.out.print("Device Protocol (MQTT, GRPC, REST): ");
                        protocolType = reader.readLine();
                        protocolType = protocolType.isEmpty() ? null : protocolType;
                        System.out.print("Room ID: ");
                        roomId = reader.readLine();
                        System.out.print("Floor ID: ");
                        floorId = reader.readLine();
                        System.out.print("Building ID: ");
                        buildingId = reader.readLine();
                        System.out.print("Department ID: ");
                        departmentId = reader.readLine();
                        System.out.print("State (true/false): ");
                        state = Boolean.parseBoolean(reader.readLine());
                        
                        Device updatedDevice = devicesClient.updateDevice(
                                                        id, 
                                                        protocolType, 
                                                        roomId, 
                                                        floorId, 
                                                        buildingId, 
                                                        departmentId, 
                                                        state);
                        Device.printHeader();
                        updatedDevice.printDevice();
                        Device.printSeparator();
                        break;
                    case "4":
                        // Remove Device
                        System.out.print("Device ID: ");
                        id = Long.parseLong(reader.readLine());
                        String response = devicesClient.removeDevice(id);
                        System.out.println(response);

                        break;
                    case "5":
                        // View Device
                        System.out.print("Device ID: ");
                        id = Long.parseLong(reader.readLine());
                        Device device = devicesClient.viewDevice(id);
                        Device.printHeader();
                        device.printDevice();
                        Device.printSeparator();
                        
                        break;
                    case "6":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid input format. Please enter the correct data type.");
            } catch (RuntimeException re) {
                System.err.println("Error: " + re.getMessage());
            } catch (IOException ioe) {
                System.err.println("IO Error: " + ioe.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected Error: " + e.getMessage());
            }
        }
    }


    /**
     * Reading Management Menu
     * @param reader: BufferedReader for user input
     * @param readingsClient: ReadingsClient to interact with the server
     */
    public static void readingsMenu (BufferedReader reader, ReadingsClient readingsClient) {
        Boolean running = true;
        Long deviceId;
        String levelId;
        String from;
        String fromStr;
        String toStr;
        String to;
        
        while (running) {
            try{
                printReadingManagementMenu();
                String readingInput = reader.readLine();
                switch (readingInput) {
                    case "1":
                        // Readings by Room
                        System.out.print("Enter Room ID: ");
                        levelId = reader.readLine();
                        readingsExtraMenu(reader, readingsClient, "sala", levelId);
                        break;
                    case "2":
                        // Readings by Department
                        System.out.print("Enter Department ID: ");
                        levelId = reader.readLine();
                        readingsExtraMenu(reader, readingsClient,"departamento", levelId);
                        break;
                    case "3":
                        // Readings by Floor
                        System.out.print("Enter Floor ID: ");
                        levelId = reader.readLine();
                        readingsExtraMenu(reader, readingsClient,"piso", levelId);
                        break;
                    case "4":
                        // Readings by Building
                        System.out.print("Enter Building ID: ");
                        levelId = reader.readLine();
                        readingsExtraMenu(reader, readingsClient,"edificio", levelId);
                        break;
                    case "5":
                        // Readings by Device
                        System.out.print("Enter Device ID: ");
                        deviceId = Long.parseLong(reader.readLine());
                        System.out.print("From, empty = last 24h (YYYY-MM-DDTHH:MM:SS): ");
                        fromStr = reader.readLine().trim();
                        
                        if(fromStr.isEmpty()) {
                            from ="";
                            to ="";
                        } else {
                            from = LocalDateTime.parse(fromStr).toString();
                            System.out.print("To (YYYY-MM-DDTHH:MM:SS): ");
                            toStr = reader.readLine().trim();
                            to = LocalDateTime.parse(toStr).toString();
                        }
                        
                        List<Reading> deviceReadings = readingsClient.getRawReadings(deviceId, from, to);
                        Reading.printRawHeader();
                        for (Reading r : deviceReadings) {
                            r.printRawReadings();
                        }
                        Reading.printRawSeparator();
                        break;
                    case "6":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid input format. Please enter the correct data type.");
            } catch (RuntimeException re) {
                System.err.println("Error: " + re.getMessage());
            } catch (IOException ioe) {
                System.err.println("IO Error: " + ioe.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected Error: " + e.getMessage());
            }
        }
        
    }


    /**
     * Reading Extra Menu for Average and Raw readings
     * @param reader: BufferedReader for user input
     * @param readingsClient: ReadingsClient to interact with the server
     * @param level: level type
     * @param levelId: level id
     */
    public static void readingsExtraMenu (BufferedReader reader, ReadingsClient readingsClient, String level, String levelId) {
        Boolean running = true;
        String from;
        String to;
        String fromStr;
        String toStr;
        while (running) {
            try {
                printReadingExtraMenu(levelId);
                String readingExtraInput = reader.readLine();
                switch (readingExtraInput) {
                    case "1":
                        // Average Readings per level
                        System.out.print("From, empty = last 24h (YYYY-MM-DDTHH:MM:SS): ");
                        fromStr = reader.readLine().trim();
                        
                        if(fromStr.isEmpty()) {
                            from ="";
                            to ="";
                        } else {
                            from = LocalDateTime.parse(fromStr).toString();
                            System.out.print("To (YYYY-MM-DDTHH:MM:SS): ");
                            toStr = reader.readLine().trim();
                            to = LocalDateTime.parse(toStr).toString();
                        }

                        Reading avgReading = readingsClient.getAvgReadings(level, levelId, from, to);
                        
                        Reading.printAvgHeader();
                        avgReading.printAvgReadings();
                        Reading.printAvgSeparator();
                        break;
                    case "2":
                        //Raw Readings per level
                        System.out.print("From, empty = last 24h (YYYY-MM-DDTHH:MM:SS): ");
                        fromStr = reader.readLine().trim();
                        
                        if(fromStr.isEmpty()) {
                            from ="";
                            to ="";
                        } else {
                            from = LocalDateTime.parse(fromStr).toString();
                            System.out.print("To (YYYY-MM-DDTHH:MM:SS): ");
                            toStr = reader.readLine().trim();
                            to = LocalDateTime.parse(toStr).toString();
                        }
                        List<Reading> readings = readingsClient.getRawLevelReadings(level, levelId, from, to);
                        Reading.printRawHeader();
                        for (Reading reading : readings) {
                            reading.printRawReadings();
                        }
                        Reading.printRawSeparator();
                        break;
                    case "3":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid input format. Please enter the correct data type.");
            } catch (RuntimeException re) {
                System.err.println("Error: " + re.getMessage());
            } catch (IOException ioe) {
                System.err.println("IO Error: " + ioe.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected Error: " + e.getMessage());
            }
        }
    }


    /**
     * System Statistics Menu
     * @param reader: BufferedReader for user input
     * @param devicesClient: DevicesClient to interact with the server
     * @param readingsClient: ReadingsClient to interact with the server
     */
    public static void statsMenu (BufferedReader reader, DevicesClient devicesClient, ReadingsClient readingsClient) {
        Boolean running = true;
        while (running) {
            try {
                printStatsMenu();
                String statsInput = reader.readLine();
                switch (statsInput) {
                    case "1":
                        // Device Stats
                        DevicesStats devicesStats = devicesClient.getDeviceStatistics();
                        devicesStats.printHeader();
                        devicesStats.printStats();
                        break;
                    case "2":
                        // Readings Stats
                        ReadingsStats readingsStats = readingsClient.getReadingsStatistics();
                        readingsStats.printHeader();
                        readingsStats.printStats();
                        break;
                    case "3":
                        // Performance Stats
                        System.out.print("Enter time interval in seconds: ");
                        Long time = Long.parseLong(reader.readLine());
                        PerformanceStats performanceStats = readingsClient.getPerformanceStatistics(time);
                        performanceStats.printHeader();
                        performanceStats.printStats();
                        break;
                    case "4":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }

            } catch (RuntimeException re) {
                System.err.println("Error: " + re.getMessage());
            } catch (IOException ioe) {
                System.err.println("IO Error: " + ioe.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected Error: " + e.getMessage());
            }
        }

    }


    public static void main(String[] args) {
        boolean running = true;

        HttpClient httpClient = HttpClient.newBuilder()
                                        .connectTimeout(Duration.ofSeconds(10))
                                        .build();

        DevicesClient devicesClient = new DevicesClient(httpClient);
        ReadingsClient readingsClient = new ReadingsClient(httpClient);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            while (running) {
                printMainMenu();
                String input = reader.readLine();

                switch (input) {
                    case "1":
                        deviceMenu(reader, devicesClient);
                        break;
                    case "2":
                        readingsMenu(reader, readingsClient);
                        break;
                    case "3":
                        statsMenu(reader, devicesClient, readingsClient);
                        break;
                    case "4":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
        }

    }
    /**
     * Main Menu
     */
    private static void printMainMenu(){
        System.out.println("\n ---- [ Admin Client ] ----");
        System.out.println("1. Device Management");
        System.out.println("2. Reading Management");
        System.out.println("3. System Statistics");
        System.out.println("4. Exit");
        System.out.print("Option Number: ");
    }


    /**
     * Device Management Menu
     */
    private static void printDeviceManagementMenu(){
        System.out.println("\n ---- [ Device Management ] ----");
        System.out.println("1. List Devices");
        System.out.println("2. Add Device");
        System.out.println("3. Update Device");
        System.out.println("4. Remove Device");
        System.out.println("5. View Device");
        System.out.println("6. Exit");
        System.out.print("Option Number: ");
    }


    /**
     * Reading Management Menu
     */
    private static void printReadingManagementMenu(){
        System.out.println("\n ---- [ Readings Management ] ----");
        System.out.println("1. Readings by Room");
        System.out.println("2. Readings by Department");
        System.out.println("3. Readings by Floor");
        System.out.println("4. Readings by Building");
        System.out.println("5. Readings by Device");
        System.out.println("6. Exit");
        System.out.print("Option Number: ");
    }


    /**
     * Reading Extra Menu
     * @param name: level name
     */
    private static void printReadingExtraMenu(String name){
        System.out.println("\n ---- [ "+ name + " ] ----");
        System.out.println("1. Average Readings");
        System.out.println("2. List All Readings");
        System.out.println("3. Exit");
        System.out.print("Option Number: ");
    }


    /**
     * System Statistics Menu
     */
    private static void printStatsMenu(){
        System.out.println("\n ---- [ System Statistics ] ----");
        System.out.println("1. Device Stats");
        System.out.println("2. Readings Stats");
        System.out.println("3. Performance Stats");
        System.out.println("4. Exit");
        System.out.print("Option Number: ");
    }
}
