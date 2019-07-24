package com.ozakharchenko.placesearch.ui.search

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.ozakharchenko.placesearch.R
import com.ozakharchenko.placesearch.repository.PlaceItem
import com.ozakharchenko.placesearch.ui.BaseLocationActivity
import com.ozakharchenko.placesearch.ui.listeners.OnAddToFavouriteListener
import com.ozakharchenko.placesearch.ui.listeners.OnItemClickListener
import com.ozakharchenko.placesearch.ui.listeners.OnLocationChangedListener
import com.ozakharchenko.placesearch.utils.CATEGORY
import com.ozakharchenko.placesearch.utils.SearchCategory
import com.ozakharchenko.placesearch.viewmodel.PlacesViewModel
import com.ozakharchenko.placesearch.viewmodel.Resource

class SearchActivity : BaseLocationActivity(), OnItemClickListener, OnAddToFavouriteListener {

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
    }

    override fun onStart() {
        super.onStart()
        setupViewModel()
    }

    private fun setupViewModel() {
        placesViewModel = ViewModelProviders.of(this).get(PlacesViewModel::class.java)
        getData(query, location)
    }

    override fun getData(query: String, location: String?) {
        Log.e(TAG, "Actual loc is 1 $location")
        placesViewModel.getPlaces(category = getCategory(), query = query, coordinates = location ?: defaultLocation)
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
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = intent.getStringExtra(CATEGORY)
        }

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
                getData(query?.toLowerCase() ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getData(newText?.toLowerCase() ?: "")
                return true
            }
        })
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


    override fun getLocation(onLocationChangedListener: OnLocationChangedListener) {
        if (checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            with(LocationServices.getFusedLocationProviderClient(this)){lastLocation.addOnSuccessListener {
                if (it != null) {
                    onLocationChangedListener.onLocationChanged(it.latitude.toString() + "," + it.longitude.toString())
                }}
            }
        }
    }
}
