package ch.mse.dea.donteatalone.Activitys;

import android.content.Intent;
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

import ch.mse.dea.donteatalone.R;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private EditText mEmailTextField;
    private EditText mPasswordTextField;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        //set onclicklistener on sign in button
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signIn() {
        //get username and login from textbox
        mEmailTextField = findViewById(R.id.emailLoginTextField);
        String email = mEmailTextField.getText().toString();

        mPasswordTextField= findViewById(R.id.passwordLoginTextField);
        String password = mPasswordTextField.getText().toString();

        Log.d(TAG, "signIn:" + email);

        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText("authentication failed");
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        //checks if fields are not empty
        String email = mEmailTextField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailTextField.setError("Required.");
            valid = false;
        } else {
            mEmailTextField.setError(null);
        }

        String password = mPasswordTextField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordTextField.setError("Required.");
            valid = false;
        } else {
            mPasswordTextField.setError(null);
        }

        //checks if entered email is valid
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailTextField.setError("Please enter a valid email");
            valid = false;
        } else {
            mEmailTextField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.emailSignInButton) {
            signIn();
        } else if (i == R.id.emailRegisterButton) {
            //start new intent
            Intent nextIntent = new Intent(LoginActivity.this, RegisterWithEmailActivity.class);
            LoginActivity.this.startActivity(nextIntent);
        }
    }
}
