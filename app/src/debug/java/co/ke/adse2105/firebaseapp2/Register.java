package co.ke.adse2105.firebaseapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ke.adse2105.firebaseapp2.models.AppUser;
import co.ke.adse2105.firebaseapp2.utilities.BCrypt;

public class Register extends AppCompatActivity {

    //Firebase auth staff
    private FirebaseAuth mAuth;
    private DatabaseReference databaseUsers;

    //Use ButterKnife bind view to re views/widgets
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.activity_register_et_firstname)
    EditText etFirstname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.activity_register_et_lastname)
    EditText etLastname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.activity_register_et_email)
    EditText etMail;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.activity_register_et_password)
    EditText etPassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.activity_register_et_confirm_password)
    EditText etConfirmPassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.activity_register_btn_register)
    Button btnRegister;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get a ref/instance for the firebase obj
        mAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("AppUsers");

        //code to make ButterKnife bidding on view work
        ButterKnife.bind(this);

        //Instance the progress dialog
        progressDialog = new ProgressDialog(this);

    }


    /**
     * Method to hash users password using BCrypt encryption before storage in Firebase
     *
     * @param password2Hash the password that  is to be hashed /encrypted
     * @param
     */
    private static String hashPassword(String password2Hash) {
        return BCrypt.hashpw(password2Hash, BCrypt.gensalt());
    }


    /**
     * Method to validate the input data before submitting it to the firebase database
     *
     * @return boolean indicating whether all the data has been successfully validated
     */

    private boolean checkCredentials() {
        String fname, lname, email, password, confirmPassword;
        fname = etFirstname.getText().toString().trim();
        lname = etLastname.getText().toString().trim();
        email = etMail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();

        if (fname.isEmpty()) {
            etFirstname.setError("Your First name is required");
            return false;
        }

        if (lname.isEmpty()) {
            etLastname.setError("Your last name is required");
            return false;
        }


        if (email.isEmpty() || !email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f" +
                "\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" +
                "|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|" +
                "[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21" +
                "-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+))")) {
            etMail.setError("Valid email required");
            Toast.makeText(getApplicationContext(), "Please enter a valid email Address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,15}")) {
            etPassword.setError("Insecure password detected");
            Toast.makeText(getApplicationContext(), "Please enter a password that is at least 8 characters long,less than 15 characters" +
                    "contains a numbers mix of lowercase & uppercase letters ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            etPassword.setError("Password is required");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Password are not matching!");
            return false;
        }

        //When all the above are met/passed data should be ok
        return true;

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.activity_register_btn_register)
    public void handleBtnRegisterClicked() {
        createUserAccount();
    }

    private void createUserAccount() {
        //Validate the users details
        if (!checkCredentials())
            return; //Stop method execution

        //Once the details are successfully validated
        String fname = etFirstname.getText().toString().trim();
        String lname = etLastname.getText().toString().trim();
        String email = etMail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        //Display the progress dialog
        progressDialog.setMessage("Creating your account, please wait");
        progressDialog.show();

        //Code to create the users is waiting to register
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                task ->
                {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        //Get a reference to the current user
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        //Get the current user's unique ID
                        String userID = currentUser.getUid();

                        //Declare and instantiate an AppUser obj
                        AppUser appUser = new AppUser(fname, lname, email, hashPassword(password));

                        //Write the current user details to the firebase Realtime database
                        databaseUsers.child(userID).setValue(appUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Register.this, "User " + lname
                                        + " Successfully created!", Toast.LENGTH_SHORT).show();

                                //Redirect the user to the application's main screen/Activity
                                startActivity(new Intent(getApplicationContext(),
                                        MainActivity.class));
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(Register.this, "User account creation failed!"
                                , Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(Register.this, "Email address is already being used!"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Register.this, e.getLocalizedMessage()
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

