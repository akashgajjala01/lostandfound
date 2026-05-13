package com.example.lostfoundapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateAdvertActivity : AppCompatActivity() {

    private lateinit var rbLost: RadioButton
    private lateinit var rbFound: RadioButton
    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var spCategory: Spinner
    private lateinit var etDescription: EditText
    private lateinit var etLocation: EditText
    private lateinit var btnGetCurrentLocation: Button
    private lateinit var btnSelectImage: Button
    private lateinit var btnSave: Button
    private lateinit var imgPreview: ImageView

    private lateinit var databaseHelper: DatabaseHelper

    private var selectedImageUri: String = ""
    private var selectedLatitude: Double = 0.0
    private var selectedLongitude: Double = 0.0

    private val imageRequestCode = 101
    private val locationRequestCode = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advert)

        databaseHelper = DatabaseHelper(this)

        rbLost = findViewById(R.id.rbLost)
        rbFound = findViewById(R.id.rbFound)
        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        spCategory = findViewById(R.id.spCategory)
        etDescription = findViewById(R.id.etDescription)
        etLocation = findViewById(R.id.etLocation)
        btnGetCurrentLocation = findViewById(R.id.btnGetCurrentLocation)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnSave = findViewById(R.id.btnSave)
        imgPreview = findViewById(R.id.imgPreview)

        val categories = arrayOf(
            "Electronics",
            "Pets",
            "Wallet",
            "Bag",
            "Keys",
            "Other"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        spCategory.adapter = adapter

        btnGetCurrentLocation.setOnClickListener {
            getCurrentLocation()
        }

        btnSelectImage.setOnClickListener {
            selectImage()
        }

        btnSave.setOnClickListener {
            saveAdvert()
        }
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

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {
                selectedLatitude = location.latitude
                selectedLongitude = location.longitude

                etLocation.setText("Lat: $selectedLatitude, Lng: $selectedLongitude")

                Toast.makeText(
                    this,
                    "Current location selected",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                selectedLatitude = -37.8476
                selectedLongitude = 145.1149

                etLocation.setText("Lat: $selectedLatitude, Lng: $selectedLongitude")

                Toast.makeText(
                    this,
                    "Using Deakin Burwood location for emulator testing",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }.addOnFailureListener {

            selectedLatitude = -37.8476
            selectedLongitude = 145.1149

            etLocation.setText("Lat: $selectedLatitude, Lng: $selectedLongitude")

            Toast.makeText(
                this,
                "Location failed. Using Deakin Burwood location",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, imageRequestCode)
    }

    private fun saveAdvert() {
        val postType = if (rbLost.isChecked) "Lost" else "Found"
        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val category = spCategory.selectedItem.toString()
        val description = etDescription.text.toString().trim()
        val location = etLocation.text.toString().trim()

        if (name.isEmpty()) {
            etName.error = "Enter item name"
            return
        }

        if (phone.isEmpty()) {
            etPhone.error = "Enter phone number"
            return
        }

        if (description.isEmpty()) {
            etDescription.error = "Enter description"
            return
        }

        if (location.isEmpty()) {
            etLocation.error = "Click GET CURRENT LOCATION or enter location"
            return
        }

        if (selectedLatitude == 0.0 && selectedLongitude == 0.0) {
            selectedLatitude = -37.8476
            selectedLongitude = 145.1149
        }

        val date = SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(Date())

        val advert = Advert(
            id = 0,
            postType = postType,
            name = name,
            phone = phone,
            category = category,
            description = description,
            date = date,
            location = location,
            imageUri = selectedImageUri,
            latitude = selectedLatitude,
            longitude = selectedLongitude
        )

        val result = databaseHelper.insertAdvert(advert)

        if (result) {
            Toast.makeText(
                this,
                "Advert saved successfully",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        } else {
            Toast.makeText(
                this,
                "Advert not saved",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (
            requestCode == imageRequestCode &&
            resultCode == RESULT_OK &&
            data != null
        ) {
            val uri: Uri? = data.data

            if (uri != null) {
                selectedImageUri = uri.toString()
                imgPreview.setImageURI(uri)
            }
        }
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
            Toast.makeText(
                this,
                "Location permission denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}