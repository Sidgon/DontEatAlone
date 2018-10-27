package ch.mse.dea.donteatalone.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ch.mse.dea.donteatalone.Adapter.GsonAdapter;
import ch.mse.dea.donteatalone.Objects.App;
import ch.mse.dea.donteatalone.Objects.SyncTaskHandler;
import ch.mse.dea.donteatalone.Objects.User;
import ch.mse.dea.donteatalone.Objects.UserValidation;
import ch.mse.dea.donteatalone.R;

public class EditUserProfileActivity extends AppCompatActivity {
    private static final String TAG = EditUserProfileActivity.class.getName();

    private EditText txtUsername;
    private EditText txtFirstname;
    private EditText txtLastname;

    private Context context;

    private DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refUsers= mDatabase.child("users");

    private Map<String, Object> map = new HashMap<>();

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        context = this;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser!=null) {
            App.log(TAG,"Im in on Start");
            getViews();

            if (getIntent().getExtras() != null) {
                Gson gson = GsonAdapter.getGson();
                String json = getIntent().getExtras().getString(R.string.intent_edit_user_profile_user + "");
                user = gson.fromJson(json, User.class);
                setViewValues(user);
            } else {
                Log.d(TAG, "Couldn't extract User from Intent");
                finish();
            }
        }else {
            Toast.makeText(this,R.string.user_not_logedin,Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private User getViewValues() {

        String username = txtUsername.getText().toString();
        String firstname = txtFirstname.getText().toString();
        String lastname = txtLastname.getText().toString();

        return new User(
                user.getUserId(),
                username,
                firstname,
                lastname,
                user.getEmail(),
                user.getImage()
        );

    }

    private void setViewValues(User user) {
        txtUsername.setText(user.getUsername());
        txtFirstname.setText(user.getFirstname());
        txtLastname.setText(user.getLastname());
    }

    private void getViews() {
        txtUsername = findViewById(R.id.txtUsername);
        txtFirstname = findViewById(R.id.txtFirstname);
        txtLastname = findViewById(R.id.txtLastname);
    }


    private boolean validateRegisterForm(String username, String firstname,
                                         String lastname) {
        String str;
        boolean valid = true;

        str = UserValidation.username(username);
        if (str != null) valid = false;
        txtUsername.setError(str);

        str = UserValidation.firstname(firstname);
        if (str != null) valid = false;
        txtFirstname.setError(str);

        str = UserValidation.lastname(lastname);
        if (str != null) valid = false;
        txtLastname.setError(str);

        return valid;
    }

    public void onClick_saveEvent(View view) {
        User user = getViewValues();

        if (validateRegisterForm(user.getUsername(), user.getFirstname(), user.getLastname())) {
            refUsers.child(user.getUserId()).updateChildren(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(R.string.edit_user_profile_error_updating_user);
                }
            });
        } else {
            showToast(R.string.edit_user_profile_error_updating_user);
        }

    }

    private void showToast(int resourceStringId) {
        Toast.makeText(this, resourceStringId, Toast.LENGTH_LONG).show();
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
                        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null && fuser!=null) {

                        refUsers.child(user.getUserId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                    fuser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            App.log(TAG, "Account gelöscht: \n   -ID: " + user.getUserId() + " \n   -Name: " + user.getEmail());
                                            loginIntent();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            App.log(TAG,"DeleteEvent:failure");
                                            showToast(R.string.edit_user_profile_error_deleting_event);
                                        }
                                    });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                App.log(TAG, "DeleteEvent:failure no User");
                                showToast(R.string.edit_user_profile_error_deleting_event);
                            }
                        });


                        }else {
                            Log.w(TAG, "DeleteEvent:failure no User");
                            showToast(R.string.edit_user_profile_error_deleting_event);
                        }

                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void loginIntent() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onClick_changePassword(View view) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.popup_edit_user_profile_password, null);

        final EditText txtOldPassword = promptsView.findViewById(R.id.user_edit_popup_password_txtOldPassword);
        final EditText txtEnterPassword = promptsView.findViewById(R.id.user_edit_popup_password_txtEnterPassword);
        final EditText txtRepeatPassword = promptsView.findViewById(R.id.user_edit_popup_password_txtRepeatPassword);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(promptsView)
                .setCancelable(false)
                .setNegativeButton(R.string.edit_create_event_dialog_cancel_button, null)
                .setPositiveButton(R.string.edit_user_profile_change_button, null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                final DialogInterface DIALOG = dialog;
                Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DIALOG.cancel();
                    }
                });

                Button positivButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positivButton.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        App.log(TAG,
                                "Old Password=" + txtOldPassword.getText().toString() +
                                        "\nNew Password=" + txtEnterPassword.getText().toString() +
                                        "\nRepeated Password=" + txtRepeatPassword.getText().toString()
                        );

                        String str = UserValidation.password(context, txtEnterPassword.getText().toString(), txtRepeatPassword.getText().toString(), true);
                        txtEnterPassword.setError(str);
                        txtRepeatPassword.setError(str);


                        if (str == null && firebaseUser!=null && firebaseUser.getEmail()!=null) {
                            AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), txtOldPassword.getText().toString());

                            firebaseUser.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            App.log(TAG, "User re-authenticated.");
                                            if (task.isSuccessful()) {
                                                firebaseUser.updatePassword(txtEnterPassword.getText().toString());
                                                DIALOG.cancel();
                                            } else {
                                                txtOldPassword.setError(getResources().getString(R.string.user_validation_error_passwords_wrong_password));
                                            }
                                        }
                                    });
                        }
                    }
                });


            }
        });

        // show it
        alertDialog.show();

    }

    public void onClick_changeEmail(View view) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.popup_edit_user_profil_email, null);

        final EditText txtPassword = promptsView.findViewById(R.id.user_edit_popup_email_txtPassword);
        final EditText txtEmail = promptsView.findViewById(R.id.user_edit_popup_email_txtEmail);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(promptsView)
                .setCancelable(false)
                .setNegativeButton(R.string.edit_create_event_dialog_cancel_button, null)
                .setPositiveButton(R.string.edit_user_profile_change_button, null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (firebaseUser != null && firebaseUser.getEmail()!=null) {
                    txtEmail.setText(firebaseUser.getEmail());

                    final DialogInterface DIALOG = dialog;
                    Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DIALOG.cancel();
                        }
                    });

                    Button positivButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    positivButton.setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View view) {

                            String str = UserValidation.email(txtEmail.getText().toString());
                            txtEmail.setError(str);

                            String str2 = UserValidation.notEmpty(txtPassword.getText().toString(),0);
                            txtPassword.setError(str2);

                            if (str == null && str2==null && firebaseUser!=null && firebaseUser.getEmail()!=null) {
                                AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), txtPassword.getText().toString());

                                firebaseUser.reauthenticate(credential)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                App.log(TAG, "User re-authenticated.");
                                                if (task.isSuccessful()) {
                                                    firebaseUser.updateEmail(txtEmail.getText().toString());
                                                    DIALOG.cancel();
                                                } else {
                                                    txtPassword.setError(getResources().getString(R.string.user_validation_error_passwords_wrong_password));
                                                }
                                            }
                                        });
                            }
                        }
                    });


                }else {
                    showToast(R.string.error_no_connection_to_database);
                }
            }
        });

        // show it
        alertDialog.show();

    }
}
