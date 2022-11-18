package co.ke.adse2105.firebaseapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {

    //Firebase auth stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    //Declare button objects
    Button btnLogin, btnRegister;
    private ProgressDialog progressDialog;
    EditText etEmail, etPassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.activity_login_tv_forgot_password)
    TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //code to make ButterKnife bidding on view work
        ButterKnife.bind(this);

        //Gte a ref to the firebase auth obj
        mAuth = FirebaseAuth.getInstance();

        //Attach an authentication listener to detect logout/sing out
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) //Check if the user is logged in
                    Toast.makeText(Login.this, "Signed in!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Login.this, "Signed out!", Toast.LENGTH_SHORT).show();

            }
        };

        //Instantiate the views/ui widgets
        btnLogin = findViewById(R.id.activity_login_btn_login);
        btnRegister = findViewById(R.id.activity_login_btn_register);
        etEmail = findViewById(R.id.activity_login_et_email);
        etPassword = findViewById(R.id.activity_login_et_password);
        progressDialog = new ProgressDialog(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        //Event handling for the Register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();

            }
        });



    }

    private void signInUser() {
        //validate the username and password
        if (!validateLoginDetails())
            return;

        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        //Try to sign in the user and display the progress dialog
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        //Execute  this once the user is successfully logged in
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Signed in successfully!", Toast.LENGTH_SHORT).show();
                            //Go to thee main screen
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Signed in failed!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(Login.this, "Invalid Password!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(e instanceof FirebaseAuthInvalidUserException){
                            Toast.makeText(Login.this, "Invalid Email Address",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Login.this, e.getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private boolean validateLoginDetails() {
        final String email = etEmail.getText().toString().trim();
        final String password = etEmail.getText().toString().trim();

        if (email.isEmpty() || !email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f" +
                "\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" +
                "|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|" +
                "[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21" +
                "-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+))")) {
            etEmail.setError("Valid email required");
            Toast.makeText(getApplicationContext(), "Please enter a valid email Address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @SuppressLint("NonConstrainResourceId")
    @OnClick(R.id.activity_login_tv_forgot_password)
    public void handleTvForgotPasswordClick() {

        final String email = etEmail.getText().toString().trim();

        if (email.isEmpty() || !email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f" +
                "\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" +
                "|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|" +
                "[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21" +
                "-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+))")) {
            etEmail.setError("Valid email required");
            Toast.makeText(getApplicationContext(), "Please enter a valid email Address", Toast.LENGTH_SHORT).show();

        } else {
            progressDialog.setMessage("Resting password ,please wait...");
            progressDialog.show();
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Check email for password reset instruction", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error resting password ,please confirm you gave us the correct email and password  Please try again latter...", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    //Override the onStart()  & onStop method
    @Override
    protected void onStart() {
        super.onStart();
        //Add the auth listener
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Add the auth listener
        if (mAuthStateListener != null)
            mAuth.addAuthStateListener(mAuthStateListener);
    }


}