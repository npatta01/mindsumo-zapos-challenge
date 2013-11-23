package model;

/**
 * Verification Status
 */
public enum VerificationEnum {
    ALREADY_VERIFIED("Already Verified"), VERIFIED("Verified"), INVALID("Invalid verification code"), NOT_A_USER("No user exists"),
    UNSUBSCRIBED("Unsubscribed"), NEWLY_REGISTERED("Registered. Check Email"), NOTIFIED("Registered to be notified"), UNNOTIFIED("Unotified");
    private String message;

    /**
     * Constructor
     * @param message string message
     */
    private VerificationEnum(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
