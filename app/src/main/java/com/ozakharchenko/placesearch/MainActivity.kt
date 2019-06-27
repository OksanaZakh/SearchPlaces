package com.ozakharchenko.placesearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ozakharchenko.placesearch.utils.CATEGORY
import com.ozakharchenko.placesearch.utils.CategoryCustomLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToCategory(view: View) {
        val activityIntent = Intent(this, SearchActivity::class.java).apply {
            putExtra(CATEGORY, ((view as CategoryCustomLayout).getText()))
        }
        startActivity(activityIntent)
    }
}
