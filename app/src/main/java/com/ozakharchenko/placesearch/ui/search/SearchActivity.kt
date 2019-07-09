package com.ozakharchenko.placesearch.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ozakharchenko.placesearch.R
import com.ozakharchenko.placesearch.model.PlaceItem
import com.ozakharchenko.placesearch.utils.CATEGORY
import com.ozakharchenko.placesearch.utils.SearchCategory
import com.ozakharchenko.placesearch.viewmodel.PlacesViewModel
import com.ozakharchenko.placesearch.viewmodel.Resource

class SearchActivity : AppCompatActivity() {

    private var places: List<PlaceItem> = ArrayList()
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var placesViewModel: PlacesViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupView()
        setupRecycler()
        setupViewModel()
    }

    private fun setupViewModel() {
        placesViewModel = ViewModelProviders.of(this).get(PlacesViewModel::class.java)
        placesViewModel.getPlaces(getCategory()).observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    when {
                        it.data == null || it.data.isEmpty() -> showErrorToast()
                        else -> {
                            places = it.data
                            searchAdapter.places = it.data
                        }
                    }
                }
                Resource.Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    showErrorToast()
                }
            }
        })
    }

    private fun setupRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter()
        recyclerView.adapter = searchAdapter
    }

    private fun setupView() {
        recyclerView = findViewById(R.id.rvList)
        progressBar = findViewById(R.id.progressBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getCategory(): SearchCategory {
        val category: SearchCategory by lazy {
            when (intent.getStringExtra(CATEGORY)) {
                getString(R.string.entertainment) -> SearchCategory.ENTERTAINMENT
                getString(R.string.art) -> SearchCategory.ART
                getString(R.string.cafes_and_restaurants) -> SearchCategory.FOOD
                getString(R.string.sightseeing) -> SearchCategory.SIGHTSEEING
                getString(R.string.educational) -> SearchCategory.EDUCATIONAL
                getString(R.string.events) -> SearchCategory.EVENTS
                getString(R.string.government) -> SearchCategory.GOVERNMENT
                getString(R.string.medical) -> SearchCategory.MEDICAL
                getString(R.string.public_transport) -> SearchCategory.TRANSPORT
                else -> SearchCategory.SHOPPING
            }
        }
        return category
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun showErrorToast() = Toast.makeText(this, R.string.download_error, Toast.LENGTH_LONG).show()
}
