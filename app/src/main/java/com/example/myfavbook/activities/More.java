package com.example.myfavbook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfavbook.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class More extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private TextView q1;
    private TextView a1;
    private TextView q2;
    private TextView a2;
    private TextView q3;
    private TextView a3;
    private TextView q4;
    private TextView a4;
    private TextView q5;
    private TextView a5;
    private TextView q6;
    private TextView a6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        bottomNavigationView = findViewById(R.id.btn_navigator);
        bottomNavigationView.setSelectedItemId(R.id.more);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.more:

                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainHome.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.my_books:
                        startActivity(new Intent(getApplicationContext(), Books.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.friends:
                        startActivity(new Intent(getApplicationContext(), Friends.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        q1 = findViewById(R.id.qus1);
        a1 = findViewById(R.id.ans1);
        //initializing on click listner for our button.
        q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a1.getVisibility()== View.GONE){
                    a1.setVisibility(View.VISIBLE);
                }
                else{
                    a1.setVisibility(View.GONE);
                }
            }
        });
        q2 = findViewById(R.id.qus2);
        a2 = findViewById(R.id.ans2);
        //initializing on click listner for our button.
        q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a2.getVisibility()== View.GONE){
                    a2.setVisibility(View.VISIBLE);
                }
                else{
                    a2.setVisibility(View.GONE);
                }
            }
        });
        q3 = findViewById(R.id.qus3);
        a3 = findViewById(R.id.ans3);
        //initializing on click listner for our button.
        q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a3.getVisibility()== View.GONE){
                    a3.setVisibility(View.VISIBLE);
                }
                else{
                    a3.setVisibility(View.GONE);
                }
            }
        });
        q4 = findViewById(R.id.qus4);
        a4 = findViewById(R.id.ans4);
        //initializing on click listner for our button.
        q4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a4.getVisibility()== View.GONE){
                    a4.setVisibility(View.VISIBLE);
                }
                else{
                    a4.setVisibility(View.GONE);
                }
            }
        });
        q5 = findViewById(R.id.qus5);
        a5 = findViewById(R.id.ans5);
        //initializing on click listner for our button.
        q5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a5.getVisibility()== View.GONE){
                    a5.setVisibility(View.VISIBLE);
                }
                else{
                    a5.setVisibility(View.GONE);
                }
            }
        });
        q6 = findViewById(R.id.qus6);
        a6 = findViewById(R.id.ans6);
        //initializing on click listner for our button.
        q6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a6.getVisibility()== View.GONE){
                    a6.setVisibility(View.VISIBLE);
                }
                else{
                    a6.setVisibility(View.GONE);
                }
            }
        });
    }
}