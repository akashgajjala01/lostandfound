package com.example.lostfoundapp

data class Advert(
    val id: Int,
    val type: String,
    val name: String,
    val phone: String,
    val category: String,
    val description: String,
    val location: String,
    val imageUri: String,
    val dateTime: String
)