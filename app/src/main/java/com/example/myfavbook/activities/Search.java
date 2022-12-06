package com.example.myfavbook.activities;


import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myfavbook.R;
import com.example.myfavbook.entities.Book;
import com.example.myfavbook.adapters.BookAdapter;
import com.example.myfavbook.entities.Review;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    //creating variables for our request queue, array list, progressbar, edittext, image button and our recycler view.
    private RequestQueue mRequestQueue;
    private ArrayList<Book> libraryToShow;
    private ArrayList<Book> favBookLibrary = new ArrayList<>();
    private ProgressBar progressBar;
    private EditText searchEdt;
    private ImageButton searchBtn;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    BottomNavigationView bottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeViews();
        loadLibrary();
        bottomNavigationView.setOnItemSelectedListener(item -> selectView(item));


        //initializing on click listener for our button.
        searchBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            search();
        });
    }

    //main method to search books
    private void search() {
        if (searchEdt.getText().toString().isEmpty()) {
            searchEdt.setError("Please enter search query");
        }else {
            if (notInOurLibrary(searchEdt.getText().toString())) {
                getBooksInfoFromApi(searchEdt.getText().toString());
            } else {
                loadLibraryToShow(searchEdt.getText().toString());
                showOurLibrary();
                progressBar.setVisibility(View.GONE);
            }
        }
    }
    // initializing our views
    private void initializeViews() {
        bottomNavigationView = findViewById((R.id.btn_navigator));
        bottomNavigationView.setSelectedItemId(R.id.search);
        progressBar = findViewById(R.id.idLoadingPB);
        searchEdt = findViewById(R.id.idEdtSearchBooks);
        searchBtn = findViewById(R.id.idBtnSearch);
    }

    //switch activities
    private Boolean selectView(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.search:

                return true;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(),MainHome.class));
                overridePendingTransition(0,0);
                return true;

            case R.id.more:
                startActivity(new Intent(getApplicationContext(),More.class));
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

    //load our own library from database if exist
    private void loadLibrary() {
        //show books from the library
        db.collection("books")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        favBookLibrary = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //set book values
                            Book book = setBookValues(document);
                            //below line is use to pass our modal class in our array list.
                            favBookLibrary.add(book);
                            Log.d(TAG, "Loaded Succesfully");
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    //method to get the books from the api and store to our own database
    private void getBooksInfoFromApi(String query) {
        libraryToShow = new ArrayList<>();
        //below line is use to initialize the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(Search.this);
        //below line is use to clear cache this will be use when our data is being updated.
        mRequestQueue.getCache().clear();
        //below is the url for getting data from API in json format.
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        //below line we are  creating a new request queue.
        RequestQueue queue = Volley.newRequestQueue(Search.this);

        //below line is use to make json object request inside that we are passing url, get method and getting json object. .
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            progressBar.setVisibility(View.GONE);
            //inside on response method we are extracting all our json data.
            //related to the api response we have a list of "items" that´s why we retrieve them in the next line
            try {
                JSONArray itemsArray = response.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    //Extract data for each item and save to modal class
                    Book bookInfo = extractData(itemsArray, i);
                    saveToDataBase("books", bookInfo);

                    favBookLibrary.add(bookInfo); //add to our own database collection
                    libraryToShow.add(bookInfo); //list of books to show based on the search

                }
            } catch (Exception e) {
                e.printStackTrace();
                //displaying a toast message when we get any error from API
                Toast.makeText(Search.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            //also displaying error message in toast.
            Toast.makeText(Search.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
        });
        showOurLibrary();

        //at last we are adding our json object request in our request queue.
        queue.add(booksObjrequest);

    }

    //method to save in the collection "books" the book
    private void saveToDataBase(String books, Book bookInfo) {
        db.collection(books).document(bookInfo.getTitle())
                .set(bookInfo)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

    }

    //method to extract data and save to a book
    private Book extractData(JSONArray itemsArray, int i) {
        try {
            JSONObject itemsObj = itemsArray.getJSONObject(i);
            JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
            String title = volumeObj.optString("title");
            String subtitle = volumeObj.optString("subtitle");
            String publisher = volumeObj.optString("publisher");
            String publishedDate = volumeObj.optString("publishedDate");
            String description = volumeObj.optString("description");
            int pageCount = volumeObj.optInt("pageCount");
            JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
            String thumbnail = imageLinks.optString("thumbnail");
            String previewLink = volumeObj.optString("previewLink");
            String infoLink = volumeObj.optString("infoLink");
            JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
            String buyLink = saleInfoObj.optString("buyLink");
            return new Book(title, subtitle, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);

        } catch (Exception e) {
            e.printStackTrace();
            //displaying a toast message when we get any error from API
            Toast.makeText(Search.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    //return a book based on the document
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
                (String) document.getData().get("buyLink"),
                (ArrayList<Review>) document.getData().get("reviews"),
                Math.toIntExact((Long) document.getData().get("totalRating")),
                (double) Math.toIntExact((Long) document.getData().get("totalRating"))
        );
    }

    //method to display our library
    private void showOurLibrary() {
        BookAdapter adapter = new BookAdapter(this.libraryToShow, Search.this);
        //below line is use to add linear layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search.this, RecyclerView.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.idRVBooks);
        //in below line we are setting layout manager and adapter to our recycler view.
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    //load the library we show to the user
    private void loadLibraryToShow(String bookToFind) {
        libraryToShow = new ArrayList<>();
        for (Book book : favBookLibrary) {
            if (book.getTitle().toLowerCase().contains(bookToFind)) {
                libraryToShow.add(book);
            }
        }
    }

    //method to check whether the book is in our library or not
    private boolean notInOurLibrary(String bookToFind) {
        for (Book book : favBookLibrary) {
            if (book.getTitle().contains(bookToFind))
                return false;
        }
        return true;
    }
}