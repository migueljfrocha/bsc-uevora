package generalClient;

import java.util.ArrayList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import java.math.BigDecimal;
import java.net.Socket;

import auxiliar.*;

import auxiliar.Filters.Filter;
import models.Apartment;
import models.Reservation;
import models.User;


public class GeneralClientManager {

    private Socket sock;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private BufferedReader in;
    private int userID = -1;
    private static final DateTimeFormatter STRICT_DTF =
        DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);


    /**
     * Constructor for GeneralClientManager
     * @param sock: Socket
     * @param oos: ObjectOutputStream
     * @param ois: ObjectInputStream
     * @param in: BufferedReader
     */
    public GeneralClientManager(Socket sock, ObjectOutputStream oos, ObjectInputStream ois, BufferedReader in){
        this.sock = sock;
        this.oos = oos;
        this.ois = ois;
        this.in = in;
    }
    
    /**
     * Main authentication panel loop logic
     */
    public void authPanel() throws Exception{
        boolean authenticated = false;

        while(!authenticated){
            authMenu();
            String line = in.readLine();
            if(line == null || line.trim().isEmpty()) continue;

            switch(line.trim()){
                case "1":
                    handleRegisterClient();                    
                    authenticated = false; // User has not been approved yet
                    break;
                case "2":
                    authenticated = handleLoginByEmail();
                    break;
                case "0":
                    System.out.println("Exiting...");
                    closeConnections();
                    System.exit(0);
                    break;    
                default:         
                    System.out.println("Invalid option. Please try again.");   
            }
        }
    }

    /**
     * Main client panel loop logic
     */
    public void clientPanel() throws Exception{
        while(true){
            printMenu();
            String line = in.readLine();
            if(line == null || line.trim().isEmpty()) continue;

            switch(line.trim()){
                case "1":
                    handleRegisterApartment();
                    break;
                case "2":
                    handleCreateReservation();
                    break;
                case "3":
                    handleListApartmentsByFilter();
                    break;
                case "4":
                    handleListUsersByFilter();
                    break;
                case "5":
                    handleListReservationsByFilter();
                    break;  
                case "6":
                    handleEntityStatusConsultation();
                    break;  
                case "7":
                    handleEntityHistoryConsultation();
                    break;
                case "8":
                    handleCancelReservation();
                    break;  
                case "9":
                    handleConfirmReservation();
                    break;  
                case "10":
                    handleDisableAccount();
                    System.out.println("Exiting...");
                    closeConnections();
                    System.exit(0);
                    break; 
                case "11":
                    handleApartmentMaintenance();
                    break;  
                case "0":
                    System.out.println("Exiting...");
                    closeConnections();
                    System.exit(0);
                    break;    
                default:         
                    System.out.println("Invalid option. Please try again.");      
            }
        }

    }

    /**
     * Print the authentication menu options
     */
    private void authMenu(){
        System.out.println("\n--- Welcome, Dear Client ---");
        System.out.println("1) Register user");
        System.out.println("2) Login with email");
        System.out.println("0) Exit");
        System.out.print("Select option: ");
    }

    /**
     * Print the client menu options
     */
    private void printMenu(){
        System.out.println("\n--- Client Options ---");
        System.out.println("1) Register apartment");
        System.out.println("2) Create reservation");

        System.out.println("3) List apartments (filter)");
        System.out.println("4) List users (filter)");
        System.out.println("5) List my reservations (filter)");

        System.out.println("6) Get entity status");
        System.out.println("7) Get entity history");

        System.out.println("8) Cancel reservation");
        System.out.println("9) Confirm reservation");
        System.out.println("10) Disable account");
        System.out.println("11) Put apartment maintenance");
        System.out.println("0) Exit");
        System.out.print("Select option: ");
    }

    /**
     * Handle registering a new client/user
     * @return true if registration was successful, false otherwise
     */
    private boolean handleRegisterClient(){
        try{
            String name;
            do{
                System.out.print("Name: ");
                name = in.readLine().trim();
            }while(name.trim().isEmpty());

            String email;
            do{
                System.out.print("Email: ");
                email = in.readLine().trim();
            }while(email.trim().isEmpty());

            String phone;
            do{
                System.out.print("Phone: ");
                phone = in.readLine().trim();
            }while(phone.trim().isEmpty());

            User newUser = User.userRequest(name, email, phone);

            sendRequest(new ClientRequest(RequestType.REGISTER_USER, newUser));
            return readLoginRegisterResponse();
        }catch(Exception E){
            System.out.println("Failed to register user: " + E.getMessage());
        }
        return false;
    }

    /**
     * Handle registering an apartment
     */
    private void handleRegisterApartment(){
        try{
            String name;
            do{
                System.out.print("Name of Apartment: ");
                name = in.readLine().trim();
            }while(name.trim().isEmpty());

            String location;
            do{
                System.out.print("Location: ");
                location = in.readLine().trim();
            }while(location.trim().isEmpty());

            BigDecimal price = BigDecimal.ZERO;
            while(true){
                System.out.print("Price per stay: ");
                String priceStr = in.readLine().trim();
                
                priceStr = priceStr.replace(",", "").trim();
                try {
                    price = new BigDecimal(priceStr);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format. Example: 45.00");
                }
            }
            
            int type;
            while (true) {
                System.out.print("Number of rooms (integer): ");
                String typeStr = in.readLine();
                try{
                    type = Integer.parseInt(typeStr.trim());
                    break;
                }catch(Exception e){
                    System.out.println("Invalid number. Please enter an integer.");
                }
            }

            Apartment newApartment = Apartment.apartmentRequest(name, location, price, type);

            sendRequest(new ClientRequest(RequestType.REGISTER_APARTMENT, newApartment));
            printReadGeneralResponse();
        }catch(Exception E){
            System.out.println("Failed to register apartment: " + E.getMessage());
        }
    }

    /**
     * Handle creating a reservation
     */
    private void handleCreateReservation(){
        try{
            int apartmentID;
            while (true) {
                System.out.print("ID of Apartment to make reservation (Integer): ");
                String IDStr = in.readLine();
                try{
                    apartmentID = Integer.parseInt(IDStr.trim());
                    break;
                }catch(Exception e){
                    System.out.println("Invalid number. Please enter an integer.");
                }
            }
            
            LocalDate startDate = readLocalDate("Start date (YYYY-MM-DD): ");
            LocalDate endDate;
            while (true) {
                endDate = readLocalDate("End date (YYYY-MM-DD): ");
                if (!endDate.isAfter(startDate)) {
                    System.out.println("End date must be after start date. Please enter a later date.");
                    continue;
                }
                break;
            }

            
            Reservation newReservation = Reservation.reservationRequest(apartmentID, this.userID, startDate, endDate);

            sendRequest(new ClientRequest(RequestType.CREATE_RESERVATION, newReservation));
            printReadGeneralResponse();
        }catch(Exception E){
            System.out.println("Failed to create reservation: " + E.getMessage());
        }
    }

    /**
     * Handle listing apartments by filter(s)
     */
    private void handleListApartmentsByFilter(){

        ArrayList<Filter> filters = new ArrayList<>();
        try {
            System.out.println("\nList Apartments — leave empty to skip a filter.");

            System.out.print("Location substring: ");
            String location = in.readLine().trim();


            String MinNumberOfRooms = promptAValidNumber("Minimum number of rooms (integer): ");
            String MaxNumberOfRooms = promptAValidNumber("Maximum number of rooms (integer): ");

            String MinPrice = promptAValidNumber("Minimum apartment price (integer): ");
            String MaxPrice = promptAValidNumber("Maximum apartment price (integer): ");

            if (!location.isEmpty()) {
                filters.add(new Filter("location", "ILIKE", location));
            }
            if (!MinNumberOfRooms.isEmpty()) {
                filters.add(new Filter("type", "GTE", MinNumberOfRooms));
            }
            if (!MaxNumberOfRooms.isEmpty()) {
                filters.add(new Filter("type", "LTE", MaxNumberOfRooms));
            }
            if (!MinPrice.isEmpty()) {
                filters.add(new Filter("price_per_night", "GTE", MinPrice));
            }
            if (!MaxPrice.isEmpty()) {
                filters.add(new Filter("price_per_night", "LTE", MaxPrice));
            }

            sendRequest(new ClientRequest(RequestType.LIST_APARTMENTS, filters));
            printReadListResponse();
        } catch (Exception e) {
            System.out.println("Error while listing users: " + e.getMessage());
        }
    }

    
    /**
     * Handle listing users by filter(s)
     */
    private void handleListUsersByFilter(){

        ArrayList<Filter> filters = new ArrayList<>();

        try {
            System.out.println("\nList users — leave empty to skip a filter.");
            System.out.print("Name substring: ");
            String name = in.readLine().trim();

            System.out.print("Email: ");
            String email = in.readLine().trim();

            System.out.print("Phone: ");
            String phone = in.readLine().trim();

            
            if (!name.isEmpty()) {
                filters.add(new Filter("name", "ILIKE", name));
            }
            if (!email.isEmpty()) {
                filters.add(new Filter("email", "EQ", email));
            }
            if (!phone.isEmpty()) {
                filters.add(new Filter("phone", "EQ", phone));
            }

            sendRequest(new ClientRequest(RequestType.LIST_USERS, filters));
            printReadListResponse();
        } catch (Exception e) {
            System.out.println("Error while listing users: " + e.getMessage());
        }
    }

    /**
     * Handle listing reservations by filter(s)
     */
    private void handleListReservationsByFilter(){
        
        ArrayList<Filter> filters = new ArrayList<>();
        try {
            System.out.println("\nList My Reservations — leave empty to skip a filter.");

            String ApartmentID = promptAValidNumber("ID of the Apartment: ");

            String MinPrice = promptAValidNumber("Minimum reservation price (integer): ");
            String MaxPrice = promptAValidNumber("Maximum reservation price (integer): ");

            if (!ApartmentID.isEmpty()) {
                filters.add(new Filter("apartment_id", "EQ", ApartmentID));
            }
            if (!MinPrice.isEmpty()) {
                filters.add(new Filter("total_price", "GTE", MinPrice));
            }
            if (!MaxPrice.isEmpty()) {
                filters.add(new Filter("total_price", "LTE", MaxPrice));
            }

            sendRequest(new ClientRequest(RequestType.LIST_RESERVATIONS, filters));
            printReadListResponse();
        } catch (Exception e) {
            System.out.println("Error while listing users: " + e.getMessage());
        }
    }


    /**
     * Handle consulting the status of an entity
     */
    private void handleEntityStatusConsultation(){
        try {
            String entity = "";
            while(!entity.equals("apartments") && !entity.equals("reservations") && !entity.equals("exit")){
                System.out.print("Entity (apartments|reservations) or \"exit\": ");
                entity = in.readLine().trim();
            }
            if (entity.equals("exit")) 
                return;

            String data = entity;
            sendRequest(new ClientRequest(RequestType.GET_ENTITY_STATUS, data));
            printReadListResponse();
        } catch (Exception e) {
            System.out.println("Error fetching entity status: " + e.getMessage());
        }
    }


    /**
     * Handle consulting the history of an entity
     */
    private void handleEntityHistoryConsultation(){
        try {
            String entity = "";
            while(!entity.equals("apartments") && !entity.equals("reservations") && !entity.equals("exit")){
                System.out.print("Type \"apartments\" for your apartments reservation history or \"reservations\" for your reservation history or \"exit\": ");
                entity = in.readLine().trim();
            }
            if (entity.equals("exit")) 
                return;

            String data = entity;
            sendRequest(new ClientRequest(RequestType.GET_ENTITY_HISTORY, data));
            printReadListResponse();

        } catch (Exception e) {
            System.out.println("Error fetching entity history: " + e.getMessage());
        }
    }


    /**
     * Handle cancelling a reservation
     */
    private void handleCancelReservation(){
        try{
            int type;
            while (true) {
                System.out.print("Id of the reservation (integer): ");
                String typeStr = in.readLine();
                try{
                    type = Integer.parseInt(typeStr.trim());
                    break;
                }catch(Exception e){
                    System.out.println("Invalid number. Please enter an integer.");
                }
            }
        
            sendRequest(new ClientRequest(RequestType.CANCEL_RESERVATION, type));
            printReadGeneralResponse();
        }catch(Exception E){
            System.out.println("Failed to cancel reservation: " + E.getMessage());
        }       
    }

    /**
     * Handle confirming a reservation
     */
    private void handleConfirmReservation(){
        try{
            int type;
            while (true) {
                System.out.print("Id of the reservation (integer): ");
                String typeStr = in.readLine();
                try{
                    type = Integer.parseInt(typeStr.trim());
                    break;
                }catch(Exception e){
                    System.out.println("Invalid number. Please enter an integer.");
                }
            }
        
            sendRequest(new ClientRequest(RequestType.CONFIRM_RESERVATION, type));
            printReadGeneralResponse();
        }catch(Exception E){
            System.out.println("Failed to confirm reservation: " + E.getMessage());
        }       
    }

    /**
     * Handle disabling/supending the user account
     */
    private void handleDisableAccount(){
        try{
            String warning = "";

            while(!warning.equals("Y") && !warning.equals("N")){
                    System.out.print("Are you sure you want to disable your account? (Y/N): ");
                try{
                    warning = in.readLine().trim();  
                }catch (Exception e) {
                    System.out.println("Error reading input. Please try again.");
                }
            }

            if(warning.equals("N")){
                return;
            }

            sendRequest(new ClientRequest(RequestType.DISABLE_ACCOUNT));
            printReadGeneralResponse();
        }catch(Exception E){
            System.out.println("Failed to disable account: " + E.getMessage());
        }
    }    

    /**
     * Handle putting an apartment into maintenance
     */
    private void handleApartmentMaintenance(){
        try{

            int type;
            while (true) {
                System.out.print("Id of your apartment (integer): ");
                String typeStr = in.readLine();
                try{
                    type = Integer.parseInt(typeStr.trim());
                    break;
                }catch(Exception e){
                    System.out.println("Invalid number. Please enter an integer.");
                }
            }
        
            sendRequest(new ClientRequest(RequestType.PUT_APARTMENT_MAINTENANCE, type));
            printReadGeneralResponse();
        }catch(Exception E){
            System.out.println("Failed to put apartment in maintenance: " + E.getMessage());
        }
    }
    
    /**
     * Send a request to the server
     * @param req The ClientRequest to send
     * @throws IOException If an I/O error occurs
     */
    private void sendRequest(ClientRequest req) throws IOException {
        oos.writeObject(req);
        oos.flush();
    }

    /**
     * Handle user login by email
     * @return true if login was successful, false otherwise
     */
    private boolean handleLoginByEmail(){
        try{
            String email;
            do{
                System.out.print("Email: ");
                email = in.readLine().trim();
            }while(email.trim().isEmpty());

            sendRequest(new ClientRequest(RequestType.LOGIN_USER, email));
            return readLoginRegisterResponse();
        }catch(Exception E){
            System.out.println("Failed to login: " + E.getMessage());
        }
        return false;
    }

    /**
     * Read the response after login or registration attempt
     * @return true if login/registration was successful, false otherwise
     */
    private boolean readLoginRegisterResponse(){
        try{
            Object resp = ois.readObject();
            
            if(resp instanceof User){
                User user = (User) resp;
                this.userID = user.id;
                return true;
            }else if(resp instanceof String){
                System.out.println((String)resp);
                return false;
            }
        }catch(Exception e){
            System.out.println("Failed to get user data");
        }
        return false;
    }

    /**
     * Prompt the user for a valid LocalDate input
     * @param prompt The prompt message to display
     * @return A valid LocalDate object
     */
    private LocalDate readLocalDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = in.readLine();
                if (line == null) {
                    System.out.println("Invalid input.");
                    continue;
                }
                line = line.trim();
                return LocalDate.parse(line, STRICT_DTF);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Expected format: YYYY-MM-DD (example: 2025-11-10).");
            } catch (IOException e) {
                System.out.println("Error reading input. Please try again.");
            }
        }
    }

    /**
     * Read and print the response for list requests
     */
    private void printReadListResponse() {
        System.out.println("\nList Response:\n");
        try {
            Object resp = ois.readObject();
            if (resp instanceof ArrayList) {
                ArrayList<?> list = (ArrayList<?>) resp;
                for (Object item : list) {
                    System.out.println(item);
                    System.out.println();
                }
            } else{
                System.out.println("Unexpected Error");
            }
        } catch (Exception e){
            System.out.println("Failed to read response: " + e.getMessage());
        }
    }

    /**
     * Read and print the response for general requests
     * 
     */
    private void printReadGeneralResponse() {
        try {
            Object resp = ois.readObject();
            if (resp instanceof String) {
                System.out.println("Response: " + String.valueOf(resp));
            }else if(resp instanceof User || resp instanceof Apartment || resp instanceof Reservation){
                System.out.println(resp);
            }
            else{
                System.out.println("Unexpected Error");
            }
        } catch (Exception e){
            System.out.println("Failed to read response: " + e.getMessage());
        }
    }

    /**
     * Prompt the user for a valid integer number, allowing empty input
     * @param prompt The prompt message to display
     * @return A valid integer as a string, or an empty string if no input is provided
     * @throws IOException If an I/O error occurs
     */
    private String promptAValidNumber(String prompt) throws IOException {
        String number;
        while (true) {
            System.out.print(prompt);
            String input = in.readLine().trim();

            if (input.isEmpty()) {
                number = "";
                break;
            }
            try {
                number = String.valueOf(Integer.parseInt(input));
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return number;
    }

    /**
     * Close socket connections
     */
    private void closeConnections(){
        try{
            sock.close();
        }catch(IOException e){
            System.out.println("Error closing socket: " + e.getMessage());
        }
    }

}