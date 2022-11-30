package com.example.myfavbook.activities;

import static android.content.ContentValues.TAG;
import static com.example.myfavbook.R.id.idBtnDelete;
import static com.example.myfavbook.R.id.idBtnRate;
import static com.example.myfavbook.R.id.idBtnReview;
import static com.example.myfavbook.R.id.rate;

import static com.example.myfavbook.R.id.review;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfavbook.R;
import com.example.myfavbook.entities.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class BookDetails extends AppCompatActivity {

    // creating variables for strings,text view, image views and button.
    String title, subtitle, publisher, publishedDate, description, thumbnail, previewLink, infoLink, buyLink, review;
    EditText mReview, mRate;
    
    int pageCount;
    private ArrayList<String> authors;


    TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV;
    Button previewBtn, buyBtn, addBtn, deleteBtn, rateBtn, reviewBtn;
    private ImageView bookIV;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth dbAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // initializing our views..
        titleTV = findViewById(R.id.idTVTitle);
        subtitleTV = findViewById(R.id.idTVSubTitle);
        publisherTV = findViewById(R.id.idTVpublisher);
        descTV = findViewById(R.id.idTVDescription);
        pageTV = findViewById(R.id.idTVNoOfPages);
        publishDateTV = findViewById(R.id.idTVPublishDate);
        previewBtn = findViewById(R.id.idBtnPreview);
        buyBtn = findViewById(R.id.idBtnBuy);
        addBtn = findViewById(R.id.idBtnAdd);
        deleteBtn = findViewById(idBtnDelete);
        bookIV = findViewById(R.id.idIVbook);
        rateBtn = findViewById(idBtnRate);
        reviewBtn = findViewById(idBtnReview);
        mReview = findViewById(R.id.review);
        mRate = findViewById(rate);



        // getting the data which we have passed from our adapter class.
        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        publisher = getIntent().getStringExtra("publisher");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        pageCount = getIntent().getIntExtra("pageCount", 0);
        thumbnail = getIntent().getStringExtra("thumbnail");
        previewLink = getIntent().getStringExtra("previewLink");
        infoLink = getIntent().getStringExtra("infoLink");
        buyLink = getIntent().getStringExtra("buyLink");


        Book libro = new Book(title, subtitle, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);
        // after getting the data we are setting that data to our text views and image view.

        titleTV.setText(title);
        subtitleTV.setText(subtitle);
        publisherTV.setText(publisher);
        publishDateTV.setText("Published On : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("No Of Pages : " + pageCount);
        Picasso.get().load(thumbnail).into(bookIV);
        setTextReview();
        setTextRate();




        // adding on click listener for our preview button.
        previewBtn.setOnClickListener(v -> {
            if (previewLink.isEmpty()) {
                // below toast message is displayed when preview link is not present.
                Toast.makeText(BookDetails.this, "No preview Link present", Toast.LENGTH_SHORT).show();
                return;
            }
            // if the link is present we are opening that link via an intent.
            Uri uri = Uri.parse(previewLink);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        });

        // initializing on click listener for buy button.
        buyBtn.setOnClickListener(v -> {
            if (buyLink.isEmpty()) {
                // below toast message is displaying when buy link is empty.
                Toast.makeText(BookDetails.this, "No buy page present for this book", Toast.LENGTH_SHORT).show();
                return;
            }
            // if the link is present we are opening the link via an intent.
            Uri uri = Uri.parse(buyLink);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        });

        addBtn.setOnClickListener(v -> {

            //retrieving document/book from firebase
            DocumentReference docRef = db.collection("books").document(title);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //if document exists we don´t add a new book
                    if (document.exists()) {
                        if (!Objects.equals(Objects.requireNonNull(dbAuth.getCurrentUser()).getEmail(), Objects.requireNonNull(Objects.requireNonNull(document.getData()).get("owner")).toString())){
                            Map<String, Object> book = new HashMap<>();
                            book.put("title", title);
                            book.put("subtitle", subtitle);
                            book.put("publisher", publisher);
                            book.put("publishedDate",publishedDate);
                            book.put("description",description);
                            book.put("pageCount", pageCount);
                            book.put("thumbnail",thumbnail);
                            book.put("previewLink",previewLink);
                            book.put("infoLink",infoLink);
                            book.put("buyLink", buyLink);
                            book.put("owner",dbAuth.getCurrentUser().getEmail());


                            // Add a new document with a generated ID

                            db.collection("books").document(title)
                                    .set(book)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            //if document doesn´t exists we add a new book
                        }else{
                            Toast.makeText(BookDetails.this, "El libro existe, no se puede añadir", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Create a new book
                        Map<String, Object> book = new HashMap<>();
                        book.put("title", title);
                        book.put("subtitle", subtitle);
                        book.put("publisher", publisher);
                        book.put("publishedDate",publishedDate);
                        book.put("description",description);
                        book.put("pageCount", pageCount);
                        book.put("thumbnail",thumbnail);
                        book.put("previewLink",previewLink);
                        book.put("infoLink",infoLink);
                        book.put("buyLink", buyLink);

                        // Add a new document with a generated ID
                        if (Objects.nonNull(dbAuth.getCurrentUser().getEmail())) {
                            db.collection(Objects.requireNonNull(dbAuth.getCurrentUser()).getEmail()).document(title)
                                    .set(book)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
                            Log.d(TAG, "No such document");
                            Toast.makeText(BookDetails.this, "Añadido correctamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    Toast.makeText(BookDetails.this, "Error al añadir: "+ Objects.requireNonNull(task.getException()), Toast.LENGTH_SHORT).show();
                }
            });



        });


        deleteBtn.setOnClickListener(view -> {

            db.collection(dbAuth.getCurrentUser().getEmail()).document(title)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
            Toast.makeText(BookDetails.this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
            // Refresh mybooks instance to apply changes when a book is deleted
            startActivity(new Intent(getApplicationContext(), Books.class));
            finish();
        });

        reviewBtn.setOnClickListener(view ->{
            String review = mReview.getText().toString().trim();
            // Update one field, creating the document if it does not already exist.
            Map<String, Object> data = new HashMap<>();
            data.put("review", review);

            db.collection(dbAuth.getCurrentUser().getEmail()).document(title)
                    .set(data, SetOptions.merge());
           startActivity(new Intent(getApplicationContext(), Books.class));
        });

        rateBtn.setOnClickListener(view -> {
            String rate = mRate.getText().toString().trim();
            // Update one field, creating the document if it does not already exist.
            Map<String, Object> data = new HashMap<>();
            data.put("rate", rate);

            db.collection(dbAuth.getCurrentUser().getEmail()).document(title)
                    .set(data, SetOptions.merge());
            startActivity(new Intent(getApplicationContext(), Books.class));
        });



    }

    private void setTextReview(){

        DocumentReference docRef = db.collection(dbAuth.getCurrentUser().getEmail()).document(title);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                     if (document.exists()) {
                         if(document.getData().get("review") != null){
                             mReview.setText((String) document.getData().get("review"));

                         }else{
                             mReview.setText(null);
                         }

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    private void setTextRate(){

        DocumentReference docRef = db.collection(dbAuth.getCurrentUser().getEmail()).document(title);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        if(document.getData().get("rate") != null){
                            mRate.setText((String) document.getData().get("rate"));

                        }else{
                            mRate.setText(null);
                        }

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
