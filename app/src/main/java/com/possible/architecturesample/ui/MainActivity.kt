package com.possible.architecturesample.ui

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.possible.architecturesample.BookApplication
import com.possible.architecturesample.R
import com.possible.architecturesample.data.ControllerResult
import com.possible.architecturesample.data.Subscriptor
import com.possible.architecturesample.data.controllers.BaseController
import com.possible.architecturesample.data.controllers.BookController
import com.possible.architecturesample.data.models.Book
import com.possible.architecturesample.data.network.ControllerCallback
import com.possible.architecturesample.data.network.requests.BookRequest
import com.possible.architecturesample.di.ActivityComponent
import com.possible.architecturesample.di.ActivityModule
import com.possible.architecturesample.di.DaggerActivityComponent

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.util.ArrayList

import javax.inject.Inject

class MainActivity : AppCompatActivity(), Subscriptor {

    @Inject lateinit var bookController: BookController

    private var activityComponent: ActivityComponent? = null

    private val subscriptedControllers = ArrayList<BaseController>()
    private var adapter: BookAdapter? = null

    private var recyclerView: RecyclerView? = null
    private var errorText: TextView? = null
    private var progressBar: ProgressBar? = null

    private var destroyedBySystem: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        errorText = findViewById(R.id.error_text) as TextView
        progressBar = findViewById(R.id.progress_bar) as ProgressBar

        adapter = BookAdapter(this)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = adapter

        bookController.loadBooks(this, true, BookRequest(), object : ControllerCallback<ControllerResult<List<Book>>>() {
            override fun onControllerNext(result: ControllerResult<List<Book>>) {
                onBooksReceived(result.result, result.isNetworkError)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        menuInflater.inflate(R.menu.main_menu, menu)

        invalidateOptionsMenu()

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_database) {
            copyDb()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    public override fun onResume() {
        super.onResume()
        destroyedBySystem = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        destroyedBySystem = true
    }

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeAllControllers()
    }

    override fun getSubscriptorTag(): String {
        return TAG
    }

    private fun unsubscribeAllControllers() {
        val tag = subscriptorTag
        for (controller in subscriptedControllers) {
            controller.unsubscribe(tag, !destroyedBySystem)
        }
    }

    override fun addSubscriptedController(controller: BaseController) {
        if (!subscriptedControllers.contains(controller)) {
            subscriptedControllers.add(controller)
        }
    }

    private fun onBooksReceived(result: List<Book>, networkError: Boolean) {
        if (networkError) {
            recyclerView!!.visibility = View.GONE
            progressBar!!.visibility = View.GONE
            errorText!!.visibility = View.VISIBLE
        } else {
            recyclerView!!.visibility = View.VISIBLE
            progressBar!!.visibility = View.GONE
            errorText!!.visibility = View.GONE
            adapter!!.setBookList(result)
        }
    }

    private fun injectDependencies() {
        activityComponent = DaggerActivityComponent
                .builder()
                .applicationComponent((application as BookApplication).appComponent)
                .activityModule(ActivityModule(this)).build()
        inject(activityComponent)
    }

    private fun inject(component: ActivityComponent) {
        component.inject(this)
    }

    private fun copyDb() {
        try {
            val sd = Environment.getExternalStorageDirectory()
            val data = Environment.getDataDirectory()

            if (sd.canWrite()) {
                val currentDBPath = "//data//com.possible.architecturesample//databases//books.db"
                val currentDB = File(data, currentDBPath)
                val backupDB = File(sd, "books.db")
                if (currentDB.exists()) {
                    val src = FileInputStream(currentDB).channel
                    val dst = FileOutputStream(backupDB).channel
                    dst.transferFrom(src, 0, src.size())
                    src.close()
                    dst.close()
                    Toast.makeText(this, "DB Exported to " + backupDB.absolutePath, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "DB Not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Can't write to SD card", Toast.LENGTH_SHORT).show()
            }

        } catch (e: FileNotFoundException) {
            Toast.makeText(this, "FileNotFound: " + e.message, Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(this, "IOException: " + e.message, Toast.LENGTH_LONG).show()
        }

    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
