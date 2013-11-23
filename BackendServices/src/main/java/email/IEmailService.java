package email;

/**
 * An emailservice that sends an email given a set of parameters
 */
public interface IEmailService {
    /**
     * Send email with the given parameters
     * @param mp  Parameters to send
     */
    public void sendWelcomeEmail(MailParams mp);

    /**
     * Send notification email
     * @param mp
     */
    public void sendNotificationEmail(MailParams mp);



}
