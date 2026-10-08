package adminClient;

import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import java.io.Serializable;
import java.io.IOException;
import java.io.BufferedReader;

import java.math.BigDecimal;

import auxiliar.*;
import auxiliar.status.ApartmentStatus;
import auxiliar.status.ReservationStatus;
import auxiliar.status.UserStatus;
import models.User;
import models.Apartment;
import models.Reservation;

public class AdminClientManager {

    private AdminRMIInterface admin;
    private BufferedReader in;
    private static final DateTimeFormatter STRICT_DTF =
        DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Constructor for AdminClientManager class
     * @param admin The remote admin RMI interface
     * @param in BufferedReader for user input
     */
    public AdminClientManager(AdminRMIInterface admin, BufferedReader in) {
        this.admin = admin;
        this.in = in;
    }

    /**
     * Main admin panel loop logic
     */
    public void adminPanel() throws Exception {
        while(true){
            printMenu();
            String line = in.readLine();
            if(line == null || line.trim().isEmpty()) continue;

            switch(line.trim()) {
                case "1":
                    handleListUsers();
                    break;
                case "2":
                    handleListApartments();
                    break;
                case "3":
                    handleListReservations();
                    break;
                case "4":
                    handleUpdateAdminStatus();
                    break;
                case "5":
                    handleUpdateOperationalStatus();
                    break;
                case "6":
                    handleEditEntity();
                    break;
                case "7":
                    handleAddEntity();
                    break;
                case "8":
                    handleDeleteEntity();
                    break;
                case "0":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Print the admin menu options
     */
    private void printMenu() {
        System.out.println("\n--- Admin Options ---");
        System.out.println("1) List users by admin state");
        System.out.println("2) List apartments by admin state");
        System.out.println("3) List reservations by admin state");

        System.out.println("4) Update entity admin status");
        System.out.println("5) Update entity operational status");

        System.out.println("6) Edit entity attribute");
        System.out.println("7) Add entity");
        System.out.println("8) Delete entity");

        System.out.println("0) Exit");
        System.out.print("Select option: ");
    }


    /**
     * Handle listing of users by admin status
     */
    private void handleListUsers() {
        String status = "";
        while(!status.equals("approved") && !status.equals("rejected") && !status.equals("exit")){
            System.out.print("\nChoose admin status to filter(\"approved\"|\"rejected\") or \"exit\": ");
            try{
                status = in.readLine().trim();  
            }catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
            }
        }

        if(status.equals("exit")){
            return;
        }
        
        try{
            List<User> users = admin.getUsersList(status);

            for(User user : users){
                System.out.println();
                System.out.println(user.toString());
            }

        }catch(Exception E){
            System.out.println("Remote call failed: " + E.getMessage());
        }
    }

    /**
     * Handle listing of apartments by admin status
     */
    private void handleListApartments() {
        String status = "";
        while(!status.equals("approved") && !status.equals("rejected") && !status.equals("exit")){
            System.out.print("\nChoose admin status to filter(\"approved\"|\"rejected\" or \"exit\") : ");
            try{
                status = in.readLine().trim();  
            }catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
            }
        }

        if(status.equals("exit")){
            return;
        }
        
        try{
            List<Apartment> apartments = admin.getApartmentsList(status);

            for(Apartment ap : apartments){
                System.out.println();
                System.out.println(ap.toString());
            }

        }catch(Exception E){
            System.out.println("Remote call failed: " + E.getMessage());
        }
    }

    /**
     * Handle listing of reservations by admin status
     */
    private void handleListReservations() {
        String status = "";
        while(!status.equals("approved") && !status.equals("rejected") && !status.equals("exit")){
            System.out.print("\nChoose admin status to filter(\"approved\"|\"rejected\" or \"exit\") : ");
            try{
                status = in.readLine().trim();  
            }catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
            }
        }

        if(status.equals("exit")){
            return;
        }
        
        try{
            List<Reservation> reservations = admin.getReservationsList(status);

            for(Reservation reservation : reservations){
                System.out.println();
                System.out.println(reservation.toString());
            }

        }catch(Exception E){
            System.out.println("Remote call failed: " + E.getMessage());
        }
    }

    /**
     * Handle updating of an entity admin status
     */
    private void handleUpdateAdminStatus(){

        String entity = getEntity();
        if(entity.equals("exit")){
            return;
        }

        int id = getEntityID();
        
        String status = "";
        while(!status.equals("approved") && !status.equals("rejected")){
            System.out.print("\nNew admin status(\"approved\"|\"rejected\") : ");
            try{
                status = in.readLine().trim();  
            }catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
            }
        }
        
        try {
            if (admin.updateEntityAdminStatus(entity, status, id)) {
                System.out.println("Admin status updated successfully.");
            } else {
                System.out.println("Failed to update admin status.");
            }
        } catch (Exception remoteEx) {;
            System.out.println("Remote call failed: " + remoteEx.getMessage());
        }
    }


    /**
     * Handle updating of an entity operational status
     */
    private void handleUpdateOperationalStatus() {

        String[] states;
        String entity = getEntity();
        
        if(entity.equals("exit")){
            return;
        }
        
        if(entity.equals("users")){
            states = UserStatus.ALL_STATUS;
        }else if(entity.equals("apartments")){
            states = ApartmentStatus.ALL_STATUS;
        }else{
            states = ReservationStatus.ALL_STATUS;
        }


        int id = getEntityID();
        
        String status = "";
        while(!status.equals(states[0]) && !status.equals(states[1]) && !status.equals(states[2])){
            System.out.print("\nNew operational status (\"" + states[0] + "\"|\"" + states[1] + "\"|\"" + states[2] + "\"): ");
            try{
                status = in.readLine().trim();  
            }catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
            }
        }
        
        try {
            if (admin.updateEntityOperationalStatus(entity, status, id)){
                System.out.println("Operational status updated successfully.");
            } else {
                System.out.println("Failed to update operational status.");
            }
        } catch (Exception remoteEx) {
            System.out.println("Remote call failed: " + remoteEx.getMessage());
        }
    }

    /**
     * Handle editing of an entity attribute
     */
    private void handleEditEntity() {
        
        String entity = getEntity();
        
        if(entity.equals("exit")){
            return;
        }

        int id = getEntityID();

        String attributeToEdit = "";
        String attributeValue = "";


        try{
            if(entity.equals("users")){
                String out = "Attribute to edit(";
                for(int i = 0; i < User.editableAttributes.length; i++){
                    String attr = User.editableAttributes[i];
                    out += attr;
                    if (i < User.editableAttributes.length - 1) {
                        out += "|";
                    }
                }
                out += "): ";
                System.out.print(out);
                
                attributeToEdit = in.readLine().trim();

                while(!User.isEditableAttribute(attributeToEdit)){
                    System.out.print("Invalid attribute. Choose again: ");
                    attributeToEdit = in.readLine().trim();
                }
            
                attributeValue = "";
                System.out.print("New value: ");
                attributeValue = in.readLine().trim();


            }else if(entity.equals("apartments")){
                String out = "Attribute to edit(";
                for (int i = 0; i < Apartment.editableAttributes.length; i++) {
                    String attr = Apartment.editableAttributes[i];
                    out += attr;
                    if (i < Apartment.editableAttributes.length - 1) {
                        out += "|";
                    }
                }
                out += "): ";
                System.out.print(out);

                attributeToEdit = in.readLine().trim();

                while(!Apartment.isEditableAttribute(attributeToEdit)){
                    System.out.print("Invalid attribute. Choose again: ");
                    attributeToEdit = in.readLine().trim();
                }

                if(attributeToEdit.equals("price_per_night")){
                    while (true) {
                        System.out.print("New value (decimal, ex: 123.45): ");
                        try {
                            String raw = in.readLine().trim();
                            BigDecimal priceValue = new BigDecimal(raw);
                            attributeValue = priceValue.stripTrailingZeros().toPlainString();
                            break;
                        } catch (NumberFormatException | NullPointerException e) {
                            System.out.println("Invalid price. Please enter a valid decimal number.");
                        } catch (Exception e) {
                            System.out.println("Error reading input. Please try again.");
                        }
                    }
                }else if(attributeToEdit.equals("type")){
                    while (true) {
                        System.out.print("New value (integer): ");
                        try {
                            int attributeIdValue = Integer.parseInt(in.readLine().trim());
                            attributeValue = Integer.toString(attributeIdValue);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID. Please enter a valid integer.");
                        } catch (Exception e) {
                            System.out.println("Error reading input. Please try again.");
                        }
                    }
                }else if(attributeToEdit.equals("location") || attributeToEdit.equals("name")){
                    System.out.print("New value: ");
                    attributeValue = in.readLine().trim();
                }


            }else if(entity.equals("reservations")){
                String out = "Attribute to edit(";
                for (int i = 0; i < Reservation.editableAttributes.length; i++) {
                    String attr = Reservation.editableAttributes[i];
                    out += attr;
                    if (i < Reservation.editableAttributes.length - 1) {
                        out += "|";
                    }
                }
                out += "): ";
                System.out.print(out);

                attributeToEdit = in.readLine().trim();

                while(!Reservation.isEditableAttribute(attributeToEdit)){
                    System.out.print("Invalid attribute. Choose again: ");
                    attributeToEdit = in.readLine().trim();
                }
                if(attributeToEdit.equals("renter_id")){
                    while (true) {
                        System.out.print("New value (integer): ");
                        try {
                            int attributeIdValue = Integer.parseInt(in.readLine().trim());
                            attributeValue = Integer.toString(attributeIdValue);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID. Please enter a valid integer.");
                        } catch (Exception e) {
                            System.out.println("Error reading input. Please try again.");
                        }
                    }
                }else if(attributeToEdit.equals("total_price")){
                    while (true) {
                        System.out.print("New value (decimal, ex: 123.45): ");
                        try {
                            String raw = in.readLine().trim();
                            BigDecimal priceValue = new BigDecimal(raw);
                            attributeValue = priceValue.stripTrailingZeros().toPlainString();
                            break;
                        } catch (NumberFormatException | NullPointerException e) {
                            System.out.println("Invalid price. Please enter a valid decimal number.");
                        } catch (Exception e) {
                            System.out.println("Error reading input. Please try again.");
                        }
                    }
                }
            }


        }catch(Exception E){
            System.out.println("Error reading input. Please try again.");
            return;    
        }

        try {
            if (admin.editEntity(entity, id, attributeToEdit, attributeValue)) {
                System.out.println("Entity edited successfully.");
            } else {
                System.out.println("Failed to update entity.");
            }
        } catch (Exception remoteEx) {
            System.out.println("Remote call failed: " + remoteEx.getMessage());
        }
    }

    /**
     * Handle addition of a new entity (user, apartment, reservation) or exit
     */
    private void handleAddEntity() {
        String entity = getEntity();
        
        Serializable newEntity = null;

        if(entity.equals("exit")){
            return;
        }else if(entity.equals("users")){
            newEntity = addUserEntity();
        }else if(entity.equals("apartments")){
            newEntity = addApartmentEntity();
        }else if(entity.equals("reservations")){
            newEntity = addReservationEntity();
        }

        try {
            if (admin.addEntity(newEntity)) {
                System.out.println("Entity added successfully.");
            } else {
                System.out.println("Failed to add entity.");
            }
        } catch (Exception remoteEx) {
            System.out.println("Remote call failed: " + remoteEx.getMessage());
        }
    }

    
    /**
     * Handle updating of an entity operational status
     */
    private Serializable addUserEntity(){
        
        String[] states = UserStatus.ALL_STATUS;
        String name, email, phone, status = "";
        try{
            do{
                System.out.print("Name: ");
                name = in.readLine().trim();
            }while(name.trim().isEmpty());

            do{
                System.out.print("Email: ");
                email = in.readLine().trim();
            }while(email.trim().isEmpty());

            do{
                System.out.print("Phone: ");
                phone = in.readLine().trim();
            }while(phone.trim().isEmpty());

            while(!status.equals(states[0]) && !status.equals(states[1]) && !status.equals(states[2])){
                System.out.print("\nNew operational status (\"" + states[0] + "\"|\"" + states[1] + "\"|\"" + states[2] + "\"): ");
                try{
                    status = in.readLine().trim();  
                }catch (Exception e) {
                    System.out.println("Error reading input. Please try again.");
                }
            }
            
        }catch(Exception E){
            System.out.println("Error reading input. Please try again.");
            return null;    
        }
        
        return new User(-1,
                        name,
                        email,
                        phone,
                        status,
                        "approved",
                        null);
    }

    /**
     * Add an apartment entity by reading user input
     * @return A new Apartment object
     */
    private Serializable addApartmentEntity(){
        String[] states = ApartmentStatus.ALL_STATUS;
        String name, location, status = "";
        int ownerId, type;
        BigDecimal pricePerStay;

        try{
            do{
                System.out.print("Name of Apartment: ");
                name = in.readLine().trim();
            }while(name.trim().isEmpty());

            do{
                System.out.print("Location: ");
                location = in.readLine().trim();
            }while(location.trim().isEmpty());

            while(true){
                System.out.print("Price per stay: ");
                String priceStr = in.readLine().trim();
                
                priceStr = priceStr.replace(",", "").trim();
                try {
                    pricePerStay = new BigDecimal(priceStr);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format. Example: 45.00");
                }
            }
            
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

            while(!status.equals(states[0]) && !status.equals(states[1]) && !status.equals(states[2])){
                System.out.print("\nNew operational status (\"" + states[0] + "\"|\"" + states[1] + "\"|\"" + states[2] + "\"): ");
                try{
                    status = in.readLine().trim();  
                }catch (Exception e) {
                    System.out.println("Error reading input. Please try again.");
                }
            }

            while (true) {
                System.out.print("Owner ID (integer): ");
                String ownerIdStr = in.readLine();
                try{
                    ownerId = Integer.parseInt(ownerIdStr.trim());
                    break;
                }catch(Exception e){
                    System.out.println("Invalid number. Please enter an integer.");
                }
            }
        

        }catch(Exception E){
            System.out.println("Error reading input. Please try again.");
            return null;    
        }

        return new Apartment(
            -1,
            name,
            location,
            pricePerStay,
            type,
            status,
            "approved",
            ownerId
        );
    }
    /**
     * Add a reservation entity by reading user input
     * @return A new Reservation object
     */
    private Serializable addReservationEntity(){
        String[] states = ReservationStatus.ALL_STATUS;
        String status = "";
        int apartmentID, renterID;
        LocalDate startDate, endDate;
        BigDecimal totalValue;
        
        try{
            
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

            while (true) {
                System.out.print("Renter ID (Integer): ");
                String IDStr = in.readLine();
                try{
                    renterID = Integer.parseInt(IDStr.trim());
                    break;
                }catch(Exception e){
                    System.out.println("Invalid number. Please enter an integer.");
                }
            }
            
            startDate = readLocalDate("Start date (YYYY-MM-DD): ");

            while (true) {
                endDate = readLocalDate("End date (YYYY-MM-DD): ");
                if (!endDate.isAfter(startDate)) {
                    System.out.println("End date must be after start date. Please enter a later date.");
                    continue;
                }
                break;
            }

            while(!status.equals(states[0]) && !status.equals(states[1]) && !status.equals(states[2])){
                System.out.print("\nNew operational status (\"" + states[0] + "\"|\"" + states[1] + "\"|\"" + states[2] + "\"): ");
                try{
                    status = in.readLine().trim();  
                }catch (Exception e) {
                    System.out.println("Error reading input. Please try again.");
                }
            }

            while(true){
                System.out.print("Total Price: ");
                String priceStr = in.readLine().trim();
                
                priceStr = priceStr.replace(",", "").trim();
                try {
                    totalValue = new BigDecimal(priceStr);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format. Example: 45.00");
                }
            }
            

        }catch(Exception E){
            System.out.println("Error reading input. Please try again.");
            return null;    
        }

        return new Reservation(
            -1,
            apartmentID,
            renterID,
            startDate,
            endDate,
            totalValue,
            status,
            "approved",
            null
        );

    }

    /**
     * Handle deletion of an entity
     */
    private void handleDeleteEntity() {
        String entity = getEntity();

        if(entity.equals("exit")){
            return;
        }

        int id = getEntityID();

        try {
            if (admin.deleteEntity(entity, id)) {
                System.out.println("Entity deleted successfully.");
            } else {
                System.out.println("Failed to delete entity.");
            }
        } catch (Exception remoteEx) {
            System.out.println("Remote call failed: " + remoteEx.getMessage());
        }
    }

    /**
     * Get an entity type or exit String from user input
     * @return The entity type as a string
     */
    private String getEntity() {
        String entity = "";

        while(!entity.equals("users") && !entity.equals("apartments") && !entity.equals("reservations") && !entity.equals("exit")){
            System.out.print("Entity (users|apartments|reservations) or \"exit\": ");
            try{
                entity = in.readLine().trim();
            }catch(Exception E){
                System.out.println("Error reading input. Please try again.");
            }
        }
        return entity;
    }

    /**
     * Get an entity ID from user input
     * @return The entity ID as an integer
     */
    private int getEntityID() {
        int id;
        while(true) {        
            System.out.print("\nEntity ID (integer): ");
            try{
                id = Integer.parseInt(in.readLine().trim());
                return id;
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID. Please enter a valid integer.");
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
            }
        }
    }

    /**
     * Read a LocalDate from user input with strict format checking
     * @param prompt The prompt to display to the user
     * @return The parsed LocalDate
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
}
