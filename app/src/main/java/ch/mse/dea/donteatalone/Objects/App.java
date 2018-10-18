package ch.mse.dea.donteatalone.Objects;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        context=this;

    }

    public static String getFromResource(int i) {
        return context.getResources().getString(i);
    }

    public static String getFromResource(int i, Object... object) {
        return context.getResources().getString(i,object);
    }
}
