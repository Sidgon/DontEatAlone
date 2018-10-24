package ch.mse.dea.donteatalone.Objects;

import android.app.Application;
import android.content.Context;
import android.util.Log;

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
    public static boolean getDebug(){
        return true;
    }

    public static void print(String str){
        System.out.println("-");
        System.out.println("------------------------");
        System.out.println(str);
        System.out.println("------------------------");
    }

    public static void log(String TAG,String str){
        Log.v(TAG,"-");
        Log.v(TAG,"------------------------");
        Log.v(TAG,str);
        Log.v(TAG,"------------------------");
    }
}
