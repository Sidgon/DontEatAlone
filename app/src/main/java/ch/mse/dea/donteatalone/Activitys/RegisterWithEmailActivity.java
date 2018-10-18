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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ch.mse.dea.donteatalone.Objects.User;
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
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register_with_email);
        //set onclicklistener on sign in button
        findViewById(R.id.registerSignUpButton).setOnClickListener(this);
        //init db ref
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                            //Register User in Real-Time DB
                            signUpFirebaseRealTimeDBUser(user.getUid(),
                                    mUsernameTextField.getText().toString(),
                                    mEmailTextField.getText().toString(),
                                    mFirstNameTextField.getText().toString(),
                                    mLastNameTextField.getText().toString());
                            //change activity here

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterWithEmailActivity.this, "Authentication failed." +
                                    task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void signUpFirebaseRealTimeDBUser(String userId, String username, String email, String firstname,
                                String lastname){
        User user = new User(userId, username, email, firstname, lastname);
        mDatabase.child("users").child(user.getuserId()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        //change intent here
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "createUserWithEmail:failure", e);
                        Toast.makeText(RegisterWithEmailActivity.this,
                                "Registering has failed, please try again later" +
                                     e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

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
