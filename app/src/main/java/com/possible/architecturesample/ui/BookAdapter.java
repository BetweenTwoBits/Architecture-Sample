package com.possible.architecturesample.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.possible.architecturesample.R;
import com.possible.architecturesample.data.models.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Book> bookData = new ArrayList<>();
    private Context context;

    public BookAdapter(Context context) {
        this.context = context;
    }

    public void setBookList(List<Book> bookData) {
        this.bookData.clear();
        this.bookData.addAll(bookData);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_row, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookViewHolder bookViewHolder = (BookViewHolder) holder;
        Book book = bookData.get(position);

        if (book != null) {
            bookViewHolder.bindBook(book);
        }
    }

    @Override
    public int getItemCount() {
        return bookData.size();
    }

    private static class BookViewHolder extends RecyclerView.ViewHolder {

        private ImageView bookImage;
        private TextView bookTitleView;
        private TextView bookAuthorView;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookImage = (ImageView) itemView.findViewById(R.id.book_image);
            bookTitleView = (TextView) itemView.findViewById(R.id.book_title);
            bookAuthorView = (TextView) itemView.findViewById(R.id.book_author);
        }

        public void bindBook(Book book) {
            String bookUrl = book.getImageUrl();
            String bookTitle = book.getTitle();
            String bookAuthor = book.getAuthor();

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
