package com.example.lostfoundapp

data class Advert(
    val id: Int,
    val postType: String,
    val name: String,
    val phone: String,
    val category: String,
    val description: String,
    val date: String,
    val location: String,
    val imageUri: String,
    val latitude: Double,
    val longitude: Double
)