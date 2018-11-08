package ch.mse.dea.donteatalone.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.mse.dea.donteatalone.objects.User;
import ch.mse.dea.donteatalone.R;

public class UsersListArrayAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final ArrayList<User> users;

    public UsersListArrayAdapter(Context context, ArrayList<User> events) {
        super(context,R.layout.activity_going_user_to_event_item, events);

        this.context=context;
        this.users=events;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View eventView=convertView;
        if (convertView == null){
            LayoutInflater view= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view !=null){
                eventView = view.inflate(R.layout.activity_going_user_to_event_item,parent,false);
            }
        }

        User user=users.get(position);
        if (user !=null && eventView!=null){
            ImageView imageView = eventView.findViewById(R.id.eventUsers_user_profile_image);
            TextView txtUsername= eventView.findViewById(R.id.eventUsers_user_name);

            if (user.getImage() != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
                imageView.setImageBitmap(bmp);
            }
            txtUsername.setText(user.getUsername());
        }

        return eventView;

    }

}
