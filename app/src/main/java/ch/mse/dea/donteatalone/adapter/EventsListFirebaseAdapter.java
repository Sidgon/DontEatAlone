package ch.mse.dea.donteatalone.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;

import org.joda.time.DateTime;

import ch.mse.dea.donteatalone.datahandling.DataFormatter;
import ch.mse.dea.donteatalone.objects.Event;
import ch.mse.dea.donteatalone.R;

public class EventsListFirebaseAdapter extends FirebaseListAdapter<Event> {

    public EventsListFirebaseAdapter(@NonNull FirebaseListOptions<Event> options) {
        super(options);
    }

    private static void setTime(TextView view, DateTime date, int duration) {
        DateTime enddate = date.plusMinutes(duration);

        String starttime = DataFormatter.getTimeAsString(date);
        String endtime = DataFormatter.getTimeAsString(enddate);

        String str = starttime + "-" + endtime;
        view.setText(str);

    }

    private static void setDate(TextView view, DateTime date) {
        view.setText(DataFormatter.getDateAsString(date, "long"));
    }

    @Override
    protected void populateView(@NonNull View convertView, @NonNull Event event, int position) {
        TextView eventName = convertView.findViewById(R.id.going_event_name);
        TextView eventDate = convertView.findViewById(R.id.going_event_date);
        TextView eventTime = convertView.findViewById(R.id.going_event_time);
        TextView eventAddresse = convertView.findViewById(R.id.going_event_addrasse);
        TextView eventCity = convertView.findViewById(R.id.going_event_postcode_city);
        TextView eventCountry= convertView.findViewById(R.id.going_event_country);

        TextView eventGoingGuests = convertView.findViewById(R.id.going_event_coming_guests);
        TextView eventMaxGuests = convertView.findViewById(R.id.going_event_max_guests);

        eventName.setText(event.getEventName());
        setDate(eventDate, event.getDateTime());
        setTime(eventTime, event.getDateTime(), event.getDuration());
        eventAddresse.setText(event.getAddress().getAddress());
        eventCity.setText(event.getAddress().getPostcode() + " " + event.getAddress().getPostcode());
        eventGoingGuests.setText(event.getGoingGuests() + "");
        eventMaxGuests.setText(event.getMaxGuest() + "");
        eventCountry.setText(event.getAddress().getCountry());
    }


}
