
---

# 📱 Android Portfolio App — Profil Diri & Portofolio

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?style=flat-square" />
  <img src="https://img.shields.io/badge/Language-Kotlin-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/UI-XML%20Layout-orange?style=flat-square" />
  <img src="https://img.shields.io/badge/License-Slimmy_V1-purple?style=flat-square" />
</p>

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

## 🆕 Pembaruan Versi 1.5.0

Berikut adalah ringkasan perubahan dan peningkatan signifikan pada versi terbaru:

### **🏗️ Perubahan Arsitektur & Core**
- **Package Refactoring**: Migrasi total package name dari `com.portfolio.app` ke `com.slimmy.portoapps` untuk standarisasi penamaan proyek.
- **Database v2 & Optimized Auto-Sync**: Implementasi sistem sinkronisasi data yang lebih cerdas. Data di-*reset* hanya pada *cold start* aplikasi menggunakan flag sesi, menghilangkan lag 0.1 detik saat navigasi antar halaman.
- **Improved Data Model**: Penambahan kolom `tasks` pada tabel pengalaman kerja untuk mendukung daftar tugas (bullet points) yang lebih detail.

### **🎨 Peningkatan UI/UX**
- **Custom UI Components**: Implementasi **Custom Toast UI** bergaya iOS/Modern dan **Custom Welcome Dialog** saat aplikasi pertama kali dibuka.
- **Fluid Animations**: Penerapan **Fade Animation** yang konsisten pada seluruh transisi halaman (About, File Manager, PDF Viewer, dan Tab Utama).
- **Portrait Lock**: Aplikasi kini dikunci pada mode **Portrait** untuk menjaga konsistensi tata letak elemen UI.
- **Responsive Stats**: Angka statistik di halaman Profil kini bersifat dinamis (berdasarkan jumlah data riil di database) bukan lagi data dummy.

### **📄 Generator CV ATS & File Management**
- **Standard ATS PDF**: Generator CV telah diperbarui ke standar **Applicant Tracking System (ATS)** dengan font yang lebih rapat (ukuran font diturunkan 2 tingkat), layout tabel presisi, dan perataan tahun di pojok kanan menggunakan *TabStops*.
- **Interactive PDF**: Bagian Header CV kini bersifat interaktif dengan tautan yang dapat diklik (Clickable Links) untuk Email, WhatsApp, LinkedIn, GitHub, dan Website.
- **Built-in File Manager**: Penambahan fitur pengelolaan file CV di direktori `Download/Portfolio App/` yang dilengkapi dengan fitur *Delete*, *Share*, *Buka Folder*, serta *Internal PDF Viewer* yang jernih dengan latar belakang kertas putih bersih.

### **🔧 Fitur Tambahan & Integrasi**
- **Smart Skill Synchronizer**: Sistem secara otomatis mengekstrak teknologi yang digunakan dalam proyek portofolio untuk ditambahkan ke daftar keahlian secara dinamis.
- **Office & Design Skills**: Penambahan otomatis daftar skill aplikasi perkantoran (Word, Excel, PP) dan UI/UX Design (Figma).
- **Online Update System**: Implementasi fitur **Cek Pembaruan** yang terhubung ke API eksternal untuk memastikan pengguna selalu menggunakan versi aplikasi terbaru.

---

## 📅 Log Pembaruan (04/01/2026 21:08)
- **Swipe Navigation**: Menambahkan dukungan navigasi antar 6 halaman inti menggunakan gestur swipe kiri/kanan (ViewPager2 implementation).
- **Exit Confirmation**: Menonaktifkan tombol back standar untuk navigasi balik antar tab inti, digantikan dengan dialog konfirmasi keluar aplikasi saat menekan back di halaman utama.
- **Enhanced Overlay UI**: Perbaikan penanganan halaman overlay (About, File Manager, dll) agar terpisah dari alur swipe halaman inti.
- **Visual Polish**: Perbaikan ikon pada Welcome Dialog dan penambahan efek rounded pada logo aplikasi di halaman informasi.
