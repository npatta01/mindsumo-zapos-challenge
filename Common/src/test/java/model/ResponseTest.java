package model;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import serviceclient.zappos.ProductResponse;
import serviceclient.zappos.ResponseParser;
import serviceclient.zappos.ZapposApiException;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: nidhi_000
 * Date: 9/15/13
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResponseTest {
    private ObjectMapper objectMapper;
    private String productFile;
    private String searchFile;
    private String invalidFile;
    private String invalidResponse;
    private ProductResponse pr;
    private ResponseParser rp;

    @BeforeClass
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        productFile = "/product_single_style.json";
        searchFile = "/search_single.json";
        invalidFile = "/invalidStatusCode.json";
        invalidResponse = "/invalidResponse.json";
        rp = new ResponseParser();
    }

    @Test
    public void Product_Has_Required_Fields() throws Exception {
        String content = parseFile(productFile);
        ProductWithStyle[] ps = rp.parseProductResponse(content);
        assert (ps.length == 1);
        ProductWithStyle pws = ps[0];
        assert (pws.styleId == 1788226);
        assert (65.00 == pws.price);
        assert (0 == pws.percentOff);
        assert (65.00 == pws.originalPrice);
        assert (!pws.productUrl.isEmpty());
        assert (!pws.imageUrl.isEmpty());
        assert (!pws.thumbnailImageUrl.isEmpty());
        assert (pws.productName.equals("Gigi"));
    }

    @Test
    public void Style_Has_Required_Fields() throws Exception {
        String content = parseFile(searchFile);
        ProductWithStyle[] ps = rp.parseSearchResponse(content);
        Assert.assertEquals(1, ps.length);
        ProductWithStyle pws = ps[0];
        assert (pws.styleId == 34320);
        assert (195.00 == pws.price);
        assert (0 == pws.percentOff);
        assert (195.00 == pws.originalPrice);
        assert (pws.productName.equals("Classic Tall"));
        assert (!pws.productUrl.isEmpty());
        assert (pws.imageUrl.isEmpty());
        assert (!pws.thumbnailImageUrl.isEmpty());
        Assert.assertEquals(115328, pws.productId);
    }

    public void null_returned_for_non_200_status_code() throws Exception {
        String content = parseFile(invalidFile);
        ProductWithStyle[] ps = rp.parseProductResponse(content);
        Assert.assertEquals(null, ps);
        ps = rp.parseSearchResponse(content);
        Assert.assertEquals(null, ps);
    }

    public void unknown_server_response() throws Exception {
        String content = parseFile(invalidResponse);
        try {
            ProductWithStyle[] ps = rp.parseProductResponse(content);
            Assert.fail("No exception thrown");
        } catch (ZapposApiException zae) {
        }
        try {
            ProductWithStyle[] ps = rp.parseSearchResponse(content);
            Assert.fail("No exception thrown");
        } catch (ZapposApiException zae) {
        }
    }

    private String parseFile(String partialPath) throws IOException {
        String fullPath = getClass().getResource(partialPath).getPath();
        String content = FileUtils.readFileToString(new File(fullPath), "utf-8");
        return content;
    }
}
