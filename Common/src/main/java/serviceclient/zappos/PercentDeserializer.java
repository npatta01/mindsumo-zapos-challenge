package serviceclient.zappos;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.lang.Float;import java.lang.Number;import java.lang.Override;import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Convert percent to float
 */
public class PercentDeserializer extends JsonDeserializer<Float> {
    /**
     * try parsing value as float, else use locl percent settings
     * @param jsonParser
     * @param deserializationContext
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public Float deserialize(org.codehaus.jackson.JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

       Float value;

        NumberFormat fmt = NumberFormat.getPercentInstance();
        Number nbr = null;
        String percent =jsonParser.getText();
        try {
           value =Float.parseFloat(percent) ;
            return value;
        }catch(NumberFormatException e){
            try {
                nbr = fmt.parse(percent);
                value=(float) nbr.floatValue();
                return value ;
            } catch (ParseException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        return 0f;
    }
}
