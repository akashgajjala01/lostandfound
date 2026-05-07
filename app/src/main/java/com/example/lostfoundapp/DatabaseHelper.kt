package com.example.lostfoundapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "LostFoundDB"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "adverts"

        private const val COL_ID = "id"
        private const val COL_TYPE = "type"
        private const val COL_NAME = "name"
        private const val COL_PHONE = "phone"
        private const val COL_CATEGORY = "category"
        private const val COL_DESCRIPTION = "description"
        private const val COL_LOCATION = "location"
        private const val COL_IMAGE = "image"
        private const val COL_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TYPE TEXT,
                $COL_NAME TEXT,
                $COL_PHONE TEXT,
                $COL_CATEGORY TEXT,
                $COL_DESCRIPTION TEXT,
                $COL_LOCATION TEXT,
                $COL_IMAGE TEXT,
                $COL_DATE TEXT
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertAdvert(advert: Advert): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(COL_TYPE, advert.type)
        values.put(COL_NAME, advert.name)
        values.put(COL_PHONE, advert.phone)
        values.put(COL_CATEGORY, advert.category)
        values.put(COL_DESCRIPTION, advert.description)
        values.put(COL_LOCATION, advert.location)
        values.put(COL_IMAGE, advert.imageUri)
        values.put(COL_DATE, advert.dateTime)

        val result = db.insert(TABLE_NAME, null, values)
        db.close()

        return result != -1L
    }

    fun getAllAdverts(): ArrayList<Advert> {
        val list = ArrayList<Advert>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COL_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val advert = Advert(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    type = cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                    location = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)),
                    imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE)),
                    dateTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                )
                list.add(advert)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }

    fun getAdvertsByCategory(category: String): ArrayList<Advert> {
        val list = ArrayList<Advert>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $COL_CATEGORY = ? ORDER BY $COL_ID DESC",
            arrayOf(category)
        )

        if (cursor.moveToFirst()) {
            do {
                val advert = Advert(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    type = cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                    location = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)),
                    imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE)),
                    dateTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                )
                list.add(advert)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }

    fun getAdvertById(id: Int): Advert? {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $COL_ID = ?",
            arrayOf(id.toString())
        )

        var advert: Advert? = null

        if (cursor.moveToFirst()) {
            advert = Advert(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                type = cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                location = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)),
                imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE)),
                dateTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
            )
        }

        cursor.close()
        db.close()

        return advert
    }

    fun deleteAdvert(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()

        return result > 0
    }
}