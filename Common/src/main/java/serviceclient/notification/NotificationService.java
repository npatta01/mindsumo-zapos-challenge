package serviceclient.notification;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import config.ServerConfig;
import model.ProductWithStyle;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import serviceclient.zappos.ResponseParser;
import serviceclient.zappos.ZapposApiException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: nidhi_000
 * Date: 9/24/13
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class NotificationService {

    private final ResponseParser rp;


    @Inject
    private ServerConfig serverConfig;

    public NotificationService(){
        rp= new ResponseParser();
    }


    private WebResource getWebResource(){
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource webResource = client.resource(serverConfig.getBASE_URL());

        webResource.accept(MediaType.APPLICATION_JSON);
        return  webResource;
    }



    public  ProductWithStyle[] getProductByStyleId(String id) throws ZapposApiException {
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("style_id", String.valueOf(id));
        try {
            String s = getProductPath().queryParams(queryParams).get(String.class);
            return rp.parseSearchResponse(s);
        } catch (UniformInterfaceException uie) {
            throw new ZapposApiException("Product Not Found", uie);
        }
    }

    private WebResource getProductPath() {
        return getWebResource().path("Product");
    }

    public  ProductWithStyle[] getProductByProdcutId(String id) throws ZapposApiException {
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("product_id", id);
        try {
            String s = getProductPath().queryParams(queryParams).get(String.class);
            return rp.parseSearchResponse(s);
        } catch (UniformInterfaceException uie) {
            throw new ZapposApiException("Product Not Found", uie);
        }
    }

    public  ProductWithStyle[] getProductByName(String name) throws ZapposApiException {
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("product_name", name);
        try {
            String s = getProductPath().queryParams(queryParams).get(String.class);
            return rp.parseSearchResponse(s);
        } catch (UniformInterfaceException uie) {
            throw new ZapposApiException("Product Not Found", uie);
        }
    }

    public  String unregister(String email) {
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("email", String.valueOf(email));
        try {
            return getUserPath().path("unregister").queryParams(queryParams).get(String.class);
        } catch (UniformInterfaceException uie) {
            return "";
        }
    }

    public  void notify(String email, Long[] products) {
        SubscriptionRequest sr = new SubscriptionRequest();
        sr.emailAddress = email;
        sr.products = products;
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();

        try {

            ObjectMapper mapper = new ObjectMapper();
            String s= mapper.writeValueAsString(sr)        ;
            getUserPath().path("notify").type(MediaType.APPLICATION_JSON).post(ClientResponse.class,s);
        } catch (UniformInterfaceException ignored) {
        } catch (JsonMappingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JsonGenerationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public  void unNotify(String email, Long[] products) {
        SubscriptionRequest sr = new SubscriptionRequest();
        sr.emailAddress = email;
        sr.products = products;
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("email", String.valueOf(email));
        try {
            ObjectMapper mapper = new ObjectMapper();
           String s= mapper.writeValueAsString(sr)        ;
            getUserPath().path("unnotify").queryParams(queryParams).type(MediaType.APPLICATION_JSON).post(s);
        } catch (UniformInterfaceException uie) {
        } catch (JsonMappingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JsonGenerationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public  ProductWithStyle[] register(String email) {
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("email", String.valueOf(email));
        try {
            String s = getUserPath().path("register").queryParams(queryParams).get(String.class);
            return rp.parseSearchResponse(s);
        } catch (Exception uie) {
            return null;
        }
    }

    private WebResource getUserPath() {
        return getWebResource().path("User");
    }

    public  String verify(String email, String verificationCode) {
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("email", email);
        queryParams.add("verificationCode", verificationCode);
        try {
            return getUserPath().path("verify").queryParams(queryParams).get(String.class);
        } catch (UniformInterfaceException uie) {
            return "";
        }
    }
}
