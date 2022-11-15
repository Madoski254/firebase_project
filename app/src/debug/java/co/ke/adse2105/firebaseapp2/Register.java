package co.ke.adse2105.firebaseapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    EditText etmail;

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
}