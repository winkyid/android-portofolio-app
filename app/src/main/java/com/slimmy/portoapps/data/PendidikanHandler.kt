package com.slimmy.portoapps.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

object PendidikanHandler {
    const val TABLE = "data_hal_pendidikan"

    fun createTable(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                level_name TEXT, institusi TEXT, jurusan TEXT, 
                tahun_mulai TEXT, tahun_selesai TEXT, deskripsi TEXT
            )
        """)
    }

    fun syncData(db: SQLiteDatabase) {
        val list = listOf(
            arrayOf("Sekolah Menengah Kejuruan", "SMKN Al-Ishlah Palangka Raya", "Teknik Komputer & Jaringan", "2021", "2023"),
            arrayOf("Pendidikan Tinggi", "Universitas Indonesia", "S1 - Sistem Informasi", "2023", "Sekarang")
        )
        list.forEach { p ->
            val values = ContentValues().apply {
                put("level_name", p[0]); put("institusi", p[1]); put("jurusan", p[2])
                put("tahun_mulai", p[3]); put("tahun_selesai", p[4]); put("deskripsi", "")
            }
            db.insert(TABLE, null, values)
        }
    }
}