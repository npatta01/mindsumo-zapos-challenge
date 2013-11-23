package serviceclient.zappos;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import model.ProductWithStyle;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Zappos Client
 */
public class ZapposClient {
    //api key
    private final String apiKey;
    //base url
    private String baseUrl;
    //response parser
    private ResponseParser rp;

    public ZapposClient() {
        this.apiKey = "52ddafbe3ee659bad97fcce7c53592916a6bfd73";
        baseUrl = "http://api.zappos.com";
        rp = new ResponseParser();
    }

    /**
     * Initialize web resource to some default
     * @return  pre initialized resource
     */
    private WebResource getWebResource() {
        WebResource webResource;
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        webResource = client.resource(baseUrl);
        webResource.accept(MediaType.APPLICATION_JSON_TYPE);
        return webResource;
    }

    /**
     * Get product by id
     * @param productId product id
     * @return                    matches
     * @throws ZapposApiException
     */
    public ProductWithStyle[] getProductById(long productId) throws ZapposApiException {
        //http://api.zappos.com/Product/?id=7515478&includes=["styles","thumbnailImageUrl"]&key=52ddafbe3ee659bad97fcce7c53592916a6bfd73
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("id", String.valueOf(productId));
        String params = "[\"styles\",\"thumbnailImageUrl\"]";
        queryParams.add("includes", params);
        queryParams.add("key", apiKey);
        try {
            WebResource webResource = getWebResource().path("Product/").queryParams(queryParams);
            ClientResponse clientResponse = webResource.accept("application/json").get(ClientResponse.class);
            String output = clientResponse.getEntity(String.class);

            return rp.parseProductResponse(output);
        } catch (UniformInterfaceException uie) {
            throw new ZapposApiException("Product Not Found", uie);
        }
    }

    /**
     * Get product by name
     * @param productTerm query term
     * @return
     * @throws ZapposApiException
     */
    public ProductWithStyle[] getProductByName(String productTerm) throws ZapposApiException {
// http://api.zappos.com/Search?term=Classic%20Tall&key=52ddafbe3ee659bad97fcce7c53592916a6bfd73
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("term", productTerm);

        queryParams.add("key", apiKey);
        try {
            String s = getWebResource().path("Search/").queryParams(queryParams).get(String.class);
            return rp.parseSearchResponse(s);
        } catch (UniformInterfaceException uie) {
            throw new ZapposApiException("Product Not Found", uie);
        }
    }

    /**
     * Get product by style id
     * @param styleId         style id
     * @return                        search results
     * @throws ZapposApiException
     */
    public ProductWithStyle[] getProductByStyleId(long styleId) throws ZapposApiException {
        // http://api.zappos.com/Product/styleId/2123605?includes=[%22styles%22,%22thumbnailImageUrl%22]&key=52ddafbe3ee659bad97fcce7c53592916a6bfd73
        MultivaluedMap<String,String> queryParams = new MultivaluedMapImpl();
        String params = "[\"styles\",\"thumbnailImageUrl\"]";
        queryParams.add("includes", params);
        queryParams.add("key", apiKey);
        try {
            WebResource wr = getWebResource().path("Product/").path("styleId/").path(String.valueOf(styleId)).queryParams(queryParams);
            String s = wr.get(String.class);
            return rp.parseProductResponse(s);
        } catch (UniformInterfaceException uie) {
            throw new ZapposApiException("Product Not Found", uie);
        }
    }
}
