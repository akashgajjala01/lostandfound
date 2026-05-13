package com.example.lostfoundapp

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var detailImage: ImageView
    private lateinit var detailTitle: TextView
    private lateinit var detailType: TextView
    private lateinit var detailCategory: TextView
    private lateinit var detailPhone: TextView
    private lateinit var detailDescription: TextView
    private lateinit var detailLocation: TextView
    private lateinit var detailDate: TextView
    private lateinit var btnRemove: Button

    private lateinit var databaseHelper: DatabaseHelper

    private var advertId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        databaseHelper = DatabaseHelper(this)

        detailImage = findViewById(R.id.detailImage)
        detailTitle = findViewById(R.id.detailTitle)
        detailType = findViewById(R.id.detailType)
        detailCategory = findViewById(R.id.detailCategory)
        detailPhone = findViewById(R.id.detailPhone)
        detailDescription = findViewById(R.id.detailDescription)
        detailLocation = findViewById(R.id.detailLocation)
        detailDate = findViewById(R.id.detailDate)
        btnRemove = findViewById(R.id.btnRemove)

        advertId = intent.getIntExtra("advert_id", -1)

        if (advertId != -1) {
            loadAdvertDetails(advertId)
        } else {
            Toast.makeText(this, "Advert not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnRemove.setOnClickListener {
            val deleted = databaseHelper.deleteAdvert(advertId)

            if (deleted) {
                Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to remove advert", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadAdvertDetails(id: Int) {
        val advert = databaseHelper.getAdvertById(id)

        if (advert != null) {
            detailTitle.text = advert.name
            detailType.text = "Type: ${advert.postType}"
            detailCategory.text = "Category: ${advert.category}"
            detailPhone.text = "Phone: ${advert.phone}"
            detailDescription.text = "Description: ${advert.description}"
            detailLocation.text = "Location: ${advert.location}"
            detailDate.text = "Posted: ${advert.date}"

            try {
                if (advert.imageUri.isNotEmpty()) {
                    detailImage.setImageURI(Uri.parse(advert.imageUri))
                } else {
                    detailImage.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } catch (e: Exception) {
                detailImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        } else {
            Toast.makeText(this, "Advert details not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}