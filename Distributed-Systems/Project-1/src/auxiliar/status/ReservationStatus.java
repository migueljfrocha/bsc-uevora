package auxiliar.status;

public abstract class ReservationStatus {
    public static final String PENDING = "pending";
    public static final String CONFIRMED = "confirmed";
    public static final String CANCELLED = "cancelled";
    public static final String[] ALL_STATUS = {PENDING, CONFIRMED, CANCELLED};
}
