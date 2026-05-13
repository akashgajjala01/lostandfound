package com.example.lostfoundapp

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class AdvertAdapter(
    private val context: Context,
    private val advertList: ArrayList<Advert>
) : BaseAdapter() {

    override fun getCount(): Int {
        return advertList.size
    }

    override fun getItem(position: Int): Any {
        return advertList[position]
    }

    override fun getItemId(position: Int): Long {
        return advertList[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_advert, parent, false)

        val itemImage = view.findViewById<ImageView>(R.id.itemImage)
        val itemTitle = view.findViewById<TextView>(R.id.itemTitle)
        val itemCategory = view.findViewById<TextView>(R.id.itemCategory)
        val itemDate = view.findViewById<TextView>(R.id.itemDate)

        val advert = advertList[position]

        itemTitle.text = "${advert.postType}: ${advert.name}"
        itemCategory.text = "Category: ${advert.category}"
        itemDate.text = "Posted: ${advert.date}"

        try {
            if (advert.imageUri.isNotEmpty()) {
                itemImage.setImageURI(Uri.parse(advert.imageUri))
            } else {
                itemImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        } catch (e: Exception) {
            itemImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        return view
    }
}