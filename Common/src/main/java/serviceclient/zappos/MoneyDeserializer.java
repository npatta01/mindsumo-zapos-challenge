package serviceclient.zappos;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.lang.Float;import java.lang.Number;import java.lang.Override;import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Converts money to double
 */
public class MoneyDeserializer extends JsonDeserializer<Float> {
    /**
     * Deserialize money to double
     * @param jsonParser
     * @param deserializationContext
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */

    /**
     *  If value is already double, return else parse using local currency
     * @param jsonParser
     * @param deserializationContext
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public Float deserialize(org.codehaus.jackson.JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        Number nbr = null;
        String text=jsonParser.getText();
        Float value;
        try {
            value=Float.parseFloat(text);
            return value;
        }catch(NumberFormatException nfe){
            try {
                nbr = fmt.parse(text);
                value = (float) nbr.floatValue();
                return value;
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

        }


          return 0f;
    }
}
