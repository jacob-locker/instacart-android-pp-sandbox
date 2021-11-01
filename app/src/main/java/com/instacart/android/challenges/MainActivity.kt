package com.instacart.android.challenges

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), MainActivityViewModel.UpdateListener {

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()

        viewModel.setStateUpdateListener(this)
    }

    private fun renderItemList(state: ItemListViewState) {
        supportActionBar?.apply {
            title = state.toolbarTitle
        }
        adapter.update(state.items)
    }

    private fun bindViews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ItemAdapter()
        recyclerView.adapter = adapter
    }

    override fun onUpdate(state: ItemListViewState) {
        renderItemList(state)
    }

    override fun onError(throwable: Throwable?) {
        Toast.makeText(this, "Oops, an error occurred $throwable", Toast.LENGTH_LONG).show()
    }
}
