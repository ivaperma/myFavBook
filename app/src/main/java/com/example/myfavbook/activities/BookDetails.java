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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfavbook.R;
import com.example.myfavbook.entities.Book;
import com.example.myfavbook.entities.Review;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class BookDetails extends AppCompatActivity {

    // creating variables for strings,text view, image views and button.
    String title, subtitle, publisher, publishedDate, description, thumbnail, previewLink, infoLink, buyLink, review;
    EditText mReview, mRate;
    String sTextFromMRate;

    int pageCount;
    private ArrayList<String> authors;
    private ArrayList<Book> userBooks;
    private ArrayList<Book> favBookLibrary = new ArrayList<>();









    TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV, review1, review2, review3, rating;
    Button previewBtn, buyBtn, addBtn, deleteBtn, rateBtn, reviewBtn;
    private ImageView bookIV;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth dbAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        loadLibrary();
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
        review1 = findViewById(R.id.review1);
        review2 = findViewById(R.id.review2);
        review3 = findViewById(R.id.review3);
        rating = findViewById(R.id.rating);


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
        //setTextRate();
        loadReviewsForBook();


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
            //LOAD BOOKS OF USER
            userBooks = new ArrayList<>();
            db.collection(dbAuth.getCurrentUser().getEmail())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData().get("publisher"));
                                userBooks.add(setBookValues(document));
                            }
                            Book book = new Book(title, subtitle,publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);
                            if (userBooks.size() > 0) {
                                for (Book bookInUser : userBooks) {
                                    if (bookInUser.getTitle().equals(book.getTitle()))
                                        Toast.makeText(BookDetails.this, "Ya tienes este libro! No se puede añadir", Toast.LENGTH_SHORT).show();
                                    else {
                                        if (Objects.nonNull(dbAuth.getCurrentUser().getEmail())) {
                                            db.collection(Objects.requireNonNull(dbAuth.getCurrentUser()).getEmail()).document(title)
                                                    .set(book)
                                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                                                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
                                            Log.d(TAG, "No such document");
                                            Toast.makeText(BookDetails.this, "Añadido correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            } else {
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
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });


            /*DocumentReference docRef = db.collection("books").document(title);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                }
                //if document exists we don´t add a new book
                    /*if (document.exists()) {
                        if (!Objects.equals(Objects.requireNonNull(dbAuth.getCurrentUser()).getEmail(), Objects.requireNonNull(Objects.requireNonNull(document.getData()).get("owner")).toString())){
            if (Objects.nonNull(dbAuth.getCurrentUser().getEmail())) {
                db.collection(Objects.requireNonNull(dbAuth.getCurrentUser()).getEmail()).document(title)
                        .set(libro)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
                Log.d(TAG, "No such document");
                Toast.makeText(BookDetails.this, "Añadido correctamente", Toast.LENGTH_SHORT).show();
            }

            //retrieving document/book from firebase
            /*DocumentReference docRef = db.collection("books").document(title);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                }
                    //if document exists we don´t add a new book
                    /*if (document.exists()) {
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

                        // ------------------------------------------------------- se añade aqui -----------------------------------------------
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
            });*/



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


        // -------------------------------------REVIEW AQUI -----------------------------
        reviewBtn.setOnClickListener(view ->{
            /*String review = mReview.getText().toString().trim();
            // Update one field, creating the document if it does not already exist.
            Map<String, Object> data = new HashMap<>();
            data.put("review", review);*/

            Review review = new Review(dbAuth.getCurrentUser().getEmail(), mReview.getText().toString().trim());

            int i = 0;
            for (Book bookToFind : favBookLibrary) {
                if (bookToFind.getTitle().equals(title))
                    break;
                i++;
            }
            favBookLibrary.get(i).getReviews().add(review);
            db.collection("books").document(title)
                    .set(favBookLibrary.get(i), SetOptions.merge());

            startActivity(new Intent(getApplicationContext(), Books.class));
        });

        rateBtn.setOnClickListener(view -> {
            /*String rate = mRate.getText().toString().trim();
            // Update one field, creating the document if it does not already exist.
            Map<String, Object> data = new HashMap<>();
            data.put("rate", rate);*/

            int i = 0;
            for (Book bookToFind : favBookLibrary) {
                if (bookToFind.getTitle().equals(title))
                    break;
                i++;
            }
            favBookLibrary.get(i).incrementByOneTotalRates();
            favBookLibrary.get(i).addPointsToRate(Double.valueOf(String.valueOf(mRate.getText())));



            db.collection("books").document(title)
                    .set(favBookLibrary.get(i), SetOptions.merge());
            startActivity(new Intent(getApplicationContext(), Books.class));


        });
    }

    private void loadReviewsForBook() {
        DocumentReference docRef = db.collection("books").document(title);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        int i = 0;
                        for (Book bookToFind : favBookLibrary) {
                            if (bookToFind.getTitle().equals(title))
                                break;
                            i++;
                        }
                        //set rate
                        if (i > 0) {
                            Double rate = favBookLibrary.get(i).getAverageRate();
                            if (Double.isNaN(rate))
                                rate = 0.0;
                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(2);
                            rating.setText("Rating: " + String.valueOf(df.format(rate)) + "/5");
                        }

                        final ObjectMapper mapper = new ObjectMapper();
                        if (favBookLibrary.get(i).getReviews().size() > 0) {
                            Review review = mapper.convertValue(favBookLibrary.get(i).getReviews().get(0), Review.class);
                            review1.setText(review.getReview() + " - " + review.getUserEmail());
                        }
                        else {
                            review3.setText("");
                        }
                        if (favBookLibrary.get(i).getReviews().size() > 1) {
                            Review review = mapper.convertValue(favBookLibrary.get(i).getReviews().get(1), Review.class);
                            review2.setText(review.getReview() + " - " + review.getUserEmail());
                        }
                        else {
                            review3.setText("");
                        }
                        if (favBookLibrary.get(i).getReviews().size() > 2) {
                            Review review = mapper.convertValue(favBookLibrary.get(i).getReviews().get(2), Review.class);
                            review3.setText(review.getReview() + " - " + review.getUserEmail());
                        } else {
                            review3.setText("");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
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

    /*private void setTextRate() {

            DocumentReference docRef = db.collection(dbAuth.getCurrentUser().getEmail()).document(title);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            if (document.getData().get("rate") != null) {

                                int z = 0;
                                for (Book bookToFind : favBookLibrary) {
                                    if (bookToFind.getTitle().equals(title))
                                        break;
                                    z++;
                                }

                                //mRate.setText((String) document.getData().get("rate"));
                                //equal Rating bar value to rate value
                                sTextFromMRate = mRate.getText().toString();

                            } else {
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
        }*/



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
                Double.valueOf(String.valueOf(document.getData().get("rate")))
        );
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
}
