package ch.mse.dea.donteatalone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;

import ch.mse.dea.donteatalone.DataHandling.DataFormatter;
import ch.mse.dea.donteatalone.Objects.Event;
import ch.mse.dea.donteatalone.R;

public class EventsListArrayAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final ArrayList<Event> events;

    public EventsListArrayAdapter(Context context, ArrayList<Event> events) {
        super(context,R.layout.activity_events_list_item, events);

        this.context=context;
        this.events=events;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View eventView=convertView;
        if (convertView == null){
            LayoutInflater view= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view !=null){
                eventView = view.inflate(R.layout.activity_events_list_item,parent,false);
            }
        }

        Event event=events.get(position);
        if (event !=null){
            TextView eventName= eventView.findViewById(R.id.going_event_name);
            TextView eventDate= eventView.findViewById(R.id.going_event_date);
            TextView eventTime=eventView.findViewById(R.id.going_event_time);
            TextView eventAddresse=eventView.findViewById(R.id.going_event_addrasse);
            TextView eventCity=eventView.findViewById(R.id.going_event_postcode_city);

            TextView eventGoingGuests=eventView.findViewById(R.id.going_event_coming_guests);
            TextView eventMaxGuests=eventView.findViewById(R.id.going_event_max_guests);

            eventName.setText(event.getEventName());
            setDate(eventDate,event.getDate());
            setTime(eventTime,event.getDate(),event.getDuration());
            eventAddresse.setText(event.getAddresse());
            eventCity.setText(event.getPostcode()+" "+event.getCity());
            eventGoingGuests.setText(event.getGoingGuests()+"");
            eventMaxGuests.setText(event.getMaxGuest()+"");
        }

        return eventView;

    }


    private static void setTime(TextView view, DateTime date, int duration){
        DateTime enddate= date.plusMinutes(duration);

        String starttime = DataFormatter.getTimeAsString(date);
        String endtime = DataFormatter.getTimeAsString(enddate);

        String str=starttime+"-"+endtime;
        view.setText(str);

    }

    private static void setDate(TextView view, DateTime date){
        view.setText(DataFormatter.getDateAsString(date,"long"));
    }
}
