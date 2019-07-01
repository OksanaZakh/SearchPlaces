package com.ozakharchenko.placesearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ozakharchenko.placesearch.R
import com.ozakharchenko.placesearch.model.PlaceItem
import com.ozakharchenko.placesearch.utils.CATEGORY
import com.ozakharchenko.placesearch.utils.SearchCategory
import com.ozakharchenko.placesearch.viewmodel.PlacesViewModel
import com.ozakharchenko.placesearch.viewmodel.Resource

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val model = ViewModelProviders.of(this).get(PlacesViewModel::class.java)
        model.getPlaces("", getCategory()).observe(this, Observer<Resource<List<PlaceItem>>> {
        })
    }

    private fun getCategory(): SearchCategory {
        val category: SearchCategory by lazy {
            when (intent.getStringExtra(CATEGORY)) {
                getString(R.string.entertainment) -> SearchCategory.ENTERTAINMENT
                getString(R.string.art)-> SearchCategory.ART
                getString(R.string.cafes_and_restaurants) -> SearchCategory.FOOD
                getString(R.string.sightseeing) -> SearchCategory.SIGHTSEEING
                getString(R.string.educational) -> SearchCategory.EDUCATIONAL
                getString(R.string.events) -> SearchCategory.EVENTS
                getString(R.string.government) -> SearchCategory.GOVERNMENT
                getString(R.string.medical) -> SearchCategory.MEDICAL
                getString(R.string.public_transport) ->SearchCategory.TRANSPORT
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
}
