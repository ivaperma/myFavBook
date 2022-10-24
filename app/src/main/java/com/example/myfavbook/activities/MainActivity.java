package com.example.myfavbook.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.myfavbook.R;
import com.example.myfavbook.credentials.Home;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToHome(View view){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }


}