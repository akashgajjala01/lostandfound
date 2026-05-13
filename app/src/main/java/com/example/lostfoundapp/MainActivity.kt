package com.example.lostfoundapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnCreateAdvert: Button
    private lateinit var btnShowItems: Button
    private lateinit var btnShowMap: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCreateAdvert = findViewById(R.id.btnCreateAdvert)
        btnShowItems = findViewById(R.id.btnShowItems)
        btnShowMap = findViewById(R.id.btnShowMap)

        btnCreateAdvert.setOnClickListener {
            startActivity(Intent(this, CreateAdvertActivity::class.java))
        }

        btnShowItems.setOnClickListener {
            startActivity(Intent(this, ShowItemsActivity::class.java))
        }

        btnShowMap.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }
}