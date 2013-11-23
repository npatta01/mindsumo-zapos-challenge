package serviceclient.zappos;

import model.ProductWithStyle;

/**
 * Zappos Search Response
 */
public class SearchResponse {
    //matches
    public ProductWithStyle [] results;
    //error cide
    public int statusCode;
}
