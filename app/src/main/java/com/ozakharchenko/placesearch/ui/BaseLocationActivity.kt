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
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.ozakharchenko.placesearch.R
import com.ozakharchenko.placesearch.ui.listeners.OnGpsListener
import com.ozakharchenko.placesearch.ui.listeners.OnLocationChangedListener
import com.ozakharchenko.placesearch.utils.ERROR_MESSAGE
import com.ozakharchenko.placesearch.utils.LOCATION_KYIV_CENTER


const val PERMISSION_REQUEST_CODE = 1000
const val GPS_REQUEST_CODE = 1001

abstract class BaseLocationActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks/*, LocationListener*/ {

    val TAG = "Base Location Activity"
    protected var query = ""
    protected var defaultLocation: String = LOCATION_KYIV_CENTER
    protected var location: String? = null
    protected var isGps = false
    protected var isPermission = false
    private val permissions: Array<String>
    protected var onGpsListener: OnGpsListener
    protected var onLocationChangedListener: OnLocationChangedListener
    private val locationRequest: LocationRequest
    private lateinit var locationApiClient: FusedLocationProviderClient

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
                when (isGps) {
                    true -> getLocation(onLocationChangedListener)
                    false -> onLocationChangedListener.onLocationChanged(defaultLocation)
                }
            }
        }
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = (10 * 1000).toLong()
            fastestInterval = (2 * 1000).toLong()
        }

        permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLocationPermissionAndGps()
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
                    getString(R.string.permission_request),
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
        makeToast(getString(R.string.denied_permission_info))
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        isPermission = true
        turnGpsOn(onGpsListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST_CODE) {
                onGpsListener.gpsStatus(true)// flag maintain before get location
            }
        } else {
            onGpsListener.gpsStatus(false)
            makeToast(getString(R.string.gps_unavailable_info))
        }
    }

    private fun turnGpsOn(onGpsListener: OnGpsListener?) {
        val mSettingsClient = LocationServices.getSettingsClient(this)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)
        if ((getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener?.gpsStatus(true)
        } else {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(this) {
                        onGpsListener?.gpsStatus(true)
                    }
                    .addOnFailureListener(this) { e ->
                        when ((e as ApiException).statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                                try {
                                    val rae = e as ResolvableApiException
                                    rae.startResolutionForResult(this, GPS_REQUEST_CODE)
                                } catch (sie: IntentSender.SendIntentException) {
                                }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage = ERROR_MESSAGE
                                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
        }
    }

    fun getLocation(onLocationChangedListener: OnLocationChangedListener) {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            locationApiClient = LocationServices.getFusedLocationProviderClient(this)
            with(locationApiClient) {
                lastLocation.addOnSuccessListener { location ->
                    if (location == null || location.accuracy > 100) {
                        requestLocationUpdates()
                    } else {
                        onLocationChangedListener.onLocationChanged(location.latitude.toString() + "," + location.longitude.toString())
                    }
                }
            }
        }
    }


    protected fun updateLocation(): Boolean {
        checkLocationPermissionAndGps()
        if(isGps && isPermission){
            requestLocationUpdates()
        }
        return true
    }

    @Throws(SecurityException::class)
    fun requestLocationUpdates() {
        val mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations.isNotEmpty()) {
                    val newLocation = locationResult.locations[0]
                    onLocationChangedListener.onLocationChanged(newLocation.latitude.toString() + "," + newLocation.longitude.toString())
                } else {
                    onLocationChangedListener.onLocationChanged(defaultLocation)
                }
            }
        }
        locationApiClient.requestLocationUpdates(locationRequest, mLocationCallback, null)
    }

    fun makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    abstract fun getData(query: String, location: String?)
}