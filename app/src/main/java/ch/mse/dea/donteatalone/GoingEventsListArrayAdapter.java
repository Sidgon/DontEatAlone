package ch.mse.dea.donteatalone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class GoingEventsListArrayAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final Event[] events;

    public GoingEventsListArrayAdapter(Context context, Event[] events) {
        super(context,R.layout.activity_going_events_list_item, events);

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
                eventView = view.inflate(R.layout.activity_going_events_list_item,parent,false);
            }
        }

        Event event=events[position];
        if (event !=null){
            TextView eventName= eventView.findViewById(R.id.going_event_name);
            TextView eventDate= eventView.findViewById(R.id.going_event_date);
            TextView eventTime=eventView.findViewById(R.id.going_event_time);
            TextView eventAddresse=eventView.findViewById(R.id.going_event_addrasse);
            TextView eventCity=eventView.findViewById(R.id.going_event_postcode_city);

            eventName.setText(event.getRestaurantName());
            setDate(eventDate,event.getDate());
            setTime(eventTime,event.getDate(),event.getDuration());
            eventAddresse.setText(event.getAddrasse());
            eventCity.setText(event.getPostcode()+" "+event.getCity());
        }

        return eventView;

    }


    private static void setTime(TextView view, DateTime date, int duration){
        DateTime enddate= date.plusMinutes(duration);

        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        String starttime = fmt.print(date);
        String endtime = fmt.print(enddate);

        view.setText(starttime+"-"+endtime);

    }

    private static void setDate(TextView view, DateTime date){
        date.toLocalDate();

        DateTimeFormatter fmt = DateTimeFormat.forPattern("EEEE, dd.MM.yyyy").withLocale(Locale.getDefault());
        String str = fmt.print(date);

        view.setText(str);

    }
}
