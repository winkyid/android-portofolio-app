package com.slimmy.portoapps.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

object PengalamanHandler {
    const val TABLE = "data_hal_pengalaman"

    fun createTable(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                perusahaan TEXT, 
                posisi TEXT, 
                tahun_mulai TEXT, 
                tahun_selesai TEXT, 
                deskripsi TEXT,
                tasks TEXT
            )
        """)
    }

    fun syncData(db: SQLiteDatabase) {
        val list = listOf(

            mapOf(
                "perusahaan" to "KOPKAR, ICC Telkom Palangka Raya",
                "posisi" to "Teknisi Jaringan",
                "tahun_mulai" to "Januari 2022",
                "tahun_selesai" to "Maret 2022",
                "deskripsi" to "Membantu tim senior sebagai teknisi jaringan.",
                "tasks" to "Network Maintenance|Installation & Configuration Network|Fix issue network problem"
            ),
            mapOf(
                "perusahaan" to "Dinas Komunikasi dan Informatika Kota Palangka Raya",
                "posisi" to "Aplikasi Teknologi Informatika (APTIKA)",
                "tahun_mulai" to "Juli 2025",
                "tahun_selesai" to "Agustus 2025",
                "deskripsi" to "Ikut Berkontribusi dalam pengembangan infrakstruktur DISKOMINFO.",
                "tasks" to "Konfigurasi API untuk layanan aplikasi deteksi banjir|Membantu menyiapkan perkenalan teknologi baru ke masyarakat|Mengoptimalkan Website Seluruh cakupan kota"
            )
        )
        
        list.forEach { item ->
            val values = ContentValues().apply {
                put("perusahaan", item["perusahaan"])
                put("posisi", item["posisi"])
                put("tahun_mulai", item["tahun_mulai"])
                put("tahun_selesai", item["tahun_selesai"])
                put("deskripsi", item["deskripsi"])
                put("tasks", item["tasks"])
            }
            db.insert(TABLE, null, values)
        }
    }
}