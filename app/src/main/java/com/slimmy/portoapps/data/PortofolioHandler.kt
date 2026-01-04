package com.slimmy.portoapps.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

object PortofolioHandler {
    const val TABLE = "data_hal_portofolio"

    fun createTable(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                judul TEXT, deskripsi TEXT, teknologi TEXT, 
                image TEXT, type TEXT, source_link TEXT, live_link TEXT
            )
        """)
    }

    fun syncData(db: SQLiteDatabase) {
        val list = listOf(
            arrayOf("Web Foodcourt", "Website Bisnis penjualan makanan", "Html, CSS, JavaScript", "Website",
                "https://bit.ly/sc-porto-foodcourt",
                "https://bit.ly/porto-foodcourt"),

            arrayOf("Kalkulator VB6", "Kalkulator sederhana menggunakan Visual Basic 6.0 Dengan Modern UI.", "Java, Visual Basic 6.0", "Windows Java",
                "https://bit.ly/sc-Kalkulator-VB6",
                "https://bit.ly/Kalkulator-VB6"),

            arrayOf("Data Banjir GUI Insert", "Sebuah Website Pendataan Banjir dengan output word,pdf,dan excel menggunakan GUI website", "php, CSS, mySql", "Website",
                "https://bit.ly/sc-data-banjir",
                "https://bit.ly/data-banjir"),

            arrayOf("ChatAI - UI", "Sebuah website untuk chat Ai menggunakan API dari Gemini Google", "Typescript, ReactJS, ShadCDN/UI, FramerMotion, GenAi APi", "Website",
                "https://bit.ly/sc-porto-chatai",
                "https://bit.ly/porto-chatai"),

            arrayOf("SIPEDAS", "SIPEDAS (Sistem Penjadwalan Cerdas) adalah sebuah website yang tujuannya untuk manajemen kelas dengan berbagai fitur yang sangat dimudahkan", "NextJS, ShadCDN/UI, Supabase PostgreSQL, Fonnte WhatsApp API, GenAI API", "Website",
                "https://bit.ly/sc-porto-sipedas",
                "https://bit.ly/porto-sipedas"),

            arrayOf("Semma Ai by Slimmy", "Semma adalah model AI ringan yang dibangun dari dasar model Gemma milik Google yang dikembangkan ulang oleh Slimmy Projects (Winky)", "Python3", "Ai GGUF Model",
                "https://bit.ly/sc-Semma-Ai",
                "https://bit.ly/Semma-Ai"),

            arrayOf("Android Portfolio Apps", "Merupakan Sebuah Aplikasi Portofolio Pribadi dengan Modern UI dan penerapan beberapa komponent dan fragment serta implmentasi beberapa fitur yang berbeda dari biasanya", "Kotlin, Android Jetpack, SQLite3, WebView, ItextPDF", "Android APK",
                "https://bit.ly/sc-portoAndroid",
                "https://bit.ly/rilis-portoAndroid")
        )
        list.forEach { data ->
            val values = ContentValues().apply {
                put("judul", data[0]); put("deskripsi", data[1]); put("teknologi", data[2])
                put("type", data[3]); put("source_link", data[4]); put("live_link", data[5])
            }
            db.insert(TABLE, null, values)
        }
    }
}