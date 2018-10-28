package ch.mse.dea.donteatalone.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import ch.mse.dea.donteatalone.Objects.App;
import ch.mse.dea.donteatalone.Objects.User;
import ch.mse.dea.donteatalone.Objects.UserValidation;
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
        getViews();

        if(App.getDebug()) setDummyData();

        //set onclicklistener on sign in button
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.emailRegisterButton).setOnClickListener(this);
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
                                Intent nextIntent = new Intent(LoginActivity.this, MainActivity.class);
                                nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                LoginActivity.this.startActivity(nextIntent);
                                finish();
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
}
