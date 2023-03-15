package pt.uninova.s4h.healthgateway.senml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Implements an auxiliary class to make the senml json conversion.
 */
public class MedxGsonUtil {

    private Gson gson = null;

    public MedxGsonUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeSpecialFloatingPointValues();
        this.gson = gsonBuilder.create();
    }
    
    public MedxEvent[] fromJson(String jsonString) {
        return gson.fromJson(jsonString, MedxEvent[].class);
    }

    public String toJson(MedxEvent[] medxEvents) {
        return gson.toJson(medxEvents, MedxEvent[].class);
    }
}
