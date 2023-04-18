package com.example.hostelmanagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;

//import kotlinx.coroutines.scheduling.Task;

public class sign_up extends AppCompatActivity {

    private static final String TAG="SignUp";
    public DatePickerDialog picker;

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
        if (target.length() >=6) {
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

        //setting up datePicker for Date of Birth like the google uses one
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);
                //Date picker created here
                picker = new DatePickerDialog(sign_up.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateOfBirth1 = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        dob.setText(dateOfBirth1);
                    }
                }, year, month, day);
                picker.show();
            }
        });

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
        //creation of User Profile
        mAuth.createUserWithEmailAndPassword(adminEmail,adminPassword).addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser=mAuth.getCurrentUser();

                    //Update name of the  that is displayed
                    UserProfileChangeRequest userProfileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(adminName).build();
                    firebaseUser.updateProfile(userProfileChangeRequest);

                    //now here we use the method of saving the data of the user in the firebase realtime Database
                    ReadwriteUserDetails writeUserDetails=new ReadwriteUserDetails(adminDOB);

                    // Extracting the user reference for database using DatabaseReference
                    DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");

                    // user is the parent and all its details are child of that parent
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(sign_up.this,"Registered successfully! Check Mail for Verification",Toast.LENGTH_SHORT).show();
                                //Email verification by sending an email to verify the account the one which you have entered
                                firebaseUser.sendEmailVerification();
                                Intent loginIntent=new Intent(sign_up.this,login_admin.class);
                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(loginIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(sign_up.this,"Registered Unsuccessfully!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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