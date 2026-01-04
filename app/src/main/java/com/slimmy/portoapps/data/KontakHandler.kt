package com.slimmy.portoapps.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

object KontakHandler {
    const val TABLE = "data_hal_kontak"

    fun createTable(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE (
                id INTEGER PRIMARY KEY,
                email TEXT,
                phone TEXT,
                lokasi_display TEXT,
                lokasi_maps_url TEXT,
                website TEXT,
                github TEXT,
                linkedin TEXT
            )
        """)
    }

    fun syncData(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put("id", 1)
            put("email", "winkykurniatama@gmail.com")
            put("phone", "085248062410")
            put("lokasi_display", "Palangka Raya, Indonesia")
            put("lokasi_maps_url", "https://maps.app.goo.gl/DRBBFfjTcX7eC8HC7")
            put("website", "https://winky.vercel.app")
            put("github", "https://github.com/winkyid")
            put("linkedin", "https://www.linkedin.com/in/winky-kurniatama-97a0182aa")
        }
        db.insert(TABLE, null, values)
    }
}