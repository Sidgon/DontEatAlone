package ch.mse.dea.donteatalone.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import ch.mse.dea.donteatalone.Objects.App;
import ch.mse.dea.donteatalone.Objects.User;
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
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register_with_email);
        getViews();

        if (App.getDebug()) setViewValues();

        //set onclicklistener on sign in button
        findViewById(R.id.registerSignUpButton).setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void setViewValues() {
        mUsernameTextField.setText("Command1991");
        mEmailTextField.setText("steinegger.daniel@gmail.com");
        mFirstNameTextField.setText("Daniel");
        mLastNameTextField.setText("Steinegger");
        mPasswordTextField.setText("Password1");
        mPasswordRepeatTextField.setText("Password1");
    }

    private void getViews() {
        mUsernameTextField = findViewById(R.id.registerUsernameTextField);
        mEmailTextField = findViewById(R.id.registerEmailTextField);
        mFirstNameTextField = findViewById(R.id.registerFirstNameTextField);
        mLastNameTextField = findViewById(R.id.registerLastNameTextField);
        mPasswordTextField = findViewById(R.id.registerPasswordTextField);
        mPasswordRepeatTextField = findViewById(R.id.registerPasswordRepeatTextField);
    }

    public void signUp() {

        // get all user attributes from textfields

        String username = mUsernameTextField.getText().toString();
        String email = mEmailTextField.getText().toString();
        String firstname = mFirstNameTextField.getText().toString();
        String lastname = mLastNameTextField.getText().toString();
        String password = mPasswordTextField.getText().toString();
        String passwordRepeat = mPasswordRepeatTextField.getText().toString();


        if (!validateRegisterForm(username, email, firstname,
                lastname, password, passwordRepeat)) {
            return;
        }

        signUpFirebaseAuthUser(email, password);


    }

    public void signUpFirebaseAuthUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //start listening for user changes in realtime db
                            //Register User in Real-Time DB
                            signUpFirebaseRealTimeDBUser(user.getUid(),
                                    mUsernameTextField.getText().toString(),
                                    mEmailTextField.getText().toString(),
                                    mFirstNameTextField.getText().toString(),
                                    mLastNameTextField.getText().toString());

                            Intent nextIntent = new Intent(RegisterWithEmailActivity.this, MainActivity.class);
                            nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            RegisterWithEmailActivity.this.startActivity(nextIntent);

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
                                             String lastname) {
        User user = new User(userId, username, firstname, lastname,email, User.getGravatar(email));
        mDatabase.child("users").child(userId).setValue(user)
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
        String str;
        boolean valid = true;

        str = UserValidation.username(username);
        if (str != null) valid = false;
        mUsernameTextField.setError(str);

        str = UserValidation.email(email);
        if (str != null) valid = false;
        mEmailTextField.setError(str);

        str = UserValidation.firstname(firstname);
        if (str != null) valid = false;
        mFirstNameTextField.setError(str);

        str = UserValidation.lastname(lastname);
        if (str != null) valid = false;
        mLastNameTextField.setError(str);

        str = UserValidation.password(this, password, passwordrepeat, true);
        if (str != null) valid = false;
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
