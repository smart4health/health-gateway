package pt.uninova.s4h.hub.kbz.senml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Implements an auxiliary class to make the senml json conversion.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 07 September 2019 - First version.
 */
public class MedxGsonUtil {

    //private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private Gson gson = null;

    public MedxGsonUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        //gsonBuilder.setDateFormat(DATE_FORMAT);
        this.gson = gsonBuilder.create();
    }

    /**
     * public static String getDateFormat() { return DATE_FORMAT; }
    *
     */
//    public ErJson fromJson(String jsonString) {
//        return gson.fromJson(jsonString, ErJson.class);
//    }
//    public String toJson(ErJson erMessage) {
//        return gson.toJson(erMessage, ErJson.class);
//    }
    public MedxEvent[] fromJson(String jsonString) {
        return gson.fromJson(jsonString, MedxEvent[].class);
    }

    public String toJson(MedxEvent[] medxEvents) {
        return gson.toJson(medxEvents, MedxEvent[].class);
    }
}
