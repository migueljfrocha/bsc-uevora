package auxiliar;

public enum RequestType {
    // Entity registering
    LOGIN_USER,
    REGISTER_USER,
    REGISTER_APARTMENT,
    CREATE_RESERVATION,

    // Entity listing with filters
    LIST_APARTMENTS,
    LIST_USERS,
    LIST_RESERVATIONS,

    //State and history search
    GET_ENTITY_STATUS,
    GET_ENTITY_HISTORY,

    // ADDONS
    CANCEL_RESERVATION,
    CONFIRM_RESERVATION,
    DISABLE_ACCOUNT,
    PUT_APARTMENT_MAINTENANCE
}