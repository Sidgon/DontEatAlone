package ch.mse.dea.donteatalone.Objects;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import ch.mse.dea.donteatalone.R;

public class EventValidation {

    public static String standart(String str,int minLength) {

        if (TextUtils.isEmpty(str)) {
            return App.getFromResource(R.string.user_validation_error_required) + ".";
        } else if (str.length() < minLength) {
            return App.getFromResource(R.string.user_validation_error_length, minLength)+".";
        }

        return null;
    }

}
