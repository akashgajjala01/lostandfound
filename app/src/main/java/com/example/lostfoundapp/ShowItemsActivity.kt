package com.example.lostfoundapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class ShowItemsActivity : AppCompatActivity() {

    private lateinit var spinnerFilter: Spinner
    private lateinit var listViewItems: ListView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var advertList: ArrayList<Advert>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_items)

        spinnerFilter = findViewById(R.id.spinnerFilter)
        listViewItems = findViewById(R.id.listViewItems)
        databaseHelper = DatabaseHelper(this)

        val categories = arrayOf(
            "All",
            "Electronics",
            "Pets",
            "Wallet",
            "Bag",
            "Keys",
            "Other"
        )

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        spinnerFilter.adapter = spinnerAdapter

        loadAllAdverts()

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = categories[position]

                advertList = if (selectedCategory == "All") {
                    databaseHelper.getAllAdverts()
                } else {
                    databaseHelper.getAdvertsByCategory(selectedCategory)
                }

                listViewItems.adapter = AdvertAdapter(this@ShowItemsActivity, advertList)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
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

        if (::databaseHelper.isInitialized) {
            loadAllAdverts()
        }
    }

    private fun loadAllAdverts() {
        advertList = databaseHelper.getAllAdverts()
        listViewItems.adapter = AdvertAdapter(this, advertList)
    }
}