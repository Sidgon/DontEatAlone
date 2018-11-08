package ch.mse.dea.donteatalone.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

public class GsonAdapter {

    private GsonAdapter() {
    }

    public static Gson getGson(){
        return new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter()).create();
    }

}
