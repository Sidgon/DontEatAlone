package ch.mse.dea.donteatalone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class RegisterWithEmailActivity extends AppCompatActivity implements
        View.OnClickListener {

    private EditText mUsernameTextField;
    private EditText mEmailTextField;
    private EditText mFirstNameTextField;
    private EditText mLastNameTextField;
    private EditText mPasswordTextField;
    private EditText mPasswordRepeatTextField;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with_email);
        //set onclicklistener on sign in button
        findViewById(R.id.registerSignUpButton).setOnClickListener(this);
    }

    public void signUp(){

        // get all user attributes from textfields
        mUsernameTextField = findViewById(R.id.registerUsernameTextField);
        String username = mUsernameTextField.getText().toString();

        mEmailTextField= findViewById(R.id.registerEmailTextField);
        String email = mEmailTextField.getText().toString();

        mFirstNameTextField= findViewById(R.id.registerFirstNameTextField);
        String firstname = mFirstNameTextField.getText().toString();

        mLastNameTextField= findViewById(R.id.registerLastNameTextField);
        String lastname = mLastNameTextField.getText().toString();

        mPasswordTextField= findViewById(R.id.registerPasswordRepeatTextField);
        String password = mPasswordTextField.getText().toString();

        mPasswordRepeatTextField= findViewById(R.id.registerPasswordRepeatTextField);
        String passwordRepeat = mPasswordRepeatTextField.getText().toString();

        //validate form
        if (!validateRegisterForm(username, email, firstname,
                lastname, password, passwordRepeat)) {
            return;
        }

        //create user for firebase auth

        //create user in realtime db with all attributes

    }

    public void signUpFirebaseAuthUser(String email, String password){

    }

    public void signUpFirebaseRealTimeDBUser(String Username, String email, String firstname,
                                String lastname, String password){

    }

    private boolean validateRegisterForm(String username, String email, String firstname,
                                         String lastname, String password, String passwordrepeat) {
        boolean valid = true;

        //checks if fields are not empty
        if (TextUtils.isEmpty(username)) {
            mUsernameTextField.setError("Required.");
            valid = false;
        } else {
            mUsernameTextField.setError(null);
        }
        if (TextUtils.isEmpty(email)) {
            mEmailTextField.setError("Required.");
            valid = false;
        } else {
            mEmailTextField.setError(null);
        }
        if (TextUtils.isEmpty(firstname)) {
            mFirstNameTextField.setError("Required.");
            valid = false;
        } else {
            mFirstNameTextField.setError(null);
        }
        if (TextUtils.isEmpty(lastname)) {
            mLastNameTextField.setError("Required.");
            valid = false;
        } else {
            mLastNameTextField.setError(null);
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordTextField.setError("Required.");
            valid = false;
        } else {
            mPasswordTextField.setError(null);
        } if (TextUtils.isEmpty(passwordrepeat)) {
            mPasswordRepeatTextField.setError("Required.");
            valid = false;
        } else {
            mPasswordRepeatTextField.setError(null);
        }

        //checks if entered email is valid and not null
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailTextField.setError("Please enter a valid email");
            valid = false;
            if(TextUtils.isEmpty(email)){
                mEmailTextField.setError("Required.");
                valid = false;
            }
        } else {
            mEmailTextField.setError(null);
        }

        //check if both password match each other
        if(!password.equals(passwordrepeat) && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(passwordrepeat)) {
            mPasswordTextField.setError("Passwords do not match");
            mPasswordRepeatTextField.setError("Passwords do not match");
            valid = false;
        }   else {
            if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordrepeat)) {
                mPasswordTextField.setError(null);
                mPasswordRepeatTextField.setError(null);
            }
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.registerSignUpButton) {
            signUp();
        }
    }
}
