package com.slimmy.portoapps.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

object SkillHandler {
    const val TABLE = "data_hal_skill"

    fun createTable(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                nama_skill TEXT, kategori_skill TEXT
            )
        """)
    }

    fun syncData(db: SQLiteDatabase) {
        // Daftar Skill Baru sesuai permintaan
        val list = mutableListOf(
            "Kotlin" to "Programming",
            "Android Jetpack" to "Mobile",
            "Word" to "Office",
            "Excel" to "Office",
            "Powerpoint" to "Office",
            "Figma" to "UI/UX Design"
        )

        // Membaca data dari tabel portofolio untuk dijadikan skill tambahan
        val cursor = db.query("data_hal_portofolio", arrayOf("teknologi"), null, null, null, null, null)
        val portoSkills = mutableSetOf<String>()
        while (cursor.moveToNext()) {
            val teks = cursor.getString(0)
            // Asumsi teknologi dipisah koma (contoh: Kotlin, Firebase, Retrofit)
            teks.split(",").forEach { 
                val skill = it.trim()
                if (skill.isNotEmpty()) portoSkills.add(skill)
            }
        }
        cursor.close()

        // Tambahkan skill unik dari portofolio ke list utama
        portoSkills.forEach { s ->
            // Cek apakah skill sudah ada di list dasar agar tidak duplikat
            if (list.none { it.first.equals(s, ignoreCase = true) }) {
                list.add(s to "Tech Stack Proyek")
            }
        }

        list.forEach { s ->
            val values = ContentValues().apply {
                put("nama_skill", s.first)
                put("kategori_skill", s.second)
            }
            db.insert(TABLE, null, values)
        }
    }
}
