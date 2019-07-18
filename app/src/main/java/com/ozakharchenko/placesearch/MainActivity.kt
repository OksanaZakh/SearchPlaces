package com.ozakharchenko.placesearch

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ozakharchenko.placesearch.utils.CATEGORY
import com.ozakharchenko.placesearch.utils.CategoryCustomLayout
import com.ozakharchenko.placesearch.ui.search.SearchActivity
import pub.devrel.easypermissions.EasyPermissions
import android.widget.Toast
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ozakharchenko.placesearch.utils.LOCATION
import com.ozakharchenko.placesearch.utils.LOCATION_KYIV_CENTER
import pub.devrel.easypermissions.AfterPermissionGranted
import android.app.Activity
import android.content.pm.PackageManager


const val REQUEST_LOCATION_PERMISSION = 1

class MainActivity : AppCompatActivity() {

    private var isPermissionGranted: Boolean = false
    val TAG = "Main"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var defaultLocation: String = LOCATION_KYIV_CENTER
    var location: String = ""
    val perms = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStart() {
        super.onStart()
        if (checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                Log.e(TAG, "My Location is ${it?.latitude} ${it?.longitude}")
                if (it == null) {
                    location = defaultLocation
                    Toast.makeText(
                        this,
                        "Please turn on location or Kiev center will be chosen as default!",
                        Toast.LENGTH_LONG
                    ).show()
                } else
                    location = it.latitude.toString() + "," + it.longitude.toString()
            }
            isPermissionGranted = true
        }
        else requestLocationPermission()
    }

    fun goToCategory(view: View) {
        val activityIntent = Intent(this, SearchActivity::class.java).apply {
            putExtra(CATEGORY, ((view as CategoryCustomLayout).getText()))
            putExtra(LOCATION, location)
        }
        startActivity(activityIntent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    fun requestLocationPermission() {
        if (EasyPermissions.hasPermissions(this, *perms)) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Please grant the location permission",
                REQUEST_LOCATION_PERMISSION,
                *perms
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1001) {
            Log.e(TAG, "It is gps")
        }
    }

}
