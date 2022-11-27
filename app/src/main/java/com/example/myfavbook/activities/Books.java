package com.example.myfavbook.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.myfavbook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Books extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth dbAuth = FirebaseAuth.getInstance();
    private ArrayList<BookInfo> bookInfoArrayList;
    private ArrayList<String> authors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        bottomNavigationView = findViewById(R.id.btn_navigator);
        bottomNavigationView.setSelectedItemId(R.id.my_books);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

        });

        db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            bookInfoArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getData().get("owner").toString().equals(dbAuth.getCurrentUser().getEmail())) {
                                    Log.d(TAG, document.getId() + " => " + document.getData().get("publisher"));
                                    String title = (String) document.getData().get("title");
                                    String subtitle = (String) document.getData().get("subtitle");
                                    String publisher = (String) document.getData().get("publisher");
                                    String publishedDate = (String) document.getData().get("publishedDate");
                                    String description = (String) document.getData().get("description");
                                    int pageCount = Math.toIntExact((Long) document.getData().get("pageCount"));
                                    String thumbnail = (String) document.getData().get("thumbnail");
                                    String previewLink = (String) document.getData().get("previewLink");
                                    String infoLink = (String) document.getData().get("infoLink");
                                    String buyLink = (String) document.getData().get("buyLink");

                                    //after extracting all the data we are saving this data in our modal class.
                                    BookInfo bookInfo = new BookInfo(title, subtitle, authors, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);
                                    //below line is use to pass our modal class in our array list.
                                    bookInfoArrayList.add(bookInfo);
                                    //below line is use to pass our array list in adapter class.
                                    BookAdapter adapter = new BookAdapter(bookInfoArrayList, Books.this);
                                    //below line is use to add linear layout manager for our recycler view.
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Books.this, RecyclerView.VERTICAL, false);
                                    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.idRVBooks);
                                    //in below line we are setting layout manager and adapter to our recycler view.
                                    mRecyclerView.setLayoutManager(linearLayoutManager);
                                    mRecyclerView.setAdapter(adapter);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}