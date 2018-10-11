package ch.mse.dea.donteatalone.Adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import ch.mse.dea.donteatalone.Adapter.DateTimeTypeAdapter;

public class GsonAdapter {


    public static Gson getGson(){
        return new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter()).create();
    }

}
