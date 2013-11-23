package registration;

import com.google.inject.Inject;
import config.ServerConfig;
import email.IEmailService;
import email.MailParams;
import model.VerificationEnum;

/**
 * Registration Service
 */
public class RegistrationService {
   //user database
    private  UserDatabase userDatabase;
    //email service
    private IEmailService iEmailService;

    //server config
    private ServerConfig serverConfig;

    /**
     * Constructor
     * @param sc  server config
     * @param iEmailService    email service
     * @param userDatabase                  userdatabse
     */
    @Inject
    public RegistrationService(ServerConfig sc,IEmailService iEmailService, UserDatabase userDatabase) {

        serverConfig=sc;
        this.iEmailService=iEmailService;
        this.userDatabase =userDatabase;

    }

    /**
     * Subscribe user to the given style ids
     * @param emailAddress email address
     * @param products                  products
     */
    public void subscribe(String emailAddress, Long[] products) {
        if      (userDatabase.userExists(emailAddress)) {
                   userDatabase.deleteUsersSubscription(emailAddress);
                  userDatabase.addSubscriptions(emailAddress,products);


        }
    }

    /**
     * Unsubscribe user from given style ids
     * @param emailAddress  email address
     * @param products                 products
     * @return   verification status
     */
    public VerificationEnum unSubscribe(String emailAddress, Long[] products) {
        if (userExists(emailAddress)) {
            userDatabase.removeSubscriptions(emailAddress,products);
            return VerificationEnum.UNNOTIFIED;
        } else {
            return VerificationEnum.NOT_A_USER;
        }
    }

    /**
     * Un subscribe user from system
     * @param emailAddress
     * @return verification status
     */
    public VerificationEnum unSubscribeAll(String emailAddress) {
        if (userExists(emailAddress)) {
            userDatabase.removeUser(emailAddress);

            return VerificationEnum.UNSUBSCRIBED;
        } else {
            return VerificationEnum.NOT_A_USER;
        }
    }

    /**
     * Get subscribed items for user
     * @param emailAddress   email
     * @return    subscribed ids
     */
    public Long[] getSubscribedItems(String emailAddress) {

        return userDatabase.getSubscribedItems(emailAddress);
    }

    /**
     * Get all the products beings tracked by all users
     * @return                                                 list of style ids
     */
    public Long [] getAllSubscribedItems (){
       return userDatabase.getAllSubscribedItems();
    }

    /**
     * Get all users subscribed to given style id
     * @param prodcutId                          style id
     * @return                                   list of emails
     */
    public String[] getSubscribedUsers(long prodcutId) {
       return userDatabase.getSubscribedUsersForItem(prodcutId);
    }

    /**
     * Does user exist in system
     * @param emailAddress      email
     * @return                       true if exists
     */
    public boolean userExists(String emailAddress) {
        return userDatabase.userExists(emailAddress);
    }

    /**
     * Register user in system
     * @param emailAddress    email
     * @return    uuid if new user
     */
    public boolean register(String emailAddress) {
        String uuid = userDatabase.registerUser(emailAddress);
        if(uuid==null){
                  return false;
        }else{
            MailParams mailParamsForUser = getMailParamsForUser(emailAddress);
            mailParamsForUser.VerificationLink=getVerificationUrl(emailAddress,uuid);
            iEmailService.sendWelcomeEmail(mailParamsForUser);
            return true;

        }
    }

    /**
     * Verify User
     * @param emailAddress email address
     * @param verificationKey          verification key
     * @return   status of user
     */
    public VerificationEnum verifyUser(String emailAddress, String verificationKey) {
        String verificationCode =userDatabase.getVerificationCode(emailAddress);

        if(verificationCode==null){
            if(userDatabase.userExists(emailAddress)){
                return VerificationEnum.ALREADY_VERIFIED;
            }        else{
                return VerificationEnum.NOT_A_USER;
            }
        }                   else{
            if(verificationCode.equals(verificationKey)){
                userDatabase.verifyUser(emailAddress);
                return VerificationEnum.VERIFIED;
            } else {
                return VerificationEnum.INVALID;
            }
        }

    }

    /**
     * Get un subscribe url
     * @param emailAddress       email
     * @return un subscribe url
     */
    private String getUnsubscriptionUrl(String emailAddress){
        return serverConfig.getBASE_URL()+"/User/unregister?email="+emailAddress ;
    }

    /**
     * Generate subscription Url
     * @param email              email
     * @return generated subscription url
     */
    private String getSubscriptionUrl(String email){
        return serverConfig.getBASE_URL()+"/User/register?email="+email ;

    }

    /**
     * Verification URL
     * @param email    email
     * @param uuid          uuid
     * @return                  verification link
     */
    private String getVerificationUrl(String email, String uuid){
        return serverConfig.getBASE_URL()+"/User/verify?email="+email+"&verificationCode="+uuid ;
    }

    /**
     * Return mail parameters for user
     * @param email                   email parameters
     * @return    Mail params
     */
    public MailParams getMailParamsForUser(String email){
        MailParams mp = new MailParams();
        mp.fromEmail = "notification@npatta01-zappos.com";
        mp.toEmail = email;
        mp.UnsubscribeLink = getUnsubscriptionUrl(email);
        mp.ViewSubscribtionsLink=getSubscriptionUrl(email);

        return mp;
    }
}

