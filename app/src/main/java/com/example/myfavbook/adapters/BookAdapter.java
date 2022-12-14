package com.example.myfavbook.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfavbook.R;
import com.example.myfavbook.activities.BookDetails;
import com.example.myfavbook.entities.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    // creating variables for arraylist and context.
    private final ArrayList<Book> bookInfoArrayList;
    private final Context mContext;

    // creating constructor for array list and context.
    public BookAdapter(ArrayList<Book> bookInfoArrayList, Context mContext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        // inside on bind view holder method we are setting ou data to each UI component.
        Book bookInfo = bookInfoArrayList.get(position);
        holder.nameTV.setText(bookInfo.getTitle());
        holder.publisherTV.setText(bookInfo.getPublisher());
        holder.pageCountTV.setText("Number of Pages : " + bookInfo.getPageCount());
        holder.dateTV.setText(bookInfo.getPublishedDate());

        // below line is use to set image from URL in our image view.
        Picasso.get().load(bookInfo.getThumbnail()).into(holder.bookIV);

        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener(v -> {
            // inside on click listener method we are calling a new activity and passing all the data of that item in next intent.
            Intent i = new Intent(mContext, BookDetails.class);
            i.putExtra("title", bookInfo.getTitle());
            i.putExtra("subtitle", bookInfo.getSubtitle());
            i.putExtra("publisher", bookInfo.getPublisher());
            i.putExtra("publishedDate", bookInfo.getPublishedDate());
            i.putExtra("description", bookInfo.getDescription());
            i.putExtra("pageCount", bookInfo.getPageCount());
            i.putExtra("thumbnail", bookInfo.getThumbnail());
            i.putExtra("previewLink", bookInfo.getPreviewLink());
            i.putExtra("infoLink", bookInfo.getInfoLink());
            i.putExtra("buyLink", bookInfo.getBuyLink());

            // after passing that data we are starting our new intent.
            mContext.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        // inside get item count method we are returning the size of our array list.
        return bookInfoArrayList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        // below line is use to initialize our text view and image views.
        TextView nameTV, publisherTV, pageCountTV, dateTV;
        ImageView bookIV;

        public BookViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.idTVBookTitle);
            publisherTV = itemView.findViewById(R.id.idTVpublisher);
            pageCountTV = itemView.findViewById(R.id.idTVPageCount);
            dateTV = itemView.findViewById(R.id.idTVDate);
            bookIV = itemView.findViewById(R.id.idIVbook);
        }
    }
}
