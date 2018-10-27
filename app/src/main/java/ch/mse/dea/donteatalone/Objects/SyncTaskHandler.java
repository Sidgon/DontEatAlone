package ch.mse.dea.donteatalone.Objects;

import java.util.ArrayList;

public abstract class SyncTaskHandler {

    private int counter;


    public SyncTaskHandler() {
        counter=0;
    }




    public void addEvent(){
        counter++;
    }

    public void removeEvent(){
        counter--;

        if (counter==0){
            onFinish();
        }
    }

    abstract public void onFinish();

}
