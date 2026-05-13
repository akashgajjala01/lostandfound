package com.example.lostfoundapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var etRadius: EditText
    private lateinit var btnApplyRadius: Button
    private lateinit var btnShowAll: Button
    private lateinit var databaseHelper: DatabaseHelper

    private var userLocation: LatLng = LatLng(-37.8476, 145.1149)

    private val locationRequestCode = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        databaseHelper = DatabaseHelper(this)

        etRadius = findViewById(R.id.etRadius)
        btnApplyRadius = findViewById(R.id.btnApplyRadius)
        btnShowAll = findViewById(R.id.btnShowAll)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment

        mapFragment.getMapAsync(this)

        btnApplyRadius.setOnClickListener {
            val radiusText = etRadius.text.toString().trim()

            if (radiusText.isEmpty()) {
                Toast.makeText(this, "Enter radius in km", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val radiusKm = radiusText.toDoubleOrNull()

            if (radiusKm == null || radiusKm <= 0) {
                Toast.makeText(this, "Enter valid radius", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showItemsWithinRadius(radiusKm)
        }

        btnShowAll.setOnClickListener {
            showAllItems()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true

        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationRequestCode
            )
            return
        }

        googleMap.isMyLocationEnabled = true

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                userLocation = LatLng(location.latitude, location.longitude)

                Toast.makeText(
                    this,
                    "Current location selected",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                userLocation = LatLng(-37.8476, 145.1149)

                Toast.makeText(
                    this,
                    "Using Deakin Burwood location for emulator testing",
                    Toast.LENGTH_SHORT
                ).show()
            }

            showAllItems()

        }.addOnFailureListener {
            userLocation = LatLng(-37.8476, 145.1149)

            Toast.makeText(
                this,
                "Location failed. Using Deakin Burwood location",
                Toast.LENGTH_SHORT
            ).show()

            showAllItems()
        }
    }

    private fun showAllItems() {
        googleMap.clear()

        googleMap.addMarker(
            MarkerOptions()
                .position(userLocation)
                .title("Your Current Location")
                .snippet("Radius search starts from here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )

        val adverts = databaseHelper.getAllAdverts()

        for (advert in adverts) {
            var lat = advert.latitude
            var lng = advert.longitude

            if (lat == 0.0 && lng == 0.0) {
                lat = -37.8476
                lng = 145.1149
            }

            val itemPosition = LatLng(lat, lng)

            googleMap.addMarker(
                MarkerOptions()
                    .position(itemPosition)
                    .title("${advert.postType}: ${advert.name}")
                    .snippet("${advert.category} - ${advert.location}")
            )
        }

        addDemoMarkers()

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(userLocation, 11f)
        )
    }

    private fun showItemsWithinRadius(radiusKm: Double) {
        googleMap.clear()

        googleMap.addMarker(
            MarkerOptions()
                .position(userLocation)
                .title("Your Current Location")
                .snippet("Showing items within $radiusKm km")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )

        var foundCount = 0

        val adverts = databaseHelper.getAllAdverts()

        for (advert in adverts) {
            var lat = advert.latitude
            var lng = advert.longitude

            if (lat == 0.0 && lng == 0.0) {
                lat = -37.8476
                lng = 145.1149
            }

            val itemPosition = LatLng(lat, lng)
            val distanceKm = calculateDistanceKm(userLocation, itemPosition)

            if (distanceKm <= radiusKm) {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(itemPosition)
                        .title("${advert.postType}: ${advert.name}")
                        .snippet("${String.format("%.2f", distanceKm)} km away - ${advert.location}")
                )

                foundCount++
            }
        }

        val demoItems = listOf(
            DemoMapItem("Deakin Burwood Campus", -37.8476, 145.1149),
            DemoMapItem("Melbourne Airport", -37.6690, 144.8410)
        )

        for (item in demoItems) {
            val itemPosition = LatLng(item.latitude, item.longitude)
            val distanceKm = calculateDistanceKm(userLocation, itemPosition)

            if (distanceKm <= radiusKm) {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(itemPosition)
                        .title(item.title)
                        .snippet("${String.format("%.2f", distanceKm)} km away")
                )

                foundCount++
            }
        }

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(userLocation, 11f)
        )

        Toast.makeText(
            this,
            "$foundCount item(s) found within $radiusKm km",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun addDemoMarkers() {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(-37.8476, 145.1149))
                .title("Deakin Burwood Campus")
                .snippet("Demo lost/found item near Deakin")
        )

        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(-37.6690, 144.8410))
                .title("Melbourne Airport")
                .snippet("Demo lost/found item near Melbourne Airport")
        )
    }

    private fun calculateDistanceKm(start: LatLng, end: LatLng): Double {
        val results = FloatArray(1)

        Location.distanceBetween(
            start.latitude,
            start.longitude,
            end.latitude,
            end.longitude,
            results
        )

        return results[0] / 1000.0
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (
            requestCode == locationRequestCode &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            userLocation = LatLng(-37.8476, 145.1149)

            Toast.makeText(
                this,
                "Permission denied. Using Deakin Burwood location",
                Toast.LENGTH_SHORT
            ).show()

            showAllItems()
        }
    }

    data class DemoMapItem(
        val title: String,
        val latitude: Double,
        val longitude: Double
    )
}