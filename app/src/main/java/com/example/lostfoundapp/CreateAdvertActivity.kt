package com.example.lostfoundapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class CreateAdvertActivity : AppCompatActivity() {

    private lateinit var radioGroupType: RadioGroup
    private lateinit var radioLost: RadioButton
    private lateinit var radioFound: RadioButton

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var etDescription: EditText
    private lateinit var etLocation: EditText

    private lateinit var btnSelectImage: Button
    private lateinit var imgPreview: ImageView
    private lateinit var btnSave: Button

    private lateinit var databaseHelper: DatabaseHelper

    private var selectedImageUri: Uri? = null

    private val IMAGE_PICK_CODE = 100

    private val categories = arrayOf(
        "Electronics",
        "Pets",
        "Wallets",
        "Keys",
        "Documents",
        "Bags",
        "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advert)

        databaseHelper = DatabaseHelper(this)

        radioGroupType = findViewById(R.id.radioGroupType)
        radioLost = findViewById(R.id.radioLost)
        radioFound = findViewById(R.id.radioFound)

        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        etDescription = findViewById(R.id.etDescription)
        etLocation = findViewById(R.id.etLocation)

        btnSelectImage = findViewById(R.id.btnSelectImage)
        imgPreview = findViewById(R.id.imgPreview)
        btnSave = findViewById(R.id.btnSave)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        btnSelectImage.setOnClickListener {
            openImagePicker()
        }

        btnSave.setOnClickListener {
            saveAdvert()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data

            selectedImageUri?.let {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imgPreview.setImageURI(it)
            }
        }
    }

    private fun saveAdvert() {
        val type = if (radioLost.isChecked) "Lost" else "Found"
        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val category = spinnerCategory.selectedItem.toString()
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
            etLocation.error = "Enter location"
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val dateTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        val advert = Advert(
            id = 0,
            type = type,
            name = name,
            phone = phone,
            category = category,
            description = description,
            location = location,
            imageUri = selectedImageUri.toString(),
            dateTime = dateTime
        )

        val inserted = databaseHelper.insertAdvert(advert)

        if (inserted) {
            Toast.makeText(this, "Advert saved successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Advert could not be saved", Toast.LENGTH_SHORT).show()
        }
    }
}