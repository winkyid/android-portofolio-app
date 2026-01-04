package com.slimmy.portoapps.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "portfolio.db"
        private const val DATABASE_VERSION = 2
        private var isSyncedInThisSession = false
    }

    override fun onCreate(db: SQLiteDatabase) {
        ProfileHandler.createTable(db)
        PendidikanHandler.createTable(db)
        PengalamanHandler.createTable(db)
        SkillHandler.createTable(db)
        PortofolioHandler.createTable(db)
        KontakHandler.createTable(db)
        
        syncAllData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        resetDatabase(db)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        // Optimasi: Hanya sinkronisasi satu kali per sesi aplikasi dibuka (cold start)
        // Ini menghilangkan lag saat pindah antar halaman
        if (!isSyncedInThisSession) {
            resetDatabase(db)
            isSyncedInThisSession = true
        }
    }

    private fun resetDatabase(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS ${ProfileHandler.TABLE}")
        db.execSQL("DROP TABLE IF EXISTS ${PendidikanHandler.TABLE}")
        db.execSQL("DROP TABLE IF EXISTS ${PengalamanHandler.TABLE}")
        db.execSQL("DROP TABLE IF EXISTS ${SkillHandler.TABLE}")
        db.execSQL("DROP TABLE IF EXISTS ${PortofolioHandler.TABLE}")
        db.execSQL("DROP TABLE IF EXISTS ${KontakHandler.TABLE}")
        onCreate(db)
    }

    private fun syncAllData(db: SQLiteDatabase) {
        PortofolioHandler.syncData(db)
        ProfileHandler.syncData(db)
        PendidikanHandler.syncData(db)
        PengalamanHandler.syncData(db)
        SkillHandler.syncData(db)
        KontakHandler.syncData(db)
    }

    fun getDataStats(): Triple<Int, Int, Int> {
        val db = readableDatabase
        val countPendidikan = db.rawQuery("SELECT COUNT(*) FROM ${PendidikanHandler.TABLE}", null).use {
            if (it.moveToFirst()) it.getInt(0) else 0
        }
        val countPengalaman = db.rawQuery("SELECT COUNT(*) FROM ${PengalamanHandler.TABLE}", null).use {
            if (it.moveToFirst()) it.getInt(0) else 0
        }
        val countProyek = db.rawQuery("SELECT COUNT(*) FROM ${PortofolioHandler.TABLE}", null).use {
            if (it.moveToFirst()) it.getInt(0) else 0
        }
        return Triple(countPengalaman, countPendidikan, countProyek)
    }

    fun getProfile(): Profile? {
        val db = readableDatabase
        val cursorProfile = db.query(ProfileHandler.TABLE, null, "id = ?", arrayOf("1"), null, null, null)
        val cursorKontak = db.query(KontakHandler.TABLE, null, "id = ?", arrayOf("1"), null, null, null)
        
        return if (cursorProfile.moveToFirst() && cursorKontak.moveToFirst()) {
            Profile(
                nama = cursorProfile.getString(cursorProfile.getColumnIndexOrThrow("nama")),
                title = cursorProfile.getString(cursorProfile.getColumnIndexOrThrow("title")),
                bio = cursorProfile.getString(cursorProfile.getColumnIndexOrThrow("bio")),
                email = cursorKontak.getString(cursorKontak.getColumnIndexOrThrow("email")),
                phone = cursorKontak.getString(cursorKontak.getColumnIndexOrThrow("phone")),
                address = cursorKontak.getString(cursorKontak.getColumnIndexOrThrow("lokasi_display")),
                mapsUrl = cursorKontak.getString(cursorKontak.getColumnIndexOrThrow("lokasi_maps_url")),
                linkedin = cursorKontak.getString(cursorKontak.getColumnIndexOrThrow("linkedin")),
                github = cursorKontak.getString(cursorKontak.getColumnIndexOrThrow("github")),
                website = cursorKontak.getString(cursorKontak.getColumnIndexOrThrow("website")),
                status = cursorProfile.getString(cursorProfile.getColumnIndexOrThrow("status")),
                warganegara = cursorProfile.getString(cursorProfile.getColumnIndexOrThrow("warganegara")),
                tempatLahir = cursorProfile.getString(cursorProfile.getColumnIndexOrThrow("tempat_lahir")),
                tanggalLahir = cursorProfile.getString(cursorProfile.getColumnIndexOrThrow("tanggal_lahir")),
                agama = cursorProfile.getString(cursorProfile.getColumnIndexOrThrow("agama")),
                gender = cursorProfile.getString(cursorProfile.getColumnIndexOrThrow("gender"))
            ).also { 
                cursorProfile.close()
                cursorKontak.close()
            }
        } else { 
            cursorProfile.close()
            cursorKontak.close()
            null 
        }
    }

    fun getPendidikan(): List<Pendidikan> {
        val list = mutableListOf<Pendidikan>()
        val db = readableDatabase
        val cursor = db.query(PendidikanHandler.TABLE, null, null, null, null, null, "id DESC")
        while (cursor.moveToNext()) {
            list.add(Pendidikan(
                levelName = cursor.getString(cursor.getColumnIndexOrThrow("level_name")),
                institusi = cursor.getString(cursor.getColumnIndexOrThrow("institusi")),
                jurusan = cursor.getString(cursor.getColumnIndexOrThrow("jurusan")),
                tahunMulai = cursor.getString(cursor.getColumnIndexOrThrow("tahun_mulai")),
                tahunSelesai = cursor.getString(cursor.getColumnIndexOrThrow("tahun_selesai")),
                deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi"))
            ))
        }
        cursor.close()
        return list
    }

    fun getPengalaman(): List<Pengalaman> {
        val list = mutableListOf<Pengalaman>()
        val db = readableDatabase
        val cursor = db.query(PengalamanHandler.TABLE, null, null, null, null, null, "id DESC")
        while (cursor.moveToNext()) {
            val tasksString = cursor.getString(cursor.getColumnIndexOrThrow("tasks")) ?: ""
            val tasks = if (tasksString.isNotEmpty()) tasksString.split("|") else emptyList()
            
            list.add(Pengalaman(
                perusahaan = cursor.getString(cursor.getColumnIndexOrThrow("perusahaan")),
                posisi = cursor.getString(cursor.getColumnIndexOrThrow("posisi")),
                tahunMulai = cursor.getString(cursor.getColumnIndexOrThrow("tahun_mulai")),
                tahunSelesai = cursor.getString(cursor.getColumnIndexOrThrow("tahun_selesai")),
                deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi")),
                tasks = tasks
            ))
        }
        cursor.close()
        return list
    }

    fun getSkills(): List<Skill> {
        val list = mutableListOf<Skill>()
        val db = readableDatabase
        val cursor = db.query(SkillHandler.TABLE, null, null, null, null, null, "kategori_skill ASC")
        while (cursor.moveToNext()) {
            list.add(Skill(
                nama = cursor.getString(cursor.getColumnIndexOrThrow("nama_skill")),
                kategori = cursor.getString(cursor.getColumnIndexOrThrow("kategori_skill"))
            ))
        }
        cursor.close()
        return list
    }

    fun getPortofolio(): List<Portofolio> {
        val list = mutableListOf<Portofolio>()
        val db = readableDatabase
        val cursor = db.query(PortofolioHandler.TABLE, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            list.add(Portofolio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                judul = cursor.getString(cursor.getColumnIndexOrThrow("judul")),
                deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi")),
                teknologi = cursor.getString(cursor.getColumnIndexOrThrow("teknologi")),
                image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                sourceLink = cursor.getString(cursor.getColumnIndexOrThrow("source_link")),
                liveLink = cursor.getString(cursor.getColumnIndexOrThrow("live_link"))
            ))
        }
        cursor.close()
        return list
    }

    fun getPortofolioById(id: Int): Portofolio? {
        val db = readableDatabase
        val cursor = db.query(PortofolioHandler.TABLE, null, "id = ?", arrayOf(id.toString()), null, null, null)
        return if (cursor.moveToFirst()) {
            Portofolio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                judul = cursor.getString(cursor.getColumnIndexOrThrow("judul")),
                deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi")),
                teknologi = cursor.getString(cursor.getColumnIndexOrThrow("teknologi")),
                image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                sourceLink = cursor.getString(cursor.getColumnIndexOrThrow("source_link")),
                liveLink = cursor.getString(cursor.getColumnIndexOrThrow("live_link"))
            ).also { cursor.close() }
        } else { cursor.close(); null }
    }
}

data class Profile(val nama: String, val title: String, val bio: String, val email: String, val phone: String, val address: String, val mapsUrl: String, val linkedin: String, val github: String, val website: String, val status: String, val warganegara: String, val tempatLahir: String, val tanggalLahir: String, val agama: String, val gender: String)
data class Pendidikan(val levelName: String, val institusi: String, val jurusan: String, val tahunMulai: String, val tahunSelesai: String, val deskripsi: String)
data class Pengalaman(val perusahaan: String, val posisi: String, val tahunMulai: String, val tahunSelesai: String, val deskripsi: String, val tasks: List<String> = emptyList())
data class Skill(val nama: String, val kategori: String)
data class Portofolio(val id: Int, val judul: String, val deskripsi: String, val teknologi: String, val image: String?, val type: String, val sourceLink: String, val liveLink: String)
