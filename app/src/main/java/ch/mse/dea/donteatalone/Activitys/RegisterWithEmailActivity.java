package ch.mse.dea.donteatalone.Activitys;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.mse.dea.donteatalone.Objects.UserValidation;
import ch.mse.dea.donteatalone.R;


public class RegisterWithEmailActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

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
        mAuth = FirebaseAuth.getInstance();
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
        signUpFirebaseAuthUser(email, password);

        //create user in realtime db with all attributes
        //signUpFirebaseRealTimeDBUser(username, email, firstname, lastname, password);

    }

    public void signUpFirebaseAuthUser(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //-> change intent here
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterWithEmailActivity.this, "Authentication failed." +
                                    "please contact an administrator",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void signUpFirebaseRealTimeDBUser(String username, String email, String firstname,
                                String lastname, String password){


    }

    private boolean validateRegisterForm(String username, String email, String firstname,
                                         String lastname, String password, String passwordrepeat) {
        String str;
        boolean valid=true;

        str=UserValidation.username(username);
        if (str!=null)valid=false;
        mUsernameTextField.setError(str);

        str=UserValidation.email(email);
        if (str!=null)valid=false;
        mEmailTextField.setError(str);

        str=UserValidation.firstname(firstname);
        if (str!=null)valid=false;
        mFirstNameTextField.setError(str);

        str=UserValidation.lastname(lastname);
        if (str!=null)valid=false;
        mLastNameTextField.setError(str);

        str=UserValidation.password(this,password,passwordrepeat,true);
        if (str!=null)valid=false;
        mPasswordTextField.setError(str);
        mPasswordRepeatTextField.setError(str);

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
