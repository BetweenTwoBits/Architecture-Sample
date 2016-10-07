package com.possible.architecturesample.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.possible.architecturesample.R
import com.possible.architecturesample.data.models.Book
import com.squareup.picasso.Picasso

import java.util.ArrayList

class BookAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val bookData = ArrayList<Book>()

    fun setBookList(bookData: List<Book>) {
        this.bookData.clear()
        this.bookData.addAll(bookData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.book_row, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bookViewHolder = holder as BookViewHolder
        val book = bookData[position]

        if (book != null) {
            bookViewHolder.bindBook(book)
        }
    }

    override fun getItemCount(): Int {
        return bookData.size
    }

    private class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bookImage: ImageView
        private val bookTitleView: TextView
        private val bookAuthorView: TextView

        init {
            bookImage = itemView.findViewById(R.id.book_image) as ImageView
            bookTitleView = itemView.findViewById(R.id.book_title) as TextView
            bookAuthorView = itemView.findViewById(R.id.book_author) as TextView
        }

        fun bindBook(book: Book) {
            val bookUrl = book.imageUrl
            val bookTitle = book.title
            val bookAuthor = book.author

            if (bookUrl != null) {
                Picasso.with(itemView.context).load(bookUrl).placeholder(R.mipmap.ic_launcher).into(bookImage)
            } else {
                Picasso.with(itemView.context).load(R.mipmap.ic_launcher).into(bookImage)
            }

            if (bookTitle != null) {
                bookTitleView.visibility = View.VISIBLE
                bookTitleView.text = bookTitle
            } else {
                bookTitleView.visibility = View.GONE
                bookTitleView.text = ""
            }

            if (bookAuthor != null) {
                val prefixedAuthor = "Author: " + bookAuthor

                bookAuthorView.visibility = View.VISIBLE
                bookAuthorView.text = prefixedAuthor
            } else {
                bookAuthorView.visibility = View.GONE
                bookAuthorView.text = ""
            }
        }
    }
}
