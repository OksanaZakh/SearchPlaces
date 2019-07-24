package com.ozakharchenko.placesearch.ui

import android.Manifest
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.ozakharchenko.placesearch.ui.listeners.OnGpsListener
import com.ozakharchenko.placesearch.ui.listeners.OnLocationChangedListener
import com.ozakharchenko.placesearch.utils.LOCATION_KYIV_CENTER


const val PERMISSION_REQUEST_CODE = 1000
const val GPS_REQUEST_CODE = 1001

abstract class BaseLocationActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    val TAG = "Base Location Activity"
    protected var query = ""
    protected var defaultLocation: String = LOCATION_KYIV_CENTER
    protected var location: String? = null
    protected var isGps = false
    protected var isPermission = false

    private val permissions: Array<String>
    protected var onGpsListener: OnGpsListener
    protected var onLocationChangedListener: OnLocationChangedListener

    init {
        onLocationChangedListener = object : OnLocationChangedListener {
            override fun onLocationChanged(newLocation: String) {
                location = newLocation
                getData(query, location)
            }
        }

        onGpsListener = object : OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                isGps = isGPSEnable
                if (isGps) getLocation(onLocationChangedListener)
            }
        }

        permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onStart() {
        super.onStart()
        checkLocationPermissionAndGps()
        Log.e(TAG, "On Start location 1 $location")
        location = location ?: defaultLocation
        Log.e(TAG, "On Start location 2 $location")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_CODE)
    fun checkLocationPermissionAndGps() {
        if (!EasyPermissions.hasPermissions(this, *permissions)) {
            EasyPermissions.requestPermissions(
                this,
                "Please grant the location permissions!",
                PERMISSION_REQUEST_CODE,
                *permissions
            )
        } else {
            turnGpsOn(onGpsListener)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        onGpsListener.gpsStatus(false)
        isPermission = false
        makeToast("Location permissions are denied :(\nKyiv center is chosen as your default location.")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        isPermission = true
        turnGpsOn(onGpsListener)
    }

    fun makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST_CODE) {
                onGpsListener.gpsStatus(true)// flag maintain before get location
            }
        } else {
            onGpsListener.gpsStatus(false)
            makeToast("GPS is unavailable:(\nKyiv center is chosen as your default location.\nYou can enable GPS any time by pressing location button.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        checkLocationPermissionAndGps()
    }

    private fun turnGpsOn(onGpsListener: OnGpsListener?) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val mSettingsClient = LocationServices.getSettingsClient(this)

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = (10 * 1000).toLong()
            fastestInterval = (2 * 1000).toLong()
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener?.gpsStatus(true)

        } else {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this) {
                    onGpsListener?.gpsStatus(true)
                }
                .addOnFailureListener(this) { e ->
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                            try {
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(this, GPS_REQUEST_CODE)
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.e(TAG, "PendingIntent unable to execute request.")
                            }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage =
                                "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                            Log.e(TAG, errorMessage)

                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    abstract fun getLocation(onLocationChangedListener: OnLocationChangedListener)

    abstract fun getData(query: String, location: String? = defaultLocation)
}