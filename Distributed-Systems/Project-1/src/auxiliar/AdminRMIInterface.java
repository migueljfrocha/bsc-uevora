package auxiliar;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import models.Apartment;
import models.Reservation;
import models.User;


public interface AdminRMIInterface extends Remote {
    //Aprove/refuse entities and change operational status 
    public boolean updateEntityAdminStatus(String entity, String status, int entityIdValue) throws RemoteException;
    public boolean updateEntityOperationalStatus(String entity, String status, int entityIdValue) throws RemoteException;
    
    // Attribute/resource management
    public boolean deleteEntity(String entity, int entityIdValue) throws RemoteException;
    public boolean addEntity(Serializable entity) throws RemoteException;
    public boolean editEntity(String entity, int entityIdValue, String attributeToEdit, String newValue) throws RemoteException;

    // List entities from adminState
    public List<Apartment> getApartmentsList(String adminState) throws RemoteException;
    public List<User> getUsersList(String adminState) throws RemoteException;
    public List<Reservation> getReservationsList(String adminState) throws RemoteException;
}
