package auxiliar;

import java.io.Serializable;


// Request Class for the General Client to make server requests

 public class ClientRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private RequestType requestType;
    private Serializable requestData;

    /**
     * Constructor for ClientRequest class.
     * @param requestType The type of request being made.
     * @param requestData The data associated with the request.
     */
    public ClientRequest(RequestType requestType, Serializable requestData){
        this.requestType = requestType;
        this.requestData = requestData;
    }
    /**
     * Constructor for ClientRequest class without requestData.
     * @param requestType The type of request being made.
     */
    public ClientRequest(RequestType requestType){
        this.requestType = requestType;
        this.requestData = null;
    }
    /**
     * Gets the type of request.
     * @return The request type.
     */
    public RequestType getRequestType(){ 
        return requestType; 
    }
    /**
     * Gets the data associated with the request.
     * @return The request data.
     */
    public Serializable getRequestData(){
        return requestData; 
    }
}