package com.possible.tourrefactorsample.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.possible.tourrefactorsample.R;
import com.possible.tourrefactorsample.data.network.responses.BookResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<BookResponse> bookData = new ArrayList<>();
    private Context context;

    public TourAdapter(Context context) {
        this.context = context;
    }

    public void setBookList(List<BookResponse> bookData) {
        this.bookData.clear();
        this.bookData.addAll(bookData);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_row, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TourViewHolder tourViewHolder = (TourViewHolder) holder;
        BookResponse book = bookData.get(position);

        if (book != null) {
            tourViewHolder.bindBook(book);
        }
    }

    @Override
    public int getItemCount() {
        return bookData.size();
    }

    private static class TourViewHolder extends RecyclerView.ViewHolder {

        private ImageView bookImage;
        private TextView bookTitleView;
        private TextView bookAuthorView;

        public TourViewHolder(View itemView) {
            super(itemView);
            bookImage = (ImageView) itemView.findViewById(R.id.book_image);
            bookTitleView = (TextView) itemView.findViewById(R.id.book_title);
            bookAuthorView = (TextView) itemView.findViewById(R.id.book_author);
        }

        public void bindBook(BookResponse book) {
            String bookUrl = book.imageUrl;
            String bookTitle = book.title;
            String bookAuthor = book.author;

            if (bookUrl != null) {
                Picasso.with(itemView.getContext()).load(bookUrl).placeholder(R.mipmap.ic_launcher).into(bookImage);
            } else {
                Picasso.with(itemView.getContext()).load(R.mipmap.ic_launcher).into(bookImage);
            }

            if (bookTitle != null) {
                bookTitleView.setText(bookTitle);
            } else {
                bookTitleView.setText("");
            }

            if (bookAuthor != null) {
                bookAuthorView.setText(bookAuthor);
            } else {
                bookAuthorView.setText("");
            }
        }
    }
}
