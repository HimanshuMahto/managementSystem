package com.example.hostelmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class sign_up extends AppCompatActivity {
    public boolean isValidString(String target){
        return ((target != null) && (!target.equals(""))
                && (target.matches("^[a-z A-Z]*$")));
    }
    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public boolean isValidPassword(String target){
        return ((target != null) && (!target.equals(""))
                && (target.matches("^[a-zA-Z 0-9#-&@]*$")));
    }
    public boolean isValidDOB(String target){
        return ((target != null) && (!target.equals(""))
                && (target.matches("^[0-9-]*$")));
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
                                System.out.println("hi");
                            }
                            else{
                                Toast.makeText(sign_up.this,"Invalid DOB",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(sign_up.this,"Invalid Password",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(sign_up.this, "Invalid Email", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(sign_up.this,"Invalid Name",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}