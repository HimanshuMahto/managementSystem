package com.example.hostelmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class login_admin extends AppCompatActivity {
    private EditText loginEmail, loginPassword;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private Button loginButton;
    public void signUp(View view){
        startActivity(new Intent(login_admin.this,sign_up.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        loginEmail=findViewById(R.id.emailAdminLogin);
        loginPassword=findViewById(R.id.passwordAdminLogin);
        progressBar=findViewById(R.id.progressBar);
        authProfile=FirebaseAuth.getInstance();
        loginButton=findViewById(R.id.adminLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=loginEmail.getText().toString();
                String password=loginPassword.getText().toString();
                if(TextUtils.isEmpty(email)){
                    loginEmail.setError("Email is required");
                    loginEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    loginEmail.setError("Invalid Email");
                    loginEmail.requestFocus();
                }
                else if (TextUtils.isEmpty(password)) {
                    loginPassword.setError("Password is required");
                    loginPassword.requestFocus();
                }
                else {
                    progressBar.setVisibility(view.VISIBLE);
                    adminLogin(email,password);
                }
            }
        });
    }

    private void adminLogin(String email, String password) {
        authProfile.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(login_admin.this,"Logged in as Admin",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(login_admin.this,adminHomePage.class));
                }
                else{
                    Toast.makeText(login_admin.this,"Please try again later",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}