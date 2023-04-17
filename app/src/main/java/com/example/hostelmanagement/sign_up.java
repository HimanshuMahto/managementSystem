package com.example.hostelmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

//import kotlinx.coroutines.scheduling.Task;

public class sign_up extends AppCompatActivity {

    private static final String TAG="SignUp";

    public boolean isValidString(String target){
        return ((target != null) && (!target.equals(""))
                && (target.matches("^[a-z A-Z]*$")));
    }
    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public boolean isValidPassword(String target){
        if(target.length()>6){
            return ((target != null) && (!target.equals(""))
                    && (target.matches("^[a-zA-Z 0-9#-&@]*$")));
        }
        else
            return false;
    }
    public boolean isValidDOB(String target) {
        int counter = 0;
        if (target.length() == 10) {
            if ((target != null) && (!target.equals("")) && (target.matches("^[0-9-]*$"))) {
                for (int i = 0; i < 10; i++) {
                    char ch = target.charAt(i);
                    if (ch == '-') {
                        counter++;
                    }
                }
                if (counter == 2) {
                    for (int i = 0; i < 10; i++) {
                        char ch = target.charAt(i);
                        if (ch == '-' && (i == 2 || i == 5)) {
                            counter++;
                        }
                    }
                    if (counter == 4)
                        return true;
                    else
                        return false;
                } else {
                    return false;
                }
            } else
                return false;
        }
        else
            return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button register=findViewById(R.id.newAdminRegister);
        EditText name=findViewById(R.id.newAdminName);
        EditText email=findViewById(R.id.newAdminEmail);
        EditText password=findViewById(R.id.newAdminPassword);
        EditText dob=findViewById(R.id.newAdminDOB);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidString(name.getText().toString())){
                    if(isValidEmail(email.getText().toString())){
                        if(isValidPassword(password.getText().toString())){
                            if(isValidDOB(dob.getText().toString())){
                                registerUser(name, email, password, dob);
                            }
                            else{
                                dob.setError("Invalid Date of Birth");
                                dob.requestFocus();
                            }
                        }
                        else{
                            password.setError("Password should contain 0-9 a-z A-z special characters & >6");
                            password.requestFocus();
                        }
                    }
                    else {
                        email.setError("Invalid Email");
                        email.requestFocus();
                    }
                }
                else{
                    name.setError("Invalid Name");
                    name.requestFocus();
                }
            }
        });
    }

    // connecting to firebase for storing user signUp details for further login
    private void registerUser(EditText name, EditText email, EditText password, EditText dob) {
        String adminName=name.getText().toString();
        String adminEmail=email.getText().toString();
        String adminPassword=password.getText().toString();
        String adminDOB=dob.getText().toString();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(adminEmail,adminPassword).addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(sign_up.this,"Registered successfully",Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser=mAuth.getCurrentUser();
                    firebaseUser.sendEmailVerification();
                    Intent loginIntent=new Intent(sign_up.this,login_admin.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                    finish();
                }
                else{
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        email.setError("Invalid email");
                        email.requestFocus();
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        email.setError("User Exists");
                        email.requestFocus();
                    }
                    catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(sign_up.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}