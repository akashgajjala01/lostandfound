package com.example.lostfoundapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ShowItemsActivity : AppCompatActivity() {

    private lateinit var spinnerFilter: Spinner
    private lateinit var listViewItems: ListView
    private lateinit var databaseHelper: DatabaseHelper

    private var advertList = ArrayList<Advert>()

    private val filterCategories = arrayOf(
        "All",
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
        setContentView(R.layout.activity_show_items)

        spinnerFilter = findViewById(R.id.spinnerFilter)
        listViewItems = findViewById(R.id.listViewItems)
        databaseHelper = DatabaseHelper(this)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterCategories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = adapter

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = filterCategories[position]

                advertList = if (selectedCategory == "All") {
                    databaseHelper.getAllAdverts()
                } else {
                    databaseHelper.getAdvertsByCategory(selectedCategory)
                }

                listViewItems.adapter = AdvertAdapter(this@ShowItemsActivity, advertList)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        listViewItems.setOnItemClickListener { _, _, position, _ ->
            val advert = advertList[position]
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("advert_id", advert.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        advertList = databaseHelper.getAllAdverts()
        listViewItems.adapter = AdvertAdapter(this, advertList)
    }
}