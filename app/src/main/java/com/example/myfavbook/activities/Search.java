package com.example.myfavbook.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myfavbook.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    //creating variables for our request queue, array list, progressbar, edittext, image button and our recycler view.
    private RequestQueue mRequestQueue;
    private ArrayList<BookInfo> bookInfoArrayList;
    private ProgressBar progressBar;
    private EditText searchEdt;
    private ImageButton searchBtn;


    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bottomNavigationView = findViewById((R.id.btn_navigator));
        bottomNavigationView.setSelectedItemId(R.id.search);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        });



        //initializing our views.
        progressBar = findViewById(R.id.idLoadingPB);
        searchEdt = findViewById(R.id.idEdtSearchBooks);
        searchBtn = findViewById(R.id.idBtnSearch);

        //initializing on click listener for our button.
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                //checking if our edittext field is empty or not.
                if (searchEdt.getText().toString().isEmpty()) {
                    searchEdt.setError("Please enter search query");
                }else {
                    getBooksInfo(searchEdt.getText().toString());
                }
            }
        });
    }

    private void getBooksInfo(String query) {
        //creating a new array list.
        bookInfoArrayList = new ArrayList<>();
        //below line is use to initialize the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(Search.this);
        //below line is use to clear cache this will be use when our data is being updated.
        mRequestQueue.getCache().clear();
        //below is the url for getting data from API in json format.
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        //below line we are  creating a new request queue.
        RequestQueue queue = Volley.newRequestQueue(Search.this);

        //below line is use to make json object request inside that we are passing url, get method and getting json object. .
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
                        String thumbnail = imageLinks.optString("thumbnail");
                        String previewLink = volumeObj.optString("previewLink");
                        String infoLink = volumeObj.optString("infoLink");
                        JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                        String buyLink = saleInfoObj.optString("buyLink");
                        ArrayList<String> authorsArrayList = new ArrayList<>();
                        if (authorsArray.length() != 0) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                authorsArrayList.add(authorsArray.optString(i));
                            }
                        }
                        //after extracting all the data we are saving this data in our modal class.
                        BookInfo bookInfo = new BookInfo(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);
                        //below line is use to pass our modal class in our array list.
                        bookInfoArrayList.add(bookInfo);
                        //below line is use to pass our array list in adapter class.
                        BookAdapter adapter = new BookAdapter(bookInfoArrayList, Search.this);
                        //below line is use to add linear layout manager for our recycler view.
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search.this, RecyclerView.VERTICAL, false);
                        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.idRVBooks);
                        //in below line we are setting layout manager and adapter to our recycler view.
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //displaying a toast message when we get any error from API
                    Toast.makeText(Search.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //also displaying error message in toast.
                Toast.makeText(Search.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        //at last we are adding our json object request in our request queue.
        queue.add(booksObjrequest);

    }
}