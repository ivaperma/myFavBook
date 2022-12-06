package com.example.myfavbook.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfavbook.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class More extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private TextView a1, a2, a3, a4, a5, a6;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        bottomNavigationView = findViewById(R.id.btn_navigator);
        bottomNavigationView.setSelectedItemId(R.id.more);
        bottomNavigationView.setOnItemSelectedListener(item -> selectView(item));



        TextView q1 = findViewById(R.id.qus1);
        a1 = findViewById(R.id.ans1);
        //initializing on click listener for our button.
        q1.setOnClickListener(v -> {
            if(a1.getVisibility()== View.GONE){
                a1.setVisibility(View.VISIBLE);
            }
            else{
                a1.setVisibility(View.GONE);
            }
        });
        TextView q2 = findViewById(R.id.qus2);
        a2 = findViewById(R.id.ans2);
        //initializing on click listener for our button.
        q2.setOnClickListener(v -> {
            if(a2.getVisibility()== View.GONE){
                a2.setVisibility(View.VISIBLE);
            }
            else{
                a2.setVisibility(View.GONE);
            }
        });
        TextView q3 = findViewById(R.id.qus3);
        a3 = findViewById(R.id.ans3);
        //initializing on click listener for our button.
        q3.setOnClickListener(v -> {
            if(a3.getVisibility()== View.GONE){
                a3.setVisibility(View.VISIBLE);
            }
            else{
                a3.setVisibility(View.GONE);
            }
        });
        TextView q4 = findViewById(R.id.qus4);
        a4 = findViewById(R.id.ans4);
        //initializing on click listener for our button.
        q4.setOnClickListener(v -> {
            if(a4.getVisibility()== View.GONE){
                a4.setVisibility(View.VISIBLE);
            }
            else{
                a4.setVisibility(View.GONE);
            }
        });
        TextView q5 = findViewById(R.id.qus5);
        a5 = findViewById(R.id.ans5);
        //initializing on click listener for our button.
        q5.setOnClickListener(v -> {
            if(a5.getVisibility()== View.GONE){
                a5.setVisibility(View.VISIBLE);
            }
            else{
                a5.setVisibility(View.GONE);
            }
        });
        TextView q6 = findViewById(R.id.qus6);
        a6 = findViewById(R.id.ans6);
        //initializing on click listener for our button.
        q6.setOnClickListener(v -> {
            if(a6.getVisibility()== View.GONE){
                a6.setVisibility(View.VISIBLE);
            }
            else{
                a6.setVisibility(View.GONE);
            }
        });
    }
    //switch activities
    private Boolean selectView(MenuItem item) {
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
}