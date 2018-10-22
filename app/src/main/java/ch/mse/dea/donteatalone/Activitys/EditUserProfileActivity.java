package ch.mse.dea.donteatalone.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.Objects.User;
import ch.mse.dea.donteatalone.Objects.UserValidation;
import ch.mse.dea.donteatalone.R;

public class EditUserProfileActivity extends AppCompatActivity {
    private static final String TAG = EditUserProfileActivity.class.getName();

    private EditText txtUsername;
    private EditText txtEmail;
    private EditText txtFirstname;
    private EditText txtLastname;
    private CheckBox checkbox_changePassword;
    private EditText txtOldPassword;
    private EditText txtEnterPassword;
    private EditText txtRepeatPassword;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refEvents = mDatabase.child("events");
    private DatabaseReference refUsers = mDatabase.child("users");
    private DatabaseReference refEventUsers = mDatabase.child("event_users");
    private DatabaseReference refUsersEvents = mDatabase.child("users_events");
    private DatabaseReference refUsersGoingEvents = mDatabase.child("users_going_events");
    private FirebaseAuth mAuth;

    private User user;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        getViews();
        registerListener();

        if (getIntent().getExtras() != null) {
            Gson gson = GsonAdapter.getGson();
            String json = getIntent().getExtras().getString(R.string.intent_edit_user_profile_user + "");
            user = gson.fromJson(json, User.class);
            setViewValues(user);
        } else {
            Log.d(TAG, "User couldn't get from Intent");
            finish();
        }


    }

    private User getViewValues() {

        String username = txtUsername.getText().toString();
        String firstname = txtFirstname.getText().toString();
        String lastname = txtLastname.getText().toString();
        String email = txtEmail.getText().toString();
        String oldPassword = txtOldPassword.getText().toString();
        String password = txtEnterPassword.getText().toString();
        String passwordRepeated = txtRepeatPassword.getText().toString();

        if (!validateRegisterForm(username, email, firstname, lastname, password, passwordRepeated, oldPassword))
            return null;

        /*
        if (!checkbox_changePassword.isSelected()){
            password=user.getPassswordHash();
        }
        */
        //TODO altes passwort
        return new User(
                user.getuserId(),
                username,
                firstname,
                lastname,
                email,
                user.getImage()
        );

    }

    private void setViewValues(User user) {
        txtUsername.setText(user.getUsername());
        txtFirstname.setText(user.getFirstname());
        txtLastname.setText(user.getLastname());
        txtEmail.setText(user.getEmail());
    }

    private void getViews() {
        checkbox_changePassword = findViewById(R.id.checkbox_changePassword);
        txtUsername = findViewById(R.id.txtUsername);
        txtFirstname = findViewById(R.id.txtFirstname);
        txtLastname = findViewById(R.id.txtLastname);
        txtEmail = findViewById(R.id.txtEmail);
        txtOldPassword = findViewById(R.id.txtOldPassword);
        txtEnterPassword = findViewById(R.id.txtEnterPassword);
        txtRepeatPassword = findViewById(R.id.txtRepeatPassword);

    }

    private void registerListener() {

        checkbox_changePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                               @Override
                                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                   if (isChecked) {
                                                                       txtEnterPassword.setVisibility(View.VISIBLE);
                                                                       txtRepeatPassword.setVisibility(View.VISIBLE);
                                                                       txtOldPassword.setVisibility(View.VISIBLE);
                                                                   } else {
                                                                       txtEnterPassword.setVisibility(View.GONE);
                                                                       txtRepeatPassword.setVisibility(View.GONE);
                                                                       txtOldPassword.setVisibility(View.GONE);
                                                                   }
                                                               }
                                                           }
        );
    }

    private boolean validateRegisterForm(String username, String email, String firstname,
                                         String lastname, String password, String passwordrepeat, String oldpassword) {
        String str;
        boolean valid = true;

        str = UserValidation.username(username);
        if (str != null) valid = false;
        txtUsername.setError(str);

        str = UserValidation.email(email);
        if (str != null) valid = false;
        txtEmail.setError(str);

        str = UserValidation.firstname(firstname);
        if (str != null) valid = false;
        txtFirstname.setError(str);

        str = UserValidation.lastname(lastname);
        if (str != null) valid = false;
        txtLastname.setError(str);

        if (checkbox_changePassword.isSelected()) {

            if (true) {
                //TODO Check in firebase if old password is correct


                str = UserValidation.password(this, password, passwordrepeat, true);
                if (str != null) valid = false;
                txtEnterPassword.setError(str);
                txtRepeatPassword.setError(str);


            } else {
                txtOldPassword.setError(getResources().getString(R.string.user_validation_error_passwords_wrong_password));
                valid = false;
            }
        }

        return valid;
    }

    public void onClick_saveEvent(View view) {
        User user = getViewValues();

        if (user == null) return;

        //TODO save user Data

        finish();

    }

    @Override
    public void onBackPressed() {

        if (!user.haveSameContent(getViewValues())) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle(R.string.edit_create_event_dialog_title);

            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.edit_create_event_dialog_massage)
                    .setCancelable(false)
                    .setPositiveButton(R.string.edit_create_event_dialog_cancel_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.edit_create_event_dialog_delete_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i(TAG, "User not saved");
                            finish();
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            finish();
        }
    }

    public void onClick_deleteEvent(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(R.string.edit_create_event_dialog_onDelete_title);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.edit_create_event_dialog_onDelete_massage)
                .setCancelable(false)
                .setPositiveButton(R.string.edit_create_event_dialog_cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.edit_create_event_dialog_delete_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (user != null) {


                            FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                            fuser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    refUsers.child(user.getuserId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Remove User from all going events and delete al going events of User
                                            refUsersGoingEvents.child(user.getuserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        if (snapshot != null && snapshot.getKey() != null) {
                                                            refEventUsers.child(snapshot.getKey()).child(user.getuserId()).removeValue();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            refUsersGoingEvents.child(user.getuserId()).removeValue();

                                            //Delete all Events of User
                                            refUsersEvents.child(user.getuserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        if (snapshot != null && snapshot.getKey() != null) {
                                                            refEvents.child(snapshot.getKey()).removeValue();
                                                            refEventUsers.child(snapshot.getKey()).removeValue();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            refUsersEvents.child(user.getuserId()).removeValue();


                                            finish();
                                        }
                                    })
                                            .addOnFailureListener(
                                                    new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("DeleteEvent:failure", e);
                                                            Toast.makeText(EditUserProfileActivity.this,
                                                                    getString(R.string.edit_user_profile_error_deleting_event),
                                                                    Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                            );

                                }
                            });


                            Log.i(TAG, "Account gelöscht: \n   -ID: " + user.getuserId() + " \n   -Name: " + user.getEmail());
                            dialog.cancel();
                        }
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
}
