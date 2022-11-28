package com.example.myfavbook.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfavbook.R;
import com.example.myfavbook.credentials.Home;
import com.google.android.material.bottomnavigation.BottomNavigationView;




public class MyBooks extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigationView = findViewById(R.id.btn_navigator);
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.my_books:

                    return true;

                case R.id.more:

                    startActivity(new Intent(getApplicationContext(), More.class));
                    overridePendingTransition(0, 0);

                    return true;

                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), Home.class));

                    overridePendingTransition(0, 0);
                    return true;

                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), Search.class));
                    overridePendingTransition(0, 0);

                    return true;

                case R.id.friends:
                    startActivity(new Intent(getApplicationContext(), Friends.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });

    }}

