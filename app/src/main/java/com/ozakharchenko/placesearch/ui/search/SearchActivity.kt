package com.ozakharchenko.placesearch.ui.search

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ozakharchenko.placesearch.R
import com.ozakharchenko.placesearch.repository.PlaceItem
import com.ozakharchenko.placesearch.ui.BaseLocationActivity
import com.ozakharchenko.placesearch.ui.listeners.OnAddToFavouriteListener
import com.ozakharchenko.placesearch.ui.listeners.OnItemClickListener
import com.ozakharchenko.placesearch.utils.CATEGORY
import com.ozakharchenko.placesearch.utils.getCategory
import com.ozakharchenko.placesearch.viewmodel.PlacesViewModel
import com.ozakharchenko.placesearch.viewmodel.Resource

class SearchActivity : BaseLocationActivity(), OnItemClickListener, OnAddToFavouriteListener {

    private var places: List<PlaceItem> = ArrayList()
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var placesViewModel: PlacesViewModel
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        placesViewModel = ViewModelProviders.of(this).get(PlacesViewModel::class.java)
        setupView()
        setupRecycler()
    }

    override fun getData(query: String, location: String?) {
        placesViewModel.getPlaces(
            category = getCategory(intent.getStringExtra(CATEGORY)),
            query = query,
            coordinates = location ?: defaultLocation
        )
            .observe(this, Observer {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        when {
                            it.data == null || it.data.isEmpty() -> makeToast(getString(R.string.download_error))
                            else -> {
                                places = it.data
                                searchAdapter.setPlaces(places.sortedBy { placeItem -> placeItem.distance })
                                searchAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                    Resource.Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    Resource.Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        makeToast(getString(R.string.download_error))
                    }
                }
            })
    }

    private fun setupRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter()
        recyclerView.adapter = searchAdapter
        searchAdapter.setItemClickListener(this)
        searchAdapter.setAddToFavouriteListener(this)
    }

    private fun setupView() {
        recyclerView = findViewById(R.id.rvList)
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = intent.getStringExtra(CATEGORY)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo((getSystemService(Context.SEARCH_SERVICE) as SearchManager)
                .getSearchableInfo(ComponentName(this, SearchActivity::class.java)))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getData(query?.toLowerCase() ?: "", location)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getData(newText?.toLowerCase() ?: "", location)
                return true
            }
        })
        menu.findItem(R.id.action_location).setOnMenuItemClickListener { updateLocation() }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemClick(itemPosition: Int) {
        //TODO (go to detailed activity)
        Log.e(TAG, "Item clicked $itemPosition")
    }

    override fun onAddToFavourite(itemPosition: Int) {
        //TODO (add to DB)
        Log.e(TAG, "Item added to db $itemPosition")
    }
}
