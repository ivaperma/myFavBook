package com.example.myfavbook.activities;

import static android.content.ContentValues.TAG;

import static com.example.myfavbook.R.id.idBtnDelete;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myfavbook.R;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.myfavbook.queries.Queries;


public class BookDetails extends AppCompatActivity {

    // creating variables for strings,text view, image views and button.
    String title, subtitle, publisher, publishedDate, description, thumbnail, previewLink, infoLink, buyLink;
    int pageCount;
    private ArrayList<String> authors;

    TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV;
    Button previewBtn, buyBtn, addBtn, deleteBtn;
    private ImageView bookIV;
    private Queries query1 = new Queries();

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

        BookInfo libro = new BookInfo(title, subtitle, authors, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);
        // after getting the data we are setting
        // that data to our text views and image view.
        titleTV.setText(title);
        subtitleTV.setText(subtitle);
        publisherTV.setText(publisher);
        publishDateTV.setText("Published On : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("No Of Pages : " + pageCount);
        Picasso.get().load(thumbnail).into(bookIV);

        // adding on click listener for our preview button.
        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewLink.isEmpty()) {
                    // below toast message is displayed when preview link is not present.
                    Toast.makeText(BookDetails.this, "No preview Link present", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if the link is present we are opening
                // that link via an intent.
                Uri uri = Uri.parse(previewLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        // initializing on click listener for buy button.
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyLink.isEmpty()) {
                    // below toast message is displaying when buy link is empty.
                    Toast.makeText(BookDetails.this, "No buy page present for this book", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if the link is present we are opening
                // the link via an intent.
                Uri uri = Uri.parse(buyLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //retrieving document/book from firebase
                DocumentReference docRef = db.collection("books").document(title);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            //if document exists we don´t add a new book
                            if (document.exists()) {
                                if (!dbAuth.getCurrentUser().getEmail().equals(document.getData().get("owner").toString())){
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
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });

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
                                book.put("owner",dbAuth.getCurrentUser().getEmail());

                                // Add a new document with a generated ID

                                db.collection("books").document(title)
                                        .set(book)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
                                            }
                                        });
                                Log.d(TAG, "No such document");
                                Toast.makeText(BookDetails.this, "Añadido correctamente", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(BookDetails.this, "Error al añadir: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                db.collection("books").document(title)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                Toast.makeText(BookDetails.this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
