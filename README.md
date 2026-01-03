
---

# 📱 Android Portfolio App — Profil Diri & Portofolio

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?style=flat-square" />
  <img src="https://img.shields.io/badge/Language-Kotlin-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/UI-XML%20Layout-orange?style=flat-square" />
  <img src="https://img.shields.io/badge/License-Slimmy_V1-purple?style=flat-square" />
</p>

---
# Disclaimer
Branch versi-1.0.2 tidak di update dan tidak sync karena proses pengembangannya dihasilkan di luar repository kami tetapi pengembangan ini dibangun dari baseline versi 1.0 dan di release diluar dari standar struktur kode standar dari repo ini. 
Repository :
https://github.com/Ipannnn21/Porto_Van_1NR
Download (bisa di download melalui tab release v1.0.2-ext-irfan atau melalui dibawah ini):
not set link
---
Aplikasi **Profil Diri & Portofolio Android** yang berisi **CV digital**, **riwayat pendidikan**, **pengalaman kerja**, **keahlian**, **portofolio**, hingga **kontak pribadi**. Dibangun menggunakan **Kotlin**, **Navigation Component**, **Fragment-based UI**, **SQLite**, serta **DataStore** untuk pengelolaan tema dan mode gelap/terang.

---

## ✨ Fitur Utama

<table>
<thead>
<tr>
<th>Fitur</th>
<th>Status / Implementasi</th>
</tr>
</thead>
<tbody>
<tr>
<td><b>6 Fragment/Halaman</b></td>
<td>Tentang Saya, Pendidikan, Pengalaman, Skills, Portofolio, Kontak</td>
</tr>
<tr>
<td><b>Bottom Navigation</b></td>
<td>Navigasi antar halaman menggunakan <i>Navigation Component</i></td>
</tr>
<tr>
<td><b>Dark / Light Mode</b></td>
<td>Pengaturan tema disimpan permanen via <b>DataStore Preferences</b></td>
</tr>
<tr>
<td><b>Download CV (PDF)</b></td>
<td>Generate PDF menggunakan <code>iText</code></td>
</tr>
<tr>
<td><b>Galeri Portofolio</b></td>
<td>RecyclerView Grid Layout untuk menampilkan proyek</td>
</tr>
<tr>
<td><b>SQLite Database</b></td>
<td>Penyimpanan lokal untuk data portofolio, pengalaman, pendidikan, dan lainnya</td>
</tr>
</tbody>
</table>

---

## 🧠 Teknologi yang Digunakan

* Kotlin
* Android Navigation Component
* Fragments
* UI/UX berbasis XML
* RecyclerView (List & Grid)
* DataStore Preferences
* SQLite Database
* iText PDF Generator

---

## 📂 Struktur Proyek

```
android-portfolio-app/
├── app/src/main/java/com/portfolio/app/
│   ├── MainActivity.kt
│   ├── data/
│   │   └── DatabaseHelper.kt         # SQLite + Data Classes
│   ├── util/
│   │   ├── ThemeManager.kt           # DataStore Manager
│   │   └── PdfGenerator.kt           # CV PDF Generator
│   └── ui/
│       ├── tentang/TentangFragment.kt
│       ├── pendidikan/PendidikanFragment.kt + Adapter
│       ├── pengalaman/PengalamanFragment.kt + Adapter
│       ├── skill/SkillFragment.kt + Adapter
│       ├── portofolio/PortofolioFragment.kt + Adapter (Grid)
│       └── kontak/KontakFragment.kt
```

---

## 🚀 Cara Menggunakan Proyek

### **1. Clone via Android Studio (tanpa download manual)**

Bisa langsung dari Android Studio:

```
Android Studio → Get from VCS → Pilih GitHub → Paste URL Repository → Clone
```

Atau versi lengkapnya:

1. Buka Android Studio
2. Pilih menu **"Get from Version Control"** atau **"Clone Repository"**
3. Pilih **GitHub**
4. Paste URL repo ke kolom Git
5. Klik **Clone**

---

### **2. Import Manual**

1. Download folder `android-portfolio-app`
2. Buka Android Studio
3. Pilih:

   ```
   File → Open → pilih folder android-portfolio-app
   ```
4. Tunggu proses **Gradle Sync**
5. Jalankan aplikasi di emulator atau device Android

---

## 📄 Lisensi

Proyek **Android_Portofolio_App** dilisensikan di bawah **Slimmy License v1.0**.

Lisensi ini memberikan izin luas untuk:

* Menggunakan, menyalin, memodifikasi, dan mendistribusikan aplikasi
* Membuat dan menyebarkan turunan proyek (derivative works)
* Penggunaan komersial dan sublicensing

Dengan ketentuan utama:

* **Atribusi ke Slimmy Projects wajib dipertahankan**
* Perubahan signifikan harus **didokumentasikan dengan jelas**
* Tidak diperkenankan menggunakan nama, logo, atau merek **Slimmy Projects** untuk promosi tanpa izin tertulis
* Lisensi mencakup **grant paten** dengan klausul **patent retaliation**
* Perangkat lunak disediakan **“AS IS” tanpa garansi**

Detail lengkap lisensi dapat dilihat pada file **LICENSE** di repositori ini.

```
Slimmy License v1.0
Copyright (c) 2025 Slimmy Projects
```

---

## ⭐ Support

Jangan lupa beri ⭐ di GitHub untuk mendukung pengembangan proyek ini.

---
