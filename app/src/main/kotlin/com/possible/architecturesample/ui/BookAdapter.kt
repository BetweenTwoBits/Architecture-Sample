package com.possible.architecturesample.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.possible.architecturesample.R
import com.possible.architecturesample.data.models.Book
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.book_row.view.bookAuthorText
import kotlinx.android.synthetic.main.book_row.view.bookImageView
import kotlinx.android.synthetic.main.book_row.view.bookTitleText
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

        bookViewHolder.bindBook(book)
    }

    override fun getItemCount(): Int {
        return bookData.size
    }

    private class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindBook(book: Book) {
            val bookUrl = book.imageUrl
            val bookTitle = book.title
            val bookAuthor = book.author

            if (bookUrl != null) {
                Picasso.with(itemView.context).load(bookUrl).placeholder(R.mipmap.ic_launcher).into(itemView.bookImageView)
            } else {
                Picasso.with(itemView.context).load(R.mipmap.ic_launcher).into(itemView.bookImageView)
            }

            if (bookTitle != null) {
                itemView.bookTitleText.visibility = View.VISIBLE
                itemView.bookTitleText.text = bookTitle
            } else {
                itemView.bookTitleText.visibility = View.GONE
                itemView.bookTitleText.text = ""
            }

            if (bookAuthor != null) {
                val prefixedAuthor = "Author: " + bookAuthor

                itemView.bookAuthorText.visibility = View.VISIBLE
                itemView.bookAuthorText.text = prefixedAuthor
            } else {
                itemView.bookAuthorText.visibility = View.GONE
                itemView.bookAuthorText.text = ""
            }
        }
    }
}
