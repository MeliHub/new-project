package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.crash.FirebaseCrash;
import com.reidius.lawrenceafriyie.overwatchmap.FinalRegistrationActivity;
import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiClient;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiInterface;
import com.reidius.lawrenceafriyie.overwatchmap.models.Student;
import com.reidius.lawrenceafriyie.overwatchmap.services.SharedPrefManager;
import com.reidius.lawrenceafriyie.overwatchmap.models.Person;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

            final EditText personID = (EditText) findViewById(R.id.id);
            personID.requestFocus();
            final EditText name = (EditText) findViewById(R.id.name);
            final EditText surname = (EditText) findViewById(R.id.surname);
            final EditText email = (EditText) findViewById(R.id.email);
            final EditText contactNo = (EditText) findViewById(R.id.contact);
            final EditText username = (EditText) findViewById(R.id.username);
            final EditText password = (EditText) findViewById(R.id.password);
            final EditText confirmPassword = (EditText) findViewById(R.id.confirmPassword);
            final String deviceToken = SharedPrefManager.getmInstance(this).getKeyAccessToken();
            final String personType = "Student";



            findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (password.getText().toString().equals(confirmPassword.getText().toString()) && personID.getText() != null && name.getText() != null &&
                            surname.getText() != null && email.getText() != null) {
                        Intent intent = new Intent(RegistrationActivity.this, FinalRegistrationActivity.class);
                        Bundle extras = new Bundle();

                        extras.putString("personID", personID.getText().toString());
                        extras.putString("name", name.getText().toString());
                        extras.putString("surname", surname.getText().toString());
                        extras.putString("email", email.getText().toString());
                        extras.putString("contactNo", contactNo.getText().toString());
                        extras.putString("username", username.getText().toString());
                        extras.putString("password", password.getText().toString());
                        extras.putString("deviceToken", deviceToken.toString());
                        extras.putString("personType", personType.toString());
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                    else
                    {
                        password.setError("Enter all required fields * and confirm password!");
                        password.requestFocus();
                    }
                    String number = contactNo.getText().toString().trim();
                    if (number.isEmpty() || number.length() < 12) {
                        contactNo.setError("Valid Number Required!");
                        contactNo.requestFocus();
                        return;
                    }
                    contactNo.setText(number);
                }

            });
        }
    }

