package ch.mse.dea.donteatalone.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import ch.mse.dea.donteatalone.objects.App;
import ch.mse.dea.donteatalone.objects.UserValidation;
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
        setContentView(R.layout.activity_login);
        getViews();

        if(App.getDebug()) setDummyData();

        //set onclicklistener on sign in button
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.emailRegisterButton).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        loginCheck();
    }

    private void setDummyData(){
        mEmailTextField.setText("steinegger.daniel@gmail.com");
        mPasswordTextField.setText("Password1");
    }
    private void getViews(){
        mEmailTextField = findViewById(R.id.emailLoginTextField);
        mPasswordTextField = findViewById(R.id.passwordLoginTextField);
    }

    private void signIn() {
        //get username and login from textbox

        String email = mEmailTextField.getText().toString();
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

                            if (user!=null) {
                                //start new intent
                                intentToMain();

                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;
        String str;

        String email = mEmailTextField.getText().toString();
        str=UserValidation.email(email);
        if (str!=null)valid=false;
        mEmailTextField.setError(str);

        String password = mPasswordTextField.getText().toString();
        str=UserValidation.password(this,password,false);
        if (str!=null)valid=false;
        mPasswordTextField.setError(str);

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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        System.exit(0);
    }

    private void loginCheck() {
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && currentUser.getEmail()!=null) {
            mAuth.fetchSignInMethodsForEmail(currentUser.getEmail()).addOnSuccessListener(new OnSuccessListener<SignInMethodQueryResult>() {
                @Override
                public void onSuccess(SignInMethodQueryResult signInMethodQueryResult) {
                    if (signInMethodQueryResult!=null && signInMethodQueryResult.getSignInMethods()!=null && !signInMethodQueryResult.getSignInMethods().isEmpty()) {
                        App.log(TAG,"login check true");
                        intentToMain();
                    }
                }
            });

        }
    }

    private void intentToMain() {
        Intent nextIntent = new Intent(this, MainActivity.class);
        nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(nextIntent);
        finish();

    }
}
