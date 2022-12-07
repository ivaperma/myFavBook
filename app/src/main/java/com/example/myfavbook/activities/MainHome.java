package com.example.myfavbook.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfavbook.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainHome extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button mLogoutBtn;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        bottomNavigationView = findViewById(R.id.btn_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);
        mLogoutBtn = findViewById(R.id.btnLogout);

        bottomNavigationView.setOnItemSelectedListener(item -> selectView(item));

        mLogoutBtn.setOnClickListener(view -> {
            Toast.makeText(MainHome.this, "Deslogueado", Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
    }

    //switch activities
    private Boolean selectView(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.home:

                return true;

            case R.id.faq:
                startActivity(new Intent(getApplicationContext(), Faq.class));
                overridePendingTransition(0,0);
                return true;
            case R.id.search:
                startActivity(new Intent(getApplicationContext(),Search.class));
                overridePendingTransition(0,0);
                return true;
            case R.id.my_books:
                startActivity(new Intent(getApplicationContext(),Books.class));
                overridePendingTransition(0,0);
                return true;

            case R.id.friends:
                startActivity(new Intent(getApplicationContext(),Friends.class));
                overridePendingTransition(0,0);
                return true;
        }
        return false;
    }
}