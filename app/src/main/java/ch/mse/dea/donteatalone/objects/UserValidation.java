package ch.mse.dea.donteatalone.objects;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import ch.mse.dea.donteatalone.R;

public class UserValidation {
    private static final String TAG= UserValidation.class.getName();

    public static String username(String username) {

        if (TextUtils.isEmpty(username)) {
            return App.getFromResource(R.string.user_validation_error_required) + ".";
        } else if (username.length() < 4) {
            return App.getFromResource(R.string.user_validation_error_length, 4)+".";
        }

        return null;
    }

    public static String firstname(String firstname) {
        if (TextUtils.isEmpty(firstname)) {
            return App.getFromResource(R.string.user_validation_error_required) + ".";
        } else if (firstname.length() < 4) {
            return App.getFromResource(R.string.user_validation_error_length, 4)+".";
        }
        return null;
    }

    public static String lastname(String lastname) {
        if (TextUtils.isEmpty(lastname)) {
            return App.getFromResource(R.string.user_validation_error_required) + ".";
        } else if (lastname.length() < 4) {
            return App.getFromResource(R.string.user_validation_error_length, 4)+".";
        }
        return null;
    }

    public static String email(String email) {

        if (TextUtils.isEmpty(email)) {
            return App.getFromResource(R.string.user_validation_error_required) + ".";
        } else if (email.length() < 4) {
            return App.getFromResource(R.string.user_validation_error_length, 4);
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return App.getFromResource(R.string.user_validation_error_valid_email)+".";
        }
        return null;
    }

    public static String password(Context context,String password,boolean showToasts) {
        if (TextUtils.isEmpty(password)) {
            return App.getFromResource(R.string.user_validation_error_required) + ".";
        } else if (!checkPasswordSecurity(context,password,showToasts)) {
            return App.getFromResource(R.string.user_validation_error_valid_password)+".";
        }
        return null;
    }

    public static String password(Context context, String password, String repeatedPassword,boolean showToasts){

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatedPassword)) {
            return App.getFromResource(R.string.user_validation_error_required) + ".";
        } else if (!checkPasswordSecurity(context,password,showToasts)) {
            return App.getFromResource(R.string.user_validation_error_valid_password)+".";
        }else if (!password.equals(repeatedPassword)){
            return App.getFromResource(R.string.user_validation_error_passwords_not_match)+".";
        }

        return null;
    }

    public static boolean checkPasswordSecurity(Context context, String password, boolean showToasts){

        if(!password.matches(".*\\d+.*") ||
                !password.matches(".*[a-z]+.*") ||
                !password.matches(".*[A-Z]+.*") ||
                password.length()<6){

            if (showToasts) {
                Toast toast = Toast.makeText(context, R.string.error_password_wrong_pattern, Toast.LENGTH_LONG*2);
                toast.show();
            }
        }else if(password.contains("AND") || password.contains("NOT")){
            if (showToasts) {
                Toast toast = Toast.makeText(context, R.string.error_password_contains_conditions, Toast.LENGTH_LONG);
                toast.show();
            }
        }else {
            return true;
        }

        return false;
    }

    public static String notEmpty(String str,int minLength) {

        if (TextUtils.isEmpty(str)) {
            return App.getFromResource(R.string.user_validation_error_required) + ".";
        } else if (minLength>0 && str.length() < minLength) {
            return App.getFromResource(R.string.user_validation_error_length, minLength)+".";
        }

        return null;
    }



}
