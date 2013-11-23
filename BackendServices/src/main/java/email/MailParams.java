package email;

import model.ProductWithStyle;

import java.util.List;

/**
 * Mail Params
 */
public class MailParams {
    //from email
    public String fromEmail;
    //to email
    public String toEmail;
    //unSubscribe link
    public String UnsubscribeLink;
    //view unSubscribe link
    public String ViewSubscribtionsLink;
    //verification link
    public String VerificationLink;
    //notification items
    public List<ProductWithStyle> items;
}
