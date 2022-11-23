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
    private TextView a2;

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
        a2 = findViewById(R.id.ans2);
        //initializing on click listner for our button.
        q1.setOnClickListener(new View.OnClickListener() {
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
    }
}