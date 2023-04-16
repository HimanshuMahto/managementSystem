package com.example.hostelmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public void loggedInUser(View view){
        Intent myIntent=new Intent(MainActivity.this,login_user.class);
        startActivity(myIntent);
    }
    public void loggedInAdmin(View view){
        startActivity(new Intent(MainActivity.this,login_admin.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}