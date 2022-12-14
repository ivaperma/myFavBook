package com.example.myfavbook.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfavbook.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Friends extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        bottomNavigationView = findViewById((R.id.btn_navigator));
        bottomNavigationView.setSelectedItemId(R.id.friends);
        bottomNavigationView.setOnItemSelectedListener(item -> selectView(item));

    }
    //switch activities
    private Boolean selectView(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.friends:

                return true;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(),MainHome.class));
                overridePendingTransition(0,0);
                return true;

            case R.id.faq:
                startActivity(new Intent(getApplicationContext(), Faq.class));
                overridePendingTransition(0,0);
                return true;

            case R.id.my_books:
                startActivity(new Intent(getApplicationContext(),Books.class));
                overridePendingTransition(0,0);
                return true;

            case R.id.search:
                startActivity(new Intent(getApplicationContext(),Search.class));
                overridePendingTransition(0,0);
                return true;
        }
        return false;
    }
}
