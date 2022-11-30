package com.example.myfavbook.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfavbook.R;
import com.example.myfavbook.entities.Book;
import com.example.myfavbook.adapters.BookAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Books extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth dbAuth = FirebaseAuth.getInstance();
    private ArrayList<Book> bookArrayList;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        bottomNavigationView = findViewById(R.id.btn_navigator);
        bottomNavigationView.setSelectedItemId(R.id.my_books);

        //switch activities
        bottomNavigationView.setOnItemSelectedListener(item -> selectView(item));

        //show books of the user
        db.collection(dbAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookArrayList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData().get("publisher"));
                            showUserBooks(document, bookArrayList);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private Boolean selectView(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_books:

                return true;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(), MainHome.class));
                overridePendingTransition(0, 0);
                return true;

            case R.id.more:
                startActivity(new Intent(getApplicationContext(), More.class));
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
    }

    private void showUserBooks(QueryDocumentSnapshot document, ArrayList<Book> bookArrayList) {
        //set book values
        Book book = setBookValues(document);
        //below line is use to pass our modal class in our array list.
        bookArrayList.add(book);
        //below line is use to pass our array list in adapter class.
        BookAdapter adapter = new BookAdapter(bookArrayList, Books.this);
        //below line is use to add linear layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Books.this, RecyclerView.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.idRVBooks);
        //in below line we are setting layout manager and adapter to our recycler view.
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    private Book setBookValues(QueryDocumentSnapshot document) {
        return new Book((String) document.getData().get("title"),
                (String) document.getData().get("subtitle"),
                (String) document.getData().get("publisher"),
                (String) document.getData().get("publishedDate"),
                (String) document.getData().get("description"),
                Math.toIntExact((Long) document.getData().get("pageCount")),
                (String) document.getData().get("thumbnail"),
                (String) document.getData().get("previewLink"),
                (String) document.getData().get("infoLink"),
                (String) document.getData().get("buyLink")


        );
    }
}