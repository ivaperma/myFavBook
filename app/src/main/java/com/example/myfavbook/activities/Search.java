package com.example.myfavbook.activities;


import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

        bottomNavigationView = findViewById((R.id.btn_navigator));
        bottomNavigationView.setSelectedItemId(R.id.search);

        bottomNavigationView.setOnItemSelectedListener(item -> {
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
        });

        loadLibrary();
        //initializing our views.
        progressBar = findViewById(R.id.idLoadingPB);
        searchEdt = findViewById(R.id.idEdtSearchBooks);
        searchBtn = findViewById(R.id.idBtnSearch);

        //initializing on click listener for our button.
        searchBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            //checking if our edittext field is empty or not.
            if (searchEdt.getText().toString().isEmpty()) {
                searchEdt.setError("Please enter search query");
            }else {
                if (notInOurLibrary(searchEdt.getText().toString())) {
                    getBooksInfoFromApi(searchEdt.getText().toString());
                } else {
                    loadLibraryToShow(searchEdt.getText().toString());
                    showOurLibrary(libraryToShow);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

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

    private void showOurLibrary(ArrayList<Book> libraryToShow) {
        BookAdapter adapter = new BookAdapter(this.libraryToShow, Search.this);
        //below line is use to add linear layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search.this, RecyclerView.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.idRVBooks);
        //in below line we are setting layout manager and adapter to our recycler view.
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    private void loadLibraryToShow(String bookToFind) {
        libraryToShow = new ArrayList<>();
        for (Book book : favBookLibrary) {
            if (book.getTitle().toLowerCase().contains(bookToFind)) {
                libraryToShow.add(book);
            }
        }
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
                (String) document.getData().get("buyLink"),
                (ArrayList<Review>) document.getData().get("reviews"),
                Math.toIntExact((Long) document.getData().get("totalRating")),
                (double) Math.toIntExact((Long) document.getData().get("totalRating"))
        );
    }

    private boolean notInOurLibrary(String bookToFind) {
        for (Book book : favBookLibrary) {
            if (book.getTitle().contains(bookToFind))
                return false;
        }
        return true;
    }

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
                    JSONObject itemsObj = itemsArray.getJSONObject(i);
                    JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                    String title = volumeObj.optString("title");
                    String subtitle = volumeObj.optString("subtitle");
                    JSONArray authorsArray = volumeObj.getJSONArray("authors");
                    String publisher = volumeObj.optString("publisher");
                    String publishedDate = volumeObj.optString("publishedDate");
                    String description = volumeObj.optString("description");
                    int pageCount = volumeObj.optInt("pageCount");
                    JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                    assert imageLinks != null;
                    String thumbnail = imageLinks.optString("thumbnail");
                    String previewLink = volumeObj.optString("previewLink");
                    String infoLink = volumeObj.optString("infoLink");
                    JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                    assert saleInfoObj != null;
                    String buyLink = saleInfoObj.optString("buyLink");
                    ArrayList<String> authorsArrayList = new ArrayList<>();
                    if (authorsArray.length() != 0) {
                        for (int j = 0; j < authorsArray.length(); j++) {
                            authorsArrayList.add(authorsArray.optString(i));
                        }
                    }
                    //after extracting all the data we are saving this data in our modal class.
                    Book bookInfo = new Book(title, subtitle, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);
                    db.collection("books").document(bookInfo.getTitle())
                            .set(bookInfo)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                            .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

                    //below line is use to pass our modal class in our array list.
                    //bookInfoArrayList.add(bookInfo);
                    favBookLibrary.add(bookInfo);
                    libraryToShow.add(bookInfo);

                    //below line is use to pass our array list in adapter class.
                    /*BookAdapter adapter = new BookAdapter(favBookLibrary, Search.this);
                    //below line is use to add linear layout manager for our recycler view.
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search.this, RecyclerView.VERTICAL, false);
                    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.idRVBooks);
                    //in below line we are setting layout manager and adapter to our recycler view.
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setAdapter(adapter);*/
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
        showOurLibrary(libraryToShow);

        //at last we are adding our json object request in our request queue.
        queue.add(booksObjrequest);

    }
}