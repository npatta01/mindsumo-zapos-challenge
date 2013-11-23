package serviceclient.zappos;

import model.*;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;

/**
 * Response Parser
 */
public class ResponseParser {
    //jackson json parser
    private ObjectMapper om;

    /**
     * constructor
     */
    public ResponseParser() {
        om = new ObjectMapper();
        //ignore unknown properties
        om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Parse content as list of product with style info
     * @param content                                  content to parse
     * @return                                                       ProductWithStyle
     * @throws ZapposApiException
     */
    public ProductWithStyle[] parseProductResponse(String content) throws ZapposApiException {
        ProductResponse pr;
        try {
            pr = om.readValue(content, ProductResponse.class);
            if (pr.statusCode != 200) {
                return null;
            }
            if (pr.product == null) {
                throw new ZapposApiException("Received content from server can't be parsed to ProductResponse class", null);
            }
        } catch (Exception ioe) {
            throw new ZapposApiException("Received content from server can't be parsed to ProductResponse class", ioe);
        }

        ArrayList<ProductWithStyle> productWithStyleArrayList = new ArrayList<ProductWithStyle>();
        for (Product p : pr.product) {
            for (ProductWithStyle pws : p.styles) {
                pws.productId = p.productId;
                pws.productName = p.productName;
                productWithStyleArrayList.add(pws);
            }
        }
        return productWithStyleArrayList.toArray(new ProductWithStyle[productWithStyleArrayList.size()]);
    }

    /**
     * Search Response
     * @param content content to parse
     * @return                        results
     * @throws ZapposApiException
     */
    public ProductWithStyle[] parseSearchResponse(String content) throws ZapposApiException {
        SearchResponse sr;
        try {
            sr = om.readValue(content, SearchResponse.class);
        } catch (Exception ioe) {
            throw new ZapposApiException("Received content from server can't be parsed to SearchResponse class", ioe);
        }
        if (sr.statusCode != 200) {
            return null;
        }
        return sr.results;
    }
}
