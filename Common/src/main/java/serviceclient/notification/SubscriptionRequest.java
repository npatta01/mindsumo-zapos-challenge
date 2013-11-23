package serviceclient.notification;

import javax.ws.rs.Produces;

/**
 * Notification SErver Message Body
 */
@Produces("application/json")
public class SubscriptionRequest {

    //email address
    public String emailAddress;
    //style ids
    public Long [] products;
}
