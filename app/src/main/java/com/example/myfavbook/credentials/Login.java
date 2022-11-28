package com.example.myfavbook.credentials;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfavbook.R;
import com.example.myfavbook.activities.MainHome;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn, mHomeBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.btnLogin);
        mCreateBtn = findViewById(R.id.textRegister);
        mHomeBtn = findViewById(R.id.txtHome);

        mLoginBtn.setOnClickListener(v -> {

            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                mEmail.setError("Email requerido.");
                return;
            }

            if(TextUtils.isEmpty(password)){
                mPassword.setError("Password requerida.");
                return;
            }

            if(password.length() < 6){
                mPassword.setError("Password debe contener al menos 6 Characters");
                return;
            }
            // Once the email and password is validated, the progress bar is displayed
            progressBar.setVisibility(View.VISIBLE);

            // Authenticate the user with Firebase

            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Logueado con éxito", Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    Intent intent = new Intent(Login.this, MainHome.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(Login.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            });

        });

        mCreateBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),Register.class)));

        mHomeBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),Home.class)));
    }
}