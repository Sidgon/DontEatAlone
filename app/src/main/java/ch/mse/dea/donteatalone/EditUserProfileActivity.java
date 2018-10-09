package ch.mse.dea.donteatalone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class EditUserProfileActivity extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtEmail;
    private EditText txtFirstname;
    private EditText txtLastname;
    private CheckBox checkbox_changePassword;
    private EditText txtEnterPassword;
    private EditText txtRepeatPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getViews();
        registerListener();

        setContentView(R.layout.activity_edit_user_profile);
    }

    private void getViews(){
        checkbox_changePassword = findViewById(R.id.checkbox_changePassword);
        txtUsername = findViewById(R.id.txtUsername);
        txtFirstname = findViewById(R.id.txtFirstname);
        txtLastname = findViewById(R.id.txtLastname);
        txtEmail = findViewById(R.id.txtEmail);
        txtEnterPassword = findViewById(R.id.txtEnterPassword);
        txtRepeatPassword = findViewById(R.id.txtRepeatPassword);
    }

    private void registerListener() {

        checkbox_changePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            txtEnterPassword.setVisibility(View.VISIBLE);
                            txtRepeatPassword.setVisibility(View.VISIBLE);
                        }else {
                            txtEnterPassword.setVisibility(View.GONE);
                            txtRepeatPassword.setVisibility(View.GONE);
                        }
                    }
        }
        );
    }

}
