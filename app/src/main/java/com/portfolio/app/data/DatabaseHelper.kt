package com.portfolio.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "portfolio.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_PROFILE = "profile"
        const val TABLE_PENDIDIKAN = "pendidikan"
        const val TABLE_PENGALAMAN = "pengalaman"
        const val TABLE_SKILL = "skill"
        const val TABLE_PORTOFOLIO = "portofolio"

        const val COLUMN_ID = "id"
        const val COLUMN_NAMA = "nama"
        const val COLUMN_TITLE = "title"
        const val COLUMN_BIO = "bio"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_LINKEDIN = "linkedin"
        const val COLUMN_GITHUB = "github"
        
        const val COLUMN_INSTITUSI = "institusi"
        const val COLUMN_JURUSAN = "jurusan"
        const val COLUMN_TAHUN_MULAI = "tahun_mulai"
        const val COLUMN_TAHUN_SELESAI = "tahun_selesai"
        const val COLUMN_DESKRIPSI = "deskripsi"
        
        const val COLUMN_PERUSAHAAN = "perusahaan"
        const val COLUMN_POSISI = "posisi"
        
        const val COLUMN_LEVEL = "level"
        const val COLUMN_KATEGORI = "kategori"
        
        const val COLUMN_JUDUL = "judul"
        const val COLUMN_TEKNOLOGI = "teknologi"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_LINK = "link"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_PROFILE (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_NAMA TEXT,
                $COLUMN_TITLE TEXT,
                $COLUMN_BIO TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_PHONE TEXT,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_LINKEDIN TEXT,
                $COLUMN_GITHUB TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_PENDIDIKAN (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_INSTITUSI TEXT,
                $COLUMN_JURUSAN TEXT,
                $COLUMN_TAHUN_MULAI TEXT,
                $COLUMN_TAHUN_SELESAI TEXT,
                $COLUMN_DESKRIPSI TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_PENGALAMAN (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PERUSAHAAN TEXT,
                $COLUMN_POSISI TEXT,
                $COLUMN_TAHUN_MULAI TEXT,
                $COLUMN_TAHUN_SELESAI TEXT,
                $COLUMN_DESKRIPSI TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_SKILL (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAMA TEXT,
                $COLUMN_LEVEL INTEGER,
                $COLUMN_KATEGORI TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_PORTOFOLIO (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_JUDUL TEXT,
                $COLUMN_DESKRIPSI TEXT,
                $COLUMN_TEKNOLOGI TEXT,
                $COLUMN_IMAGE TEXT,
                $COLUMN_LINK TEXT
            )
        """)

        insertSampleData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROFILE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PENDIDIKAN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PENGALAMAN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SKILL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PORTOFOLIO")
        onCreate(db)
    }

    private fun insertSampleData(db: SQLiteDatabase) {
        val profileValues = ContentValues().apply {
            put(COLUMN_ID, 1)
            put(COLUMN_NAMA, "John Doe")
            put(COLUMN_TITLE, "Full Stack Developer")
            put(COLUMN_BIO, "Seorang developer berpengalaman dengan passion dalam membangun aplikasi web dan mobile yang inovatif.")
            put(COLUMN_EMAIL, "john.doe@email.com")
            put(COLUMN_PHONE, "+62 812-3456-7890")
            put(COLUMN_ADDRESS, "Jakarta, Indonesia")
            put(COLUMN_LINKEDIN, "linkedin.com/in/johndoe")
            put(COLUMN_GITHUB, "github.com/johndoe")
        }
        db.insert(TABLE_PROFILE, null, profileValues)

        val pendidikan1 = ContentValues().apply {
            put(COLUMN_INSTITUSI, "Universitas Indonesia")
            put(COLUMN_JURUSAN, "Teknik Informatika")
            put(COLUMN_TAHUN_MULAI, "2018")
            put(COLUMN_TAHUN_SELESAI, "2022")
            put(COLUMN_DESKRIPSI, "Lulus dengan IPK 3.8")
        }
        db.insert(TABLE_PENDIDIKAN, null, pendidikan1)

        val pengalaman1 = ContentValues().apply {
            put(COLUMN_PERUSAHAAN, "Tech Startup XYZ")
            put(COLUMN_POSISI, "Senior Developer")
            put(COLUMN_TAHUN_MULAI, "2023")
            put(COLUMN_TAHUN_SELESAI, "Sekarang")
            put(COLUMN_DESKRIPSI, "Memimpin tim pengembangan")
        }
        db.insert(TABLE_PENGALAMAN, null, pengalaman1)

        val skills = listOf(
            Triple("Kotlin", 90, "Programming"),
            Triple("Java", 85, "Programming"),
            Triple("Android", 88, "Mobile"),
            Triple("Firebase", 80, "Backend"),
            Triple("Git", 85, "Tools")
        )
        skills.forEach { (nama, level, kategori) ->
            val skillValues = ContentValues().apply {
                put(COLUMN_NAMA, nama)
                put(COLUMN_LEVEL, level)
                put(COLUMN_KATEGORI, kategori)
            }
            db.insert(TABLE_SKILL, null, skillValues)
        }

        val portfolios = listOf(
            arrayOf("E-Commerce App", "Aplikasi e-commerce lengkap", "Kotlin, Firebase", "https://github.com/johndoe/ecommerce"),
            arrayOf("Task Manager", "Aplikasi manajemen tugas", "Kotlin, Room", "https://github.com/johndoe/taskmanager"),
            arrayOf("Weather App", "Aplikasi cuaca real-time", "Kotlin, Retrofit", "https://github.com/johndoe/weather"),
            arrayOf("Portfolio App", "Aplikasi portofolio personal", "Kotlin, SQLite", "https://github.com/johndoe/portfolio")
        )
        portfolios.forEach { (judul, deskripsi, teknologi, link) ->
            val portValues = ContentValues().apply {
                put(COLUMN_JUDUL, judul)
                put(COLUMN_DESKRIPSI, deskripsi)
                put(COLUMN_TEKNOLOGI, teknologi)
                put(COLUMN_LINK, link)
            }
            db.insert(TABLE_PORTOFOLIO, null, portValues)
        }
    }

    fun getProfile(): Profile? {
        val db = readableDatabase
        val cursor = db.query(TABLE_PROFILE, null, "$COLUMN_ID = ?", arrayOf("1"), null, null, null)
        return if (cursor.moveToFirst()) {
            Profile(
                nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                bio = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIO)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                linkedin = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINKEDIN)),
                github = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GITHUB))
            ).also { cursor.close() }
        } else {
            cursor.close()
            null
        }
    }

    fun getPendidikan(): List<Pendidikan> {
        val list = mutableListOf<Pendidikan>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PENDIDIKAN, null, null, null, null, null, "$COLUMN_TAHUN_SELESAI DESC")
        while (cursor.moveToNext()) {
            list.add(Pendidikan(
                institusi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTITUSI)),
                jurusan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JURUSAN)),
                tahunMulai = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAHUN_MULAI)),
                tahunSelesai = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAHUN_SELESAI)),
                deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI))
            ))
        }
        cursor.close()
        return list
    }

    fun getPengalaman(): List<Pengalaman> {
        val list = mutableListOf<Pengalaman>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PENGALAMAN, null, null, null, null, null, "$COLUMN_TAHUN_SELESAI DESC")
        while (cursor.moveToNext()) {
            list.add(Pengalaman(
                perusahaan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PERUSAHAAN)),
                posisi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSISI)),
                tahunMulai = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAHUN_MULAI)),
                tahunSelesai = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAHUN_SELESAI)),
                deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI))
            ))
        }
        cursor.close()
        return list
    }

    fun getSkills(): List<Skill> {
        val list = mutableListOf<Skill>()
        val db = readableDatabase
        val cursor = db.query(TABLE_SKILL, null, null, null, null, null, "$COLUMN_LEVEL DESC")
        while (cursor.moveToNext()) {
            list.add(Skill(
                nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)),
                level = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)),
                kategori = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KATEGORI))
            ))
        }
        cursor.close()
        return list
    }

    fun getPortofolio(): List<Portofolio> {
        val list = mutableListOf<Portofolio>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PORTOFOLIO, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            list.add(Portofolio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                judul = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JUDUL)),
                deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI)),
                teknologi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEKNOLOGI)),
                image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)),
                link = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK))
            ))
        }
        cursor.close()
        return list
    }
}

data class Profile(
    val nama: String,
    val title: String,
    val bio: String,
    val email: String,
    val phone: String,
    val address: String,
    val linkedin: String,
    val github: String
)

data class Pendidikan(
    val institusi: String,
    val jurusan: String,
    val tahunMulai: String,
    val tahunSelesai: String,
    val deskripsi: String
)

data class Pengalaman(
    val perusahaan: String,
    val posisi: String,
    val tahunMulai: String,
    val tahunSelesai: String,
    val deskripsi: String
)

data class Skill(
    val nama: String,
    val level: Int,
    val kategori: String
)

data class Portofolio(
    val id: Int,
    val judul: String,
    val deskripsi: String,
    val teknologi: String,
    val image: String?,
    val link: String
)
