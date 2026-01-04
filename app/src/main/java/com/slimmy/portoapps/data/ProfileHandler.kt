package com.slimmy.portoapps.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

object ProfileHandler {
    const val TABLE = "data_hal_profile"
    
    fun createTable(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE (
                id INTEGER PRIMARY KEY, nama TEXT, title TEXT, bio TEXT,
                status TEXT, warganegara TEXT, tempat_lahir TEXT, 
                tanggal_lahir TEXT, agama TEXT, gender TEXT
            )
        """)
    }

    fun syncData(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put("id", 1)
            put("nama", "Winky Kurniatama")
            put("title", "Founder Org Slimmy Project")
            put("bio", "Seorang Mahasiswa dengan minat luas dalam mempelajari dan mengeksplorasi berbagai bidang di dunia teknologi, termasuk pengembangan aplikasi, sistem, dan inovasi digital. Terbuka untuk mempelajari konsep baru dan menerapkan solusi kreatif.")
            put("status", "Belum Menikah")
            put("warganegara", "Indonesia")
            put("tempat_lahir", "Palangka Raya")
            put("tanggal_lahir", "07 Juli 2005")
            put("agama", "Islam")
            put("gender", "Laki-laki")
        }
        db.insert(TABLE, null, values)
    }
}