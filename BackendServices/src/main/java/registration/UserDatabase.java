package registration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * In Memory User Database
 */
public class UserDatabase {
    //mapping from email to style ids
    private HashMap<String, HashSet<Long>> emailProductIds;
    //mapping from style to email
    private HashMap<Long, HashSet<String>> productIdEmail;
    //email to verification code
    private HashMap<String, String> verificationCodes;

    /**
     * Constructor
     */
    public UserDatabase() {
        emailProductIds = new HashMap<>();
        productIdEmail = new HashMap<>();
        verificationCodes = new HashMap<>();
    }

    /**
     * Does user exists
     * @param email    email address
     * @return                      exisitng user
     */
    public synchronized boolean userExists(String email) {
        return emailProductIds.containsKey(email);
    }

    /**
     * Delete existing users subscription
     * @param email                      email
     */
    public synchronized void deleteUsersSubscription(String email) {
        if (userExists(email)) {
            HashSet<Long> subsribedprodcuts = emailProductIds.get(email);
            //remove existing subscriptions
            for (Long p : subsribedprodcuts) {
                HashSet<String> strings = productIdEmail.get(p);
                if (strings != null) {
                    strings.remove(email);
                }
            }
        }
    }

    /**
     * Remove user completely from system
     * @param email                      email
     */
    public synchronized void removeUser(String email) {
        deleteUsersSubscription(email);
        emailProductIds.remove(email);
    }

    /**
     *
     * @param email
     * @return
     */
    public boolean isUserVerified(String email) {
        if (userExists(email) && !verificationCodes.containsKey(email)) {
            return true;
        }
        return false;
    }

    /**
     * Remove user from system
     * @param email
     */
    public void verifyUser(String email) {
        verificationCodes.remove(email);
    }

    /**
     * Add the list of items to users subscription list
     * @param email                                    email
     * @param products                                       products
     */
    public synchronized void addSubscriptions(String email, Long[] products) {
        HashSet<Long> subsribedprodcuts = emailProductIds.get(email);
        for (Long p : products) {
            subsribedprodcuts.add(p);
        }
        for (Long p : products) {
            if (productIdEmail.containsKey(p)) {
                productIdEmail.get(p).add(email);
            } else {
                HashSet<String> hs = new HashSet<String>();
                hs.add(email);
                productIdEmail.put(p, hs);
            }
        }
    }

    /**
     * Unsubscribe user form given items
     * @param email                     email
     * @param products  products
     */
    public synchronized void removeSubscriptions(String email, Long[] products) {
        emailProductIds.remove(email);
        for (Long p : products) {
            productIdEmail.get(p).remove(email);
        }
    }

    /**
     * Get all the items the given user is subscribed to
     * @param email                                     email address
     * @return                                                       products
     */
    public synchronized Long[] getSubscribedItems(String email) {
        if (userExists(email)) {
            HashSet<Long> products = emailProductIds.get(email);
            if (products != null) {
                return products.toArray(new Long[products.size()]);
            }
        }
        return null;
    }

    /**
     * Get the list of all subscribed items
     * @return                             subscribed items
     */
    public synchronized Long[] getAllSubscribedItems() {
        Set<Long> products = productIdEmail.keySet();
        if (products != null) {
            return products.toArray(new Long[products.size()]);
        }
        return null;
    }

    /**
     * Register user in system
     * @param email  email address
     * @return
     */
    public synchronized String registerUser(String email) {
        if (userExists(email)) {
            return null;
        } else {
            emailProductIds.put(email, new HashSet<Long>());
            String uuid = UUID.randomUUID().toString();
            verificationCodes.put(email,uuid);
            return uuid;
        }
    }

    /**
     * Get verification code
     * @param email         email
     * @return                   verification cide
     */
    public synchronized String getVerificationCode(String email) {
        return verificationCodes.get(email);
    }

    /**
     * Get all the users subscribed ot the given item
     * @param productId                              product id
     * @return                                                 list of users
     */
    public synchronized String[] getSubscribedUsersForItem(long productId) {
        HashSet<String> subscribedUsers = productIdEmail.get(productId);
        if (subscribedUsers == null) {
            return null;
        } else {
            return subscribedUsers.toArray(new String[subscribedUsers.size()]);
        }
    }
}