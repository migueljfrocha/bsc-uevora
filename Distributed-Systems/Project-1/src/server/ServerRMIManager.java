package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import auxiliar.DBInfo;
import auxiliar.AdminRMIInterface;

class ServerRMIManager {

    String registryPort;
    String serviceName;
    String hostName;
    DBInfo dbInfo;


    /**
     * Constructor for ServerRMIManager
     * @param registryPort: String containing the registry port number
     * @param serviceName: String containing the service name
     * @param hostName: String containing the host name
     * @param dbInfo: DBInfo object containing database information
     */
    public ServerRMIManager(String registryPort, String serviceName, String hostName, DBInfo dbInfo) {

        this.registryPort = registryPort;
        this.serviceName = serviceName;
        this.hostName = hostName;
        this.dbInfo = dbInfo;
        setupRMIServer();

    }


    /**
     * Method to setup the RMI server
     */
    private void setupRMIServer(){

        try{
	        int regPort = Integer.parseInt(registryPort);

            LocateRegistry.createRegistry(regPort);
            System.out.println("[RMI] Registry started on port " + regPort);
            
            AdminRMIInterface adminService = new AdminRMIImpl(dbInfo);
            Naming.rebind("rmi://"+hostName+":" + regPort + "/" + serviceName, adminService);

            System.out.println("[RMI] Service '" + serviceName + "' bound successfully.");
            System.out.println("[SERVER] Ready to accept RMI requests...");
        } 

        catch (Exception ex) {
            System.out.println("[RMI ERROR] Failed to start RMI service: " + ex.getMessage());
            System.exit(1);
        }
    }

}