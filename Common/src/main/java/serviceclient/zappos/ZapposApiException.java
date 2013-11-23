package serviceclient.zappos;

/**
 * Exception thrown when communicating/parsing Zappos messages
 */
public class ZapposApiException extends Exception {
    /**
     * Constructor
     * @param message
     * @param throwable
     */
     public ZapposApiException(String message, Throwable throwable){
         super(message,throwable);
     }

}
